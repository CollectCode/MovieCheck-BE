package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.MovieDto; // DTO 클래스
import com.movie.moviecheck.service.MovieService; // 서비스 클래스 필요

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService; // MovieService 주입

    // 영화 정보 가져오기
    @GetMapping
    public ResponseEntity<Map<String, Object>> goTogetAllMovies(@RequestParam(name="page") int page,
                                                                @RequestParam(name="size") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"movieScore"));
        Map<String, Object> result = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(result);
    }

    // 특정 영화 조회
    // /api/movies/detail
    @GetMapping("/detail")
    public ResponseEntity<MovieDto> goTogetMovieDetails(@RequestParam(name="id") String movieId) {
        return movieService.getMovieDetails(movieId);
    }

    // 사용자가 선호하는 장르 영화 가져오기
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getMoviesByUserPreferences(HttpServletRequest request,
    @RequestParam(name = "size")int size,@RequestParam(name="page") int page) {
        Map<String, Object> response = movieService.getMoviesByUserPreferences(request, page,size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMovies(@RequestParam(name="size") int size, @RequestParam(name="page") int page, @RequestParam(name = "keyword") String word) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "movieScore"));
        Map<String, Object> response = movieService.searchMoviesByTitle(word, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 장르별 영화 가져오기
    @GetMapping("/genre")
    public ResponseEntity<Map<String, Object>> goTogetMoviesByGenres(@RequestParam(name="size") int size, @RequestParam(name="page") int page, @RequestParam(name="genre") String genre) {
        Map<String, Object> response = movieService.getMoviesByGenre(genre, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
