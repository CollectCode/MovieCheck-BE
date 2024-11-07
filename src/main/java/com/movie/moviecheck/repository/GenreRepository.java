package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.Genre;

public interface GenreRepository extends JpaRepository<Genre,String> {

}
