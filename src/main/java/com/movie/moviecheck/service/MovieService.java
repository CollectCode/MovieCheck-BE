package com.movie.moviecheck.service;

import com.movie.moviecheck.converter.GenreConvertor;
import com.movie.moviecheck.converter.MovieConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.ActorDto;
import com.movie.moviecheck.dto.DirectorDto;
import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.Actor;
import com.movie.moviecheck.model.Director;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.GenreMovie;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.UserGenre;
import com.movie.moviecheck.repository.GenreMovieRepository;
import com.movie.moviecheck.repository.MovieRepository;
import com.movie.moviecheck.repository.UserGenreRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final GenreMovieService genreMovieService;
    private final GenreService genreService;

    public Map<String, Object> getAllMovies(Pageable pageable) {
        // 1. 모든 영화 가져오기
        Page<Movie> movies = movieRepository.findAll(pageable); // TODO DFSDFDSFDSSDADASDASD
        // 2. 필요한 데이터만 포함하는 DTO로 변환
        List<Map<String, Object>> movieDtos = movies.stream()
                                                    .map(movie -> {    
                                                        Map<String, Object> dto = new HashMap<>();
                                                        dto.put("movieKey", movie.getMovieKey());
                                                        dto.put("movieTitle", movie.getMovieTitle());
                                                        String moviePoster = movie.getMoviePoster();
                                                        if (moviePoster.equals("")) {
                                                            moviePoster = "http://localhost:8080/images/movies/default.png";}
                                                        dto.put("moviePoster", moviePoster);
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
                    String actorImage = actor.getActorImage();
                    if(actorImage.equals("")){
                        actorImage = "http://localhost:8080/images/actors/default.png";
                    }
                    return new ActorDto(
                            actor.getActorKey(),
                            actor.getActorName(),
                            actorImage
                    );
                })
                .collect(Collectors.toList());

        // 해당 영화의 리뷰 가져오기    
        List<ReviewDto> reviews = movie.getReview().stream()
                    .map(review -> {
                        return new ReviewDto(
                                review.getReviewKey(),
                                review.getUser().getUserKey(),
                                review.getReviewContent(),
                                review.getReviewTime(),
                                review.getReviewLike()
                        );
                    })
                    .toList();

        // 감독 정보 가져오기
        Director director = movie.getDirector();
        DirectorDto directorDto = null;
        if (director != null) {
            String directorImage = director.getDirectorImage();
        if(directorImage.equals("")){
            directorImage = "http://localhost:8080/images/directors/default.png";}
            directorDto = new DirectorDto(
                    director.getDirectorName(),
                    directorImage
            );
        }

        // 영화 장르 가져오기
        List<GenreMovie> genreMovies = genreMovieRepository.findByMovie_MovieKey(movieKey);
        List<String> genreKeys = genreMovies.stream()
                .map(genreMovie -> genreMovie.getGenre().getGenreName()) // 필요한 정보로 변경
                .toList();

        // DTO에 데이터 설정
        movieDto.setActorDto(actors); // 배우 추가
        movieDto.setReviewDto(reviews); // 리뷰 추가
        movieDto.setDirectorDto(directorDto); // 감독 추가
        movieDto.setGenresName(genreKeys); // 장르 추가

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
    public Map<String, Object> getMoviesByUserPreferences(HttpServletRequest request,int page, int size) {
        // 1. 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");

        // 2. 유저 정보를 가져오기
        UserDto user = userConvertor.convertToDto(userService.findByKey(userKey));

        // 3. 유저의 선호 장르 가져오기
        List<GenreDto> genres = getUserGenres(user);

        // 4. 선호 장르에 해당하는 영화 가져오기 (페이징 적용)
        Page<MovieDto> moviePage = getMoviesByGenres(genres, page,size);

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

    // 여러 개의 장르 영화 가져오는 메서드 (페이징 추가)
    public Page<MovieDto> getMoviesByGenres(List<GenreDto> genres, int page, int size) {
        // 장르 키 목록 추출
        List<String> genreKeys = genres.stream()
                                    .map(GenreDto::getGenreKey)
                                    .toList();

        // 결과를 가져오기
        List<GenreMovie> genreMoviesPage = genreMovieRepository.findByGenre_GenreKeyIn(genreKeys);
        List<MovieDto> movieDtos = new ArrayList<>();

        for(GenreMovie genreMovie : genreMoviesPage)    {
            Movie movie = genreMovie.getMovie();
            MovieDto movieDto = movieConvertor.convertToDto(movie);
            movieDtos.add(movieDto);
        }

        int totalSize = movieDtos.size();
        int start = Math.min(page*size, totalSize);
        int end = Math.min(start + size, totalSize);

        List<MovieDto> pagedList = movieDtos.subList(start, end);
        Pageable pageables = PageRequest.of(page, size);
        Page<MovieDto> pageable = new PageImpl<>(pagedList, pageables, totalSize);

        return pageable;
    }


    // 한 장르 영화 가져오는 메서드 (페이징 추가)
    public Map<String, Object> getMoviesByGenre(String genre, int page, int size) {
        // 장르 키 목록 추출
        Genre getGenre = null;
        // 페이징된 결과를 가져오기
        Optional<Genre> og = genreService.getGenreByName(genre);
        if(og.isPresent())  {
            getGenre = og.get();
        }
        List<GenreMovie> genreMovies = genreMovieService.getMoviesByGenreKey(getGenre.getGenreKey());
        List<MovieDto> movieDtos = new ArrayList<>();

        for(GenreMovie genreMovie : genreMovies)    {
            Movie movie = genreMovie.getMovie();
            MovieDto movieDto = movieConvertor.convertToDto(movie);
            movieDtos.add(movieDto);
        }
        
        // 필요한 필드만 포함한 MovieDto 리스트로 변환
        int totalSize = movieDtos.size();
        int start = Math.min(page*size, totalSize);
        int end = Math.min(start+size, totalSize);

        List<MovieDto> pagedList = movieDtos.subList(start, end);
        Pageable pageables = PageRequest.of(page, size);
        Page<MovieDto> pageable = new PageImpl<>(pagedList, pageables, totalSize);

        Map<String, Object> result = new HashMap<>();
        result.put("movies", pageable.getContent()); // 현재 페이지의 영화 목록
        result.put("totalPages", pageable.getTotalPages()); // 총 페이지 수
        result.put("currentPage", pageable.getNumber()); // 현재 페이지 번호
        result.put("totalElements", pageable.getTotalElements()); // 총 영화 수

        return result;
    }

    // 영화 검색 메서드 (페이징 적용)
    public Map<String, Object> searchMoviesByTitle(String word, Pageable pageable) {
        // 1. 제목 부분 검색 (대소문자 무시 + 페이징)
        Page<Movie> moviesPage = movieRepository.findByMovieTitleContainingIgnoreCase(word, pageable);

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
