package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;

@Component
public class ReviewConvertor {

    // ReviewDto를 Review로 변환하는 메서드
    public Review convertToEntity(ReviewDto reviewDto, Movie movie) {
        if (reviewDto == null) {
            return null; // null 체크
        }
        return Review.builder()
                   .reviewKey(reviewDto.getReviewKey())
                   .movie(movie)
                   .reviewContent(reviewDto.getReviewContent())
                   .reviewTime(reviewDto.getReviewTime())
                   .reviewGood(reviewDto.getReviewGood())
                   .reviewBad(reviewDto.getReviewBad())
                   .build();
    }

    // Review를 ReviewDto로 변환하는 메서드
    public ReviewDto convertToDto(Review review) {
        if (review == null) {
            return null; // null 체크
        }
        return ReviewDto.builder()
                   .reviewKey(review.getReviewKey())
                   .movieKey(review.getMovie().getMovieKey())
                   .userKey(review.getUser().getUserKey())
                   .reviewContent(review.getReviewContent())
                   .reviewTime(review.getReviewTime())
                   .reviewGood(review.getReviewGood())
                   .reviewBad(review.getReviewBad())
                   .build();
    }
}
