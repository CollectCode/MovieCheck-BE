package com.movie.moviecheck.service;

import com.movie.moviecheck.controller.MovieListWithCount;
import com.movie.moviecheck.controller.WrapperClass;
import com.movie.moviecheck.converter.GenreConvertor;
import com.movie.moviecheck.converter.MovieConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.dto.UserDto;
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

import org.apache.commons.lang3.tuple.Pair;
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

    public Map<String, Object> getAllMovies() {
        // 1. 모든 영화 가져오기
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = movies.stream()
                                         .map(movieConvertor::convertToDto)
                                         .collect(Collectors.toList());
    
        // 2. 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("movies", movieDtos);
        response.put("count", movieDtos.size());
    
        return response; // 영화 목록과 영화 수를 포함하는 Map 반환
    }
    
    // 영화 디테일에서 사용
    public MovieDto getMovies(String movieKey){
        Movie movie = movieRepository.findByMovieKey(movieKey);
        return movieConvertor.convertToDto(movie);
    }

    // 영화 디테일 조회
    public ResponseEntity<MovieDto> getMovieDetails(MovieDto movieDto) {
        String movieKey = movieDto.getMovieKey();
        // 1. 요청 검증: movieKey가 없는 경우 400 Bad Request 반환
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
    public Map<String, Object> getMoviesByUserPreferences(HttpServletRequest request) {
        // 1. 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");

        // 2. 유저 정보를 가져오기
        UserDto user = userConvertor.convertToDto(userService.findByKey(userKey));

        // 3. 유저의 선호 장르 가져오기
        List<GenreDto> genres = getUserGenres(user);

        // 4. 선호 장르에 해당하는 영화 가져오기
        MovieListWithCount movieListWithCount = getMoviesByGenres(genres);

        // 5. 결과를 HashMap에 담아서 반환
        Map<String, Object> result = new HashMap<>();
        result.put("movies", movieListWithCount.getMovieList());
        result.put("count", movieListWithCount.getCount());
        return result;
    }

    // 유저의 선호 장르 가져오는 메서드
    private List<GenreDto> getUserGenres(UserDto user) {
        List<UserGenre> userGenres = userGenreRepository.findByUser_UserKey(user.getUserKey());
        return userGenres.stream()
                         .map(userGenre -> genreConvertor.converToDto(userGenre.getGenre()))
                         .toList();
    }

    // 장르별 영화 가져오는 메서드
    private MovieListWithCount getMoviesByGenres(List<GenreDto> genres) {
        List<String> genreKeys = genres.stream()
                                       .map(GenreDto::getGenreKey)
                                       .toList();

        List<GenreMovie> genreMovies = genreMovieRepository.findByGenre_GenreKeyIn(genreKeys);

        Map<Movie, Long> movieToGenreMatchCount = genreMovies.stream()
            .collect(Collectors.groupingBy(GenreMovie::getMovie, Collectors.counting()));

        List<MovieDto> sortedMovies = movieToGenreMatchCount.entrySet().stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
            .map(entry -> movieConvertor.convertToDto(entry.getKey()))
            .toList();

        return new MovieListWithCount(sortedMovies, sortedMovies.size());
    }
    
    // 영화 검색 메서드
    public Map<String, Object> searchMoviesByTitle(MovieDto movieDto) {
        // 1. 제목 부분 검색 (대소문자 무시)
        List<Movie> movies = movieRepository.findByMovieTitleContainingIgnoreCase(movieDto.getMovieTitle());
        
        // 2. 검색된 영화 리스트를 DTO로 변환
        List<MovieDto> movieDtos = movies.stream()
                                         .map(movieConvertor::convertToDto)
                                         .toList();
        
        // 3. 결과를 HashMap으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("movies", movieDtos);
        result.put("count", movieDtos.size());
        return result;
    }
    
}
