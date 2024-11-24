package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.embedded.ReviewId;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.MovieRepository;
import com.movie.moviecheck.service.GenreService;
import com.movie.moviecheck.service.MovieService;
import com.movie.moviecheck.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewConvertor {

    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final UserService userService;

    // ReviewDto를 Review로 변환하는 메서드
    public Review convertToEntity(ReviewDto reviewDto) {
        // null 체크
        if (reviewDto == null) {
            return null;
        }
    
        // Movie, User 엔티티 조회
        Movie movie = movieRepository.findByMovieKey(reviewDto.getMovieKey());
        User user = userService.findByKey(reviewDto.getUserKey());
    
        // ReviewId 생성
        ReviewId reviewId = new ReviewId(reviewDto.getMovieKey(), reviewDto.getUserKey());
    
        // Review 엔티티 생성 및 반환
        return Review.builder()
                .reviewKey(reviewDto.getReviewKey())
                .movie(movie)
                .user(user)
                .reviewContent(reviewDto.getReviewContent())
                .reviewTime(reviewDto.getReviewTime())
                .reviewLike(reviewDto.getReviewLike())
                .build();
    }

    // Review를 ReviewDto로 변환하는 메서드
    public ReviewDto convertToDto(Review review) {
        if (review == null) {
            return null; // null 체크
        }
        return ReviewDto.builder()
                   .reviewKey(review.getReviewKey())
                   .userKey(review.getUser().getUserKey())
                   .reviewContent(review.getReviewContent())
                   .reviewTime(review.getReviewTime())
                   .reviewLike(review.getReviewLike())
                   .build();
    }
}
