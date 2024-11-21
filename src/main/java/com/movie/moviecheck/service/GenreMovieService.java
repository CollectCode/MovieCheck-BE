package com.movie.moviecheck.service;

import com.movie.moviecheck.model.GenreMovie;
import com.movie.moviecheck.repository.GenreMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;
@Service
@Transactional
@RequiredArgsConstructor
public class GenreMovieService {

    private final GenreMovieRepository genreMovieRepository;

    // 1. 특정 장르별 영화 가져오기
    public List<GenreMovie> getMoviesByGenreKey(String genreKey) {
        return genreMovieRepository.findByGenre_GenreKey(genreKey);
    }

    // 2. 사용자가 선호하는 장르의 영화를 메인 화면에 우선 표시
    public List<GenreMovie> getMoviesByPreferredGenres(List<String> preferredGenreKeys) {
        if (preferredGenreKeys == null || preferredGenreKeys.isEmpty()) {
            return Collections.emptyList();
        }

        List<GenreMovie> preferredMovies = genreMovieRepository.findByGenre_GenreKeyIn(preferredGenreKeys);

        // 선호 장르 키의 순서대로 정렬
        preferredMovies.sort(Comparator.comparing(
            movieGenre -> preferredGenreKeys.indexOf(movieGenre.getGenre().getGenreKey())
        ));

        return preferredMovies;
    }
}
