package com.movie.moviecheck.service;
import org.springframework.stereotype.Service;
import com.movie.moviecheck.repository.GenreRepository;
import com.movie.moviecheck.model.Genre;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {


    private final GenreRepository genreRepository; // JPA Repository 사용

    // 장르 추가
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    // 장르 삭제
    public void deleteGenre(String genreKey) {
        genreRepository.deleteById(genreKey);
    }

    // 모든 장르 조회
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    // 특정 장르 조회
    public Optional<Genre> getGenreById(String genreKey) {
        return genreRepository.findById(genreKey);
    }
}

