package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.MovieDto; // DTO 클래스
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.service.MovieService; // 서비스 클래스 필요
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService; // MovieService 주입

    // 모든 영화 목록 조회
    // /api/movies
    @GetMapping
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies().stream()
                .map(this::convertToDto) // Movie를 MovieDto로 변환
                .collect(Collectors.toList());
    }

    // 특정 영화 조회
    // /api/movies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable String id) {
        Movie movie = movieService.getMovieById(id);
        if (movie != null) {
            return ResponseEntity.ok(convertToDto(movie)); // Movie를 MovieDto로 변환
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 영화 추가
    // /api/movies/create
    @PostMapping("/create")
    public MovieDto createMovie(@RequestBody MovieDto movieDto) {
        Movie movie = convertToEntity(movieDto); // MovieDto를 Movie로 변환
        Movie savedMovie = movieService.createMovie(movie);
        return convertToDto(savedMovie); // 저장된 Movie를 MovieDto로 변환하여 반환
    }
    
    // 영화 삭제
    // /api/movies/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        if (movieService.deleteMovie(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Movie를 MovieDto로 변환하는 메서드
    private MovieDto convertToDto(Movie movie) {
        return new MovieDto(
            movie.getMovieKey(),
            movie.getMovieTitle(),
            movie.getMovieOverview(),
            movie.getMoviePoster(),
            movie.getMovieScore(),
            movie.getMovieDirector()
        );
    }

    // MovieDto를 Movie로 변환하는 메서드
    private Movie convertToEntity(MovieDto movieDto) {
        return new Movie(
            movieDto.getMovieKey(),
            movieDto.getMovieTitle(),
            movieDto.getMovieOverview(),
            movieDto.getMoviePoster(),
            movieDto.getMovieScore(),
            movieDto.getMovieDirector()
        );
    }
}
