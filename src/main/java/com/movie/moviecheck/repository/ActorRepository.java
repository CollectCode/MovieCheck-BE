package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.Actor;

public interface ActorRepository extends JpaRepository<Actor,String> {
    Actor findByActorKey(String actorKey);
}
