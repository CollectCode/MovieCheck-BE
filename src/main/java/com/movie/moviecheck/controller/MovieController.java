package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.MovieDto; // DTO 클래스
import com.movie.moviecheck.service.MovieService; // 서비스 클래스 필요

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService; // MovieService 주입

    // 영화 정보 가져오기
    @GetMapping
    public List<MovieDto> goTogetAllMovies() {
        return movieService.getAllMovies();  // 이미 MovieDto 리스트가 반환되는 경우
    }

    // 특정 영화 조회
    // /api/movies/detail
    @PostMapping("/detail")
    public ResponseEntity<MovieDto> goTogetMovieDetails(@RequestBody MovieDto movieDto) {
        return movieService.getMovieDetails(movieDto);
    }
}
