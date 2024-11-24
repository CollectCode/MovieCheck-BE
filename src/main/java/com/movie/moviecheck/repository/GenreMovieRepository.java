package com.movie.moviecheck.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.embedded.GenreMovieId;
import com.movie.moviecheck.model.GenreMovie;

public interface GenreMovieRepository extends JpaRepository<GenreMovie, GenreMovieId> {

    // 특정 영화에 속한 장르 가져오기
    List<GenreMovie> findByMovie_MovieKey(String movieKey);
    
    // Genre_Key 가져오기
    List<GenreMovie> findByGenre_GenreKey(String genreKey);
    
    // 여려 Genre_Key 가져오기
    List<GenreMovie> findByGenre_GenreKeyIn(List<String> genreKeys);

    // 페이징 가능한 여러 장르의 영화
    Page<GenreMovie> findByGenre_GenreKeyIn(List<String> genreKeys, Pageable pageable);

    // 하나의 장르 영화
    Page<GenreMovie> findByGenre_GenreKey(String genreKey, Pageable pageable);
}
