package com.movie.moviecheck.service;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.repository.GenreRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

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
    public Genre getGenreById(String genreKey) {
        Optional<Genre> og = genreRepository.findById(genreKey);
        Genre genre;
        if(og.isPresent())   {
            genre = og.get();
            return genre;
        }
        return og.orElseThrow(() -> new EntityNotFoundException("장르를 찾을 수 없습니다: " + genreKey));
    }

    public Optional<Genre> getGenreByName(String genrename) {
        return genreRepository.findByGenreName(genrename);
    }
}

