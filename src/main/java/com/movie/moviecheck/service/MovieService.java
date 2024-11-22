package com.movie.moviecheck.service;

import com.movie.moviecheck.controller.MovieListWithCount;
import com.movie.moviecheck.controller.WrapperClass;
import com.movie.moviecheck.converter.GenreConvertor;
import com.movie.moviecheck.converter.MovieConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.ActorDto;
import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.Actor;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.GenreMovie;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.UserGenre;
import com.movie.moviecheck.repository.GenreMovieRepository;
import com.movie.moviecheck.repository.MovieRepository;
import com.movie.moviecheck.repository.UserGenreRepository;
import com.movie.moviecheck.util.ImageUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieConvertor movieConvertor;
    private final UserConvertor userConvertor;
    private final UserService userService;
    private final GenreConvertor genreConvertor;
    private final UserGenreRepository userGenreRepository;
    private final GenreMovieRepository genreMovieRepository;

    public Map<String, Object> getAllMovies(Pageable pageable) {
        // 1. 모든 영화 가져오기
        Page<Movie> movies = movieRepository.findAll(pageable);
        // 2. 필요한 데이터만 포함하는 DTO로 변환
        List<Map<String, Object>> movieDtos = movies.stream()
                                                    .map(movie -> {
                                                        Map<String, Object> dto = new HashMap<>();
                                                        dto.put("movieKey", movie.getMovieKey());
                                                        dto.put("movieTitle", movie.getMovieTitle());
                                                        dto.put("movieImage",movie.getMoviePoster());
                                                        return dto;
                                                    })
                                                    .collect(Collectors.toList());
        // 3. 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("movies", movieDtos);
        response.put("totalPages", movies.getTotalPages());  // 전체 페이지 수
        response.put("currentPage", movies.getNumber());    // 현재 페이지 번호 (0부터 시작)
        response.put("totalElements", movies.getTotalElements()); // 전체 영화 수
    
        return response; // 영화 목록과 페이지네이션 정보 포함한 Map 반환
    }
    
    // 영화 디테일에서 사용
    public MovieDto getMovies(String movieKey) {
    // 영화 엔티티 조회
    Movie movie = movieRepository.findByMovieKey(movieKey);

    // 영화 정보를 DTO로 변환
    MovieDto movieDto = movieConvertor.convertToDto(movie);

    // 영화에 속한 배우 정보 가져오기
    List<ActorDto> actors = movie.getMovieActor().stream()
            .map(movieActor -> {
                Actor actor = movieActor.getActor();
                String base64Profile = ImageUtil.encodeImageToBase64(actor.getActorImage());
                return new ActorDto(
                        actor.getActorKey(),
                        actor.getActorName(),
                        base64Profile
                );
            })
            .collect(Collectors.toList());

    // 배우 정보를 DTO에 추가
    movieDto.setActors(actors);

    return movieDto;
}

    // 영화 디테일 조회
    public ResponseEntity<MovieDto> getMovieDetails(MovieDto movieDto) {
        String movieKey = movieDto.getMovieKey();
    
        // 1. 요청 검증
        if (movieKey == null || movieKey.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
    
        try {
            // 2. Movie 상세 정보 조회
            MovieDto movieDetails = getMovies(movieKey);
            return ResponseEntity.ok(movieDetails);
        } catch (EntityNotFoundException e) {
            // 3. 조회 실패 시 404 Not Found 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    

    // 사용자가 선호하는 영화 가져오는 메서드
public Map<String, Object> getMoviesByUserPreferences(HttpServletRequest request, Pageable pageable) {
    // 1. 세션에서 userKey 가져오기
    HttpSession session = request.getSession(false);
    Integer userKey = (Integer) session.getAttribute("userKey");

    // 2. 유저 정보를 가져오기
    UserDto user = userConvertor.convertToDto(userService.findByKey(userKey));

    // 3. 유저의 선호 장르 가져오기
    List<GenreDto> genres = getUserGenres(user);

    // 4. 선호 장르에 해당하는 영화 가져오기 (페이징 적용)
    Page<MovieDto> moviePage = getMoviesByGenres(genres, pageable);

    // 5. 결과를 HashMap에 담아서 반환
    Map<String, Object> result = new HashMap<>();
    result.put("movies", moviePage.getContent()); // 현재 페이지의 영화 목록
    result.put("totalPages", moviePage.getTotalPages()); // 총 페이지 수
    result.put("currentPage", moviePage.getNumber()); // 현재 페이지 번호
    result.put("totalElements", moviePage.getTotalElements()); // 총 영화 수
    return result;
}

// 유저의 선호 장르 가져오는 메서드
private List<GenreDto> getUserGenres(UserDto user) {
    List<UserGenre> userGenres = userGenreRepository.findByUser_UserKey(user.getUserKey());
    return userGenres.stream()
                     .map(userGenre -> genreConvertor.converToDto(userGenre.getGenre()))
                     .toList();
}

// 장르별 영화 가져오는 메서드 (페이징 추가)
private Page<MovieDto> getMoviesByGenres(List<GenreDto> genres, Pageable pageable) {
    // 장르 키 목록 추출
    List<String> genreKeys = genres.stream()
                                   .map(GenreDto::getGenreKey)
                                   .toList();

    // 페이징된 결과를 가져오기
    Page<GenreMovie> genreMoviesPage = genreMovieRepository.findByGenre_GenreKeyIn(genreKeys, pageable);

    // 필요한 필드만 포함한 MovieDto 리스트로 변환
    return genreMoviesPage.map(genreMovie -> 
        new MovieDto(
            genreMovie.getMovie().getMovieKey(),
            genreMovie.getMovie().getMovieTitle(),
            genreMovie.getMovie().getMoviePoster()
        )
    );
}

// 영화 검색 메서드 (페이징 적용)
public Map<String, Object> searchMoviesByTitle(MovieDto movieDto, Pageable pageable) {
    // 1. 제목 부분 검색 (대소문자 무시 + 페이징)
    Page<Movie> moviesPage = movieRepository.findByMovieTitleContainingIgnoreCase(movieDto.getMovieTitle(), pageable);

    // 2. 검색된 영화 리스트를 DTO로 변환
    Page<MovieDto> movieDtosPage = moviesPage.map(movie -> 
        new MovieDto(
            movie.getMovieKey(),
            movie.getMovieTitle(),
            movie.getMoviePoster()
        )
    );

    // 3. 결과를 HashMap으로 반환
    Map<String, Object> result = new HashMap<>();
    result.put("movies", movieDtosPage.getContent()); // 현재 페이지의 영화 목록
    result.put("totalPages", movieDtosPage.getTotalPages()); // 총 페이지 수
    result.put("currentPage", movieDtosPage.getNumber()); // 현재 페이지 번호
    result.put("totalElements", movieDtosPage.getTotalElements()); // 총 영화 수
    return result;
}
    
}
