package com.movie.moviecheck.controller;

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


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class UserLikeController {
    
    
    private final UserLikeService userLikeService;

    @GetMapping
    public void goToaddLike(@RequestParam(name="reviewKey") Integer reviewKey, HttpServletRequest request) {
        userLikeService.addLike(reviewKey, request);
    }
}
