package com.movie.moviecheck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.Genre;

public interface GenreRepository extends JpaRepository<Genre,String> {
    Optional<Genre> findByGenreName(String genreName);


    Optional<Genre> findByGenreKey(String genreKey);
}
