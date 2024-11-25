package com.movie.moviecheck.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.dto.GenreMovieDto;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.service.GenreMovieService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/genre-movie")
@RequiredArgsConstructor
public class GenreMovieController {

    private final GenreMovieService genreMovieService;

    @GetMapping("/movies")
    public ResponseEntity<WrapperClass<List<MovieDto>>> goToGetLikeMovie(GenreMovieDto genreMovieDto) {
        return genreMovieService.getMovie(genreMovieDto);
    }
}
