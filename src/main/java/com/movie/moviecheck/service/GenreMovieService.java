package com.movie.moviecheck.service;

import com.movie.moviecheck.controller.WrapperClass;
import com.movie.moviecheck.dto.GenreMovieDto;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.GenreMovie;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.repository.GenreMovieRepository;
import com.movie.moviecheck.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;
@Service
@Transactional
@RequiredArgsConstructor
public class GenreMovieService {

    private final GenreMovieRepository genreMovieRepository;

    private final GenreRepository genreRepository;

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

    // 해당하는 장르의 영화 가져오기
    public ResponseEntity<WrapperClass<List<MovieDto>>> getMovie(GenreMovieDto genreMovieDto) {
        String genreKey = genreMovieDto.getGenreKey();
    
        // 유효성 검사: genreKey가 null이거나 비어 있는 경우 처리
        if (genreKey == null || genreKey.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new WrapperClass<>(null, "Invalid genre key."));
        }
    
        // Genre 존재 여부 확인
        Optional<Genre> genreOptional = genreRepository.findByGenreKey(genreKey);
        if (genreOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new WrapperClass<>(null, "Genre not found for the provided key."));
        }
    
        // Genre 엔티티에서 key를 추출
        Genre genre = genreOptional.get();
        String resolvedGenreKey = genre.getGenreKey(); // 엔티티에서 genreKey 가져오기
    
        // GenreMovieRepository를 사용하여 영화 목록 가져오기
        List<GenreMovie> genreMovies = genreMovieRepository.findByGenre_GenreKey(resolvedGenreKey);
        if (genreMovies.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new WrapperClass<>(Collections.emptyList(), "No movies found for the given genre."));
        }
    
        // Movie 데이터를 DTO로 변환
        List<MovieDto> movieDTOs = new ArrayList<>();
        for (GenreMovie genreMovie : genreMovies) {
            Movie movie = genreMovie.getMovie();
            if (movie != null) {
                movieDTOs.add(new MovieDto(
                        movie.getMovieKey(),
                        movie.getMovieTitle(),
                        movie.getMoviePoster()
                ));
            }
        }
    
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new WrapperClass<>(movieDTOs, "Movies fetched successfully."));
    }
    
    



}
