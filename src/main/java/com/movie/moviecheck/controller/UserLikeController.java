package com.movie.moviecheck.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.service.UserLikeService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.UserLike;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class UserLikeController {
    
    private final UserLikeService userLikeService;

    @GetMapping
    public ResponseEntity<ReviewDto> goToaddLike(@RequestParam(name="reviewKey") Integer reviewKey, HttpServletRequest request) {
        return userLikeService.addLike(reviewKey, request);    
    }

    @GetMapping("/button")
    public ResponseEntity<List<ReviewDto>> goToGetLikeInfo(HttpServletRequest request)    {
        return userLikeService.getLikeInfo(request);
    }
}
