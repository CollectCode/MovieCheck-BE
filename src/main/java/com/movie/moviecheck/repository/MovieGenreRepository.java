package com.movie.moviecheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.movie.moviecheck.embedded.MovieGenreId;
import com.movie.moviecheck.model.MovieGenre;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {

    // 특정 영화에 속한 장르 가져오기
    List<MovieGenre> findByMovie_MovieKey(String movieKey);

    // ??? 나중에 수정
    // 특정 장르에 속한 영화 가져오기
    @Query("SELECT mg FROM MovieGenre mg WHERE mg.genre.genreKey = :genreKey")
    List<MovieGenre> findByGenreKey(@Param("genreKey") String genreKey);

    // ??? 나중에 수정
    // 특정 장르 목록에 속한 영화 가져오기
    @Query("SELECT mg FROM MovieGenre mg WHERE mg.genre.genreKey IN :genreKeys")
    List<MovieGenre> findByGenreKeys(@Param("genreKeys") List<String> genreKeys);
}
