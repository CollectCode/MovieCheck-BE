package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.MovieDto; // DTO 클래스
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.service.MovieService; // 서비스 클래스 필요
import com.movie.moviecheck.converter.MovieConvertor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService; // MovieService 주입
    private final MovieConvertor movieConvertor;

    // 모든 영화 목록 조회
    // /api/movies
    @GetMapping
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies().stream()
                .map(movie -> movieConvertor.convertToDto(movie)) // 람다 표현식 사용
                .collect(Collectors.toList());
    }
    // 특정 영화 조회
    // /api/movies/search
    @GetMapping("/search")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable String movieKey) {
        Movie movie = movieService.getMovieByMovieKey(movieKey);
        if (movie != null) {
            return ResponseEntity.ok(movieConvertor.convertToDto(movie)); // Movie를 MovieDto로 변환
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 영화 추가
    // /api/movies/create
    @PostMapping("/create")
    public MovieDto goTocreateMovie(@RequestBody MovieDto movieDto) {
        return movieService.createMovie(movieDto);
    }
    
    // 영화 삭제
    // /api/movies/{id}
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMovie(@RequestBody MovieDto movieDto) {
        if (movieService.deleteMovie(movieDto.getMovieKey())) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
