package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.MovieDto; // DTO 클래스
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.service.MovieService; // 서비스 클래스 필요

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService; // MovieService 주입

    // 영화 정보 가져오기
    @GetMapping
    public ResponseEntity<Map<String, Object>> goTogetAllMovies() {
        Map<String, Object> result = movieService.getAllMovies();
        return ResponseEntity.ok(result);
    }

    // 특정 영화 조회
    // /api/movies/detail
    @PostMapping("/detail")
    public ResponseEntity<MovieDto> goTogetMovieDetails(@RequestBody MovieDto movieDto) {
        return movieService.getMovieDetails(movieDto);
    }

    // 사용자가 선호하는 장르 영화 가져오기
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getMoviesByUserPreferences(HttpServletRequest request) {
        Map<String, Object> response = movieService.getMoviesByUserPreferences(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMovies(@RequestBody MovieDto movieDto) {
        Map<String, Object> response = movieService.searchMoviesByTitle(movieDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
