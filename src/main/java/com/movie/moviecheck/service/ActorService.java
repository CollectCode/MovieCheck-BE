package com.movie.moviecheck.service;
import org.springframework.stereotype.Service;

import com.movie.moviecheck.converter.ActorConvertor;
import com.movie.moviecheck.dto.ActorDto;
import com.movie.moviecheck.model.Actor;
import com.movie.moviecheck.repository.ActorRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorRepository actorRepository;
    private final ActorConvertor actorConvertor;

    // 모든 배우 조회
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    // 특정 배우 조회
    public Optional<ActorDto> getActor(ActorDto actorDto) {
        Actor actor = actorRepository.findByActorKey(actorDto.getActorKey());
        if (actor == null) {
            return Optional.empty();
        }
        return Optional.of(actorConvertor.convertToDto(actor));
    }
}
