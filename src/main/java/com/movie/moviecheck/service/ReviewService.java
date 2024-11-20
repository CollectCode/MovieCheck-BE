package com.movie.moviecheck.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.movie.moviecheck.converter.ReviewConvertor;
import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.repository.ReviewRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConvertor reviewConvertor;


    // 추후 수정
    // 리뷰 추가
    // public ReviewDto addReview(ReviewDto reviewDto,Movie movie,HttpServletRequest request) {
    //     HttpSession session = request.getSession(false);
    //     Integer userKey = (Integer) session.getAttribute("userKey")
    //     Review review = reviewConvertor.convertToEntity(reviewDto,movie);
    //     Review saveReview = reviewRepository.save(review);
    //     saveReview = reviewConvertor.convertToDto(saveReview,movie);
    //     return saveReview;
    // }

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
