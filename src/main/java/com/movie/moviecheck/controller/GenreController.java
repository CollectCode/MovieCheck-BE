package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.service.GenreService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {


    private final GenreService genreService;

    // 장르 추가
    // /api/genres/create
    @PostMapping("/create")
    public ResponseEntity<Genre> addGenre(@RequestBody Genre genre) {
        Genre savedGenre = genreService.addGenre(genre);
        return ResponseEntity.ok(savedGenre);
    }

    // 장르 삭제
    // /api/genres/{genreKey}
    @DeleteMapping("/{genreKey}")
    public ResponseEntity<Void> deleteGenre(@PathVariable String genreKey) {
        genreService.deleteGenre(genreKey);
        return ResponseEntity.noContent().build();
    }

    // 모든 장르 조회
    // /api/genres
    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    // 특정 장르 조회
    // /api/genres/{genreKey}
    @GetMapping("/{genreKey}")
    public ResponseEntity<Genre> getGenreById(@PathVariable String genreKey) {
        Optional<Genre> genre = genreService.getGenreById(genreKey);
        return genre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
