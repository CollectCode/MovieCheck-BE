package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.movie.moviecheck.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.model.Movie;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 추가
    @PostMapping("/create")
    public ResponseEntity<ReviewDto> goToAddReview(@RequestBody ReviewDto reviewDto,Movie movie, HttpServletRequest request) {
        return reviewService.addReview(reviewDto,request);
    }
    
    // 리뷰 삭제
    // /api/reviews/delete
    @DeleteMapping("/delete")
    public ResponseEntity<ReviewDto> deleteReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request) {
        return reviewService.deleteReview(reviewDto,request);
    }

}
