package com.movie.moviecheck.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.movie.moviecheck.converter.ReviewConvertor;
import com.movie.moviecheck.dto.ReviewDto;
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
    public ResponseEntity<ReviewDto> addReview(ReviewDto reviewDto, HttpServletRequest request) {
        // 1. 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userKey") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(null); // 로그인 필요 응답
        }
        Integer userKey = (Integer) session.getAttribute("userKey");
        // 2. ReviewDto에 userKey 설정
        reviewDto.setUserKey(userKey);
        reviewDto.setReviewTime(LocalDateTime.now());
        reviewDto.setReviewLike(0);
        // 3. Review 엔티티 변환 및 저장
        Review review = reviewConvertor.convertToEntity(reviewDto);
        Review savedReview = reviewRepository.save(review);
        // 4. 저장된 Review 엔티티를 ReviewDto로 변환하여 반환
        ReviewDto responseDto = reviewConvertor.convertToDto(savedReview);
        // 5. ResponseEntity로 반환
        return ResponseEntity.ok(responseDto);
    }


    // 리뷰 삭제
    public ResponseEntity<Void> deleteReview(HttpServletRequest request, ReviewDto reviewDto) {
        // 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
        if (session == null || session.getAttribute("userKey") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 응답
        }
        Integer userKey = (Integer) session.getAttribute("userKey");
        // 리뷰 삭제 로직
        Review review = reviewRepository.findByUser_userKeyAndMovie_MovieKey(userKey, reviewDto.getMovieKey());
        // 리뷰가 없으면 404 응답
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 리뷰 없음
        }
        // 리뷰 작성자가 현재 로그인한 사용자와 일치하는지 확인
        if (!review.getUser().getUserKey().equals(userKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 삭제 권한 없음
        }
        try {
            // 리뷰 삭제
            reviewRepository.delete(review);
            return ResponseEntity.noContent().build(); // 성공적으로 삭제
        } catch (Exception e) {
            // 예외 발생 시 500 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    


    // 특정 영화의 모든 리뷰 조회
    public List<ReviewDto> getReviewsByMovie(ReviewDto reviewDto) {
        List<Review> reviews = reviewRepository.findByMovie_MovieKey(reviewDto.getMovieKey());
        return reviews.stream()
                  .map(reviewConvertor::convertToDto)
                  .toList();
    }

    // 특정 사용자의 모든 리뷰 조회
    public List<Review> getReviewsByUser(int userKey) {
        return reviewRepository.findByUser_UserKey(userKey);
    }

    // 특정 리뷰 조회
    public Review getReviewByKey(Integer reviewKey) {
        return reviewRepository.findByReviewKey(reviewKey);
    }
}
