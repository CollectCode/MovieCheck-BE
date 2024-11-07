package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movie.moviecheck.model.Movie;

public interface MovieRepository extends JpaRepository<Movie,String>  {

}
