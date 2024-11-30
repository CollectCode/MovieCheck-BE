package com.movie.moviecheck.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.model.Comment;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.MovieRepository;
import com.movie.moviecheck.service.CommentService;
import com.movie.moviecheck.service.UserService;
import com.movie.moviecheck.dto.CommentDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReviewConvertor {

    private final MovieRepository movieRepository;
    private final UserService userService;
    private final CommentConvertor commentConvertor;
    private final CommentService commentService;
    
    // ReviewDto를 Review로 변환하는 메서드
    public Review convertToEntity(ReviewDto reviewDto) {
        // null 체크
        if (reviewDto == null) {
            return null;
        }

        // Movie, User 엔티티 조회
        Movie movie = movieRepository.findByMovieKey(reviewDto.getMovieKey());
        User user = userService.findByKey(reviewDto.getUserKey());

        // Review 엔티티 생성 및 반환
        return Review.builder()
                .reviewKey(reviewDto.getReviewKey())
                .movie(movie)
                .user(user)
                .reviewContent(reviewDto.getReviewContent())
                .reviewTime(reviewDto.getReviewTime())
                .reviewLike(reviewDto.getReviewLike())
                // Review와 연관된 Comment 리스트 추가
                .comments(commentService.getCommentsByReviewKey(reviewDto.getReviewKey())) // Comment를 그대로 사용
                .build();
    }

    // Review를 ReviewDto로 변환하는 메서드
    public ReviewDto convertToDto(Review review) {
        if (review == null) {
            return null; // null 체크
        }

        // Comment 리스트를 CommentDto 리스트로 변환
        List<CommentDto> commentDtos = review.getComments().stream()
                .map(commentConvertor::convertToDto) // Comment를 CommentDto로 변환
                .collect(Collectors.toList());

        // ReviewDto 생성 및 반환
        return ReviewDto.builder()
                .reviewKey(review.getReviewKey())
                .userKey(review.getUser().getUserKey())
                .reviewContent(review.getReviewContent())
                .reviewTime(review.getReviewTime())
                .reviewLike(review.getReviewLike())
                .commentDto(commentDtos) // 변환된 CommentDto 리스트 설정
                .build();
    }

}
