package com.movie.moviecheck.service;
import org.springframework.stereotype.Service;
import com.movie.moviecheck.model.Actor;
import com.movie.moviecheck.repository.ActorRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository; // JPA Repository 사용
    // 모든 배우 조회
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    // 특정 배우 조회
    public Optional<Actor> getActorById(String actorKey) {
        return actorRepository.findById(actorKey);
    }
}
