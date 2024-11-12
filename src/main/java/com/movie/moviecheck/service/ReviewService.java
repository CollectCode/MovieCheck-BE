package com.movie.moviecheck.service;

import org.springframework.stereotype.Service;
import com.movie.moviecheck.repository.ReviewRepository;
import com.movie.moviecheck.model.Review;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    // 리뷰 추가
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    // 리뷰 삭제
    public void deleteReview(Integer reviewKey) {
        reviewRepository.deleteById(reviewKey);
    }

    // 특정 영화의 모든 리뷰 조회
    public List<Review> getReviewsByMovie(String movieKey) {
        return reviewRepository.findByMovie_MovieKey(movieKey);
    }

    // 특정 사용자의 모든 리뷰 조회
    public List<Review> getReviewsByUser(int userKey) {
        return reviewRepository.findByUser_UserKey(userKey);
    }

    // 특정 리뷰 조회
    public Optional<Review> getReviewById(Integer reviewKey) {
        return reviewRepository.findById(reviewKey);
    }
}
