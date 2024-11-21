package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.embedded.MovieActorId;
import com.movie.moviecheck.model.MovieActor;

public interface MovieActorRepository extends JpaRepository<MovieActor,MovieActorId> {
    
}
