package com.movie.moviecheck.controller;

import org.springframework.http.HttpStatus;
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
    // /api/actors/detail
    @GetMapping("/detail")
    public ResponseEntity<ActorDto> goToGetActorById(@RequestParam(name="id") String id) {
        ActorDto result = actorService.getActor(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}
