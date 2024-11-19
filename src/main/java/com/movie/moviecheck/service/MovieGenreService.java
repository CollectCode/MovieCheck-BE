package com.movie.moviecheck.service;

import com.movie.moviecheck.model.MovieGenre;
import com.movie.moviecheck.repository.MovieGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieGenreService {

    private final MovieGenreRepository movieGenreRepository;

    // 1. 특정 장르별 영화 가져오기
    public List<MovieGenre> getMoviesByGenreKey(String genreKey) {
        return movieGenreRepository.findAll().stream()
                .filter(movieGenre -> movieGenre.getGenre().getGenreKey().equals(genreKey))
                .collect(Collectors.toList());
    }

    // 2. 사용자가 선호하는 장르의 영화를 메인 화면에 우선 표시
    public List<MovieGenre> getMoviesByPreferredGenres(List<String> preferredGenreKeys) {
        // 모든 영화-장르 매핑 조회
        List<MovieGenre> allMovies = movieGenreRepository.findAll();

        // 선호 장르 우선 필터링
        List<MovieGenre> preferredMovies = allMovies.stream()
                .filter(movieGenre -> preferredGenreKeys.contains(movieGenre.getGenre().getGenreKey()))
                .collect(Collectors.toList());

        // 결과 반환 (선호 장르 영화가 먼저 오도록 정렬)
        return preferredMovies;
    }
}
