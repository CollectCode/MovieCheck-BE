package com.movie.moviecheck.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie.moviecheck.dto.ActorDto;
import com.movie.moviecheck.service.ActorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    // 특정 배우 조회
    // /api/actors/{actorKey}
    @PostMapping("/detail")
    public ResponseEntity<ActorDto> goToGetActorById(@RequestBody ActorDto actorDto) {
        Optional<ActorDto> actorDtoResult = actorService.getActor(actorDto);
        return actorDtoResult
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
