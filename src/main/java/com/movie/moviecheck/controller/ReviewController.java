package com.movie.moviecheck.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.movie.moviecheck.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import com.movie.moviecheck.dto.ReviewDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    
    // 리뷰 조회 및 출력
    @GetMapping
    public ResponseEntity<Map<String, Object>> goToGetReviewsByMovie(@RequestParam(name="id") String movieId)    {
        return reviewService.getReviewsByMovie(movieId);
    }

    // 리뷰 추가 및 갱신
    @PostMapping("/create")
    public ResponseEntity<ReviewDto> goToAddReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request) {
        return reviewService.addReview(reviewDto, request);
    }
    
    // 리뷰 삭제
    // /api/reviews/delete
    @DeleteMapping("/delete")
    public ResponseEntity<ReviewDto> deleteReview(@RequestParam(name="reviewKey") Integer reviewKey , HttpServletRequest request) {
        return reviewService.deleteReview(reviewKey,request);
    }

}
