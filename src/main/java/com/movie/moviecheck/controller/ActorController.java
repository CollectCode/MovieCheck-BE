package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.movie.moviecheck.service.ActorService;
import com.movie.moviecheck.model.Actor;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    // 모든 배우 조회
    // /api/actors
    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors() {
        List<Actor> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);
    }

    // 특정 배우 조회
    // /api/actors/{actorKey}
    @GetMapping("/{actorKey}")
    public ResponseEntity<Actor> getActorById(@PathVariable String actorKey) {
        Optional<Actor> actor = actorService.getActorById(actorKey);
        return actor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
