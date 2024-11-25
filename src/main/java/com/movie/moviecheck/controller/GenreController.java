package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.service.GenreService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    // 사용 안함
    private final GenreService genreService;
    
    // 모든 장르 조회
    // /api/genres
    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    // 특정 장르 조회
    // /api/genres/{genreKey}
    // @GetMapping("/{genreKey}")
    // public ResponseEntity<Genre> getGenreById(@PathVariable String genreKey) {
    //     Genre genre = genreService.getGenreById(genreKey);
    //     return genre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    // }
}
