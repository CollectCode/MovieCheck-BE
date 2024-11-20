package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.MovieDto; // DTO 클래스
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.service.MovieService; // 서비스 클래스 필요
import com.movie.moviecheck.converter.MovieConvertor;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    // 영화 정보 가져오기
    @GetMapping
    public List<MovieDto> goTogetAllMovies() {
        return movieService.getAllMovies();  // 이미 MovieDto 리스트가 반환되는 경우
    }

    // 특정 영화 조회
    // /api/movies/detail
    @GetMapping("/detail/{movieKey}")
    public MovieDto goTogetMovieDetails(@RequestBody MovieDto movieDto) {
            return movieService.getMovieDetails(movieDto);
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
