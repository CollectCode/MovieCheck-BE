package com.movie.moviecheck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.Actor;

public interface ActorRepository extends JpaRepository<Actor,String> {
    Optional<Actor> findByActorKey(String actorKey);
}
