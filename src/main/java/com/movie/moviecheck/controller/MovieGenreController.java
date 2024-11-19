package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.model.MovieGenre;
import com.movie.moviecheck.service.MovieGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-genre")
@RequiredArgsConstructor
public class MovieGenreController {

    private final MovieGenreService movieGenreService;

    // api 나중에 수정
    // 1. 특정 장르별 영화 가져오기
    @GetMapping("/by-genre")
    public List<MovieGenre> getMoviesByGenreKey(@RequestBody GenreDto genreDto) {
        return movieGenreService.getMoviesByGenreKey(genreDto.getGenreKey());
    }

    // api 나중에 수정
    // 2. 사용자가 선호하는 장르의 영화를 가져오기
    @PostMapping("/by-preferred-genres")
    public List<MovieGenre> getMoviesByPreferredGenres(@RequestBody List<String> preferredGenreKeys) {
        return movieGenreService.getMoviesByPreferredGenres(preferredGenreKeys);
    }
}
