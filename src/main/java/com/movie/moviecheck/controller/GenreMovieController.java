package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.model.GenreMovie;
import com.movie.moviecheck.service.GenreMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre_movie")
@RequiredArgsConstructor
public class GenreMovieController {

    private final GenreMovieService movieGenreService;

    // ?? 이게 필요한가
    // api 나중에 수정
    // 1. 특정 장르별 영화 가져오기
    @GetMapping
    public List<GenreMovie> goTogetMoviesByGenreKey(@RequestBody GenreDto genreDto) {
        return movieGenreService.getMoviesByGenreKey(genreDto.getGenreKey());
    }

}
