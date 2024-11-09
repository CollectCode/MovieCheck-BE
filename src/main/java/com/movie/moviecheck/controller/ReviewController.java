package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.movie.moviecheck.service.ReviewService;

import lombok.RequiredArgsConstructor;

import com.movie.moviecheck.model.Review;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    
    private final ReviewService reviewService;

    // 리뷰 추가
    ///api/reviews/create
    @PostMapping("/create")
    public ResponseEntity<Review> addReview(@RequestBody Review review) {
        Review savedReview = reviewService.addReview(review);
        return ResponseEntity.ok(savedReview);
    }

    // 리뷰 삭제
    // /api/reviews/{reviewKey}
    @DeleteMapping("/{reviewKey}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewKey) {
        reviewService.deleteReview(reviewKey);
        return ResponseEntity.noContent().build();
    }

    // 특정 영화에 대한 모든 리뷰 조회
    // /api/reviews/movie/{movieKey}
    @GetMapping("/movie/{movieKey}")
    public ResponseEntity<List<Review>> getReviewsByMovie(@PathVariable String movieKey) {
        List<Review> reviews = reviewService.getReviewsByMovie(movieKey);
        return ResponseEntity.ok(reviews);
    }

    // 특정 사용자에 대한 모든 리뷰 조회
    // /api/reviews/user/{userKey}
    @GetMapping("/user/{userKey}")
    public ResponseEntity<List<Review>> getReviewsByUser(@PathVariable String userKey) {
        List<Review> reviews = reviewService.getReviewsByUser(userKey);
        return ResponseEntity.ok(reviews);
    }

    // 특정 리뷰 조회
    // /api/reviews/{reviewKey}
    @GetMapping("/{reviewKey}")
    public ResponseEntity<Review> getReviewById(@PathVariable Integer reviewKey) {
        Optional<Review> review = reviewService.getReviewById(reviewKey);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
