package com.movie.moviecheck.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserLike;
import com.movie.moviecheck.repository.ReviewRepository;
import com.movie.moviecheck.repository.UserLikeRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    
    private final UserLikeRepository userLikeRepository;

    private final UserService userService;
    
    private final ReviewRepository reviewRepository; // 리뷰 리포지토리 주입

    public void addLike(ReviewDto reviewDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        Review review = reviewRepository.findByReviewKey(reviewDto.getReviewKey());
        User user = userService.findByKey(userKey);
    
        // 사용자가 이미 좋아요를 눌렀는지 확인
        if (!userLikeRepository.existsByUser(user)) {
            // 좋아요 추가
            UserLike userLike = new UserLike();
            userLike.setUser(user);
            userLike.setReview(review);
            userLikeRepository.save(userLike);
    
            // 리뷰의 좋아요 수 증가
            review.setReviewLike(review.getReviewLike() + 1);
            reviewRepository.save(review); // 리뷰 업데이트
        } else {
            Optional<UserLike> userLike = userLikeRepository.findByUserAndReview(user, review);
            if (userLike.isPresent()) { // Optional이 비어있지 않은 경우만 삭제 처리
                userLikeRepository.delete(userLike.get()); // UserLike 엔티티 삭제
                
                // 리뷰의 좋아요 수 감소
                review.setReviewLike(review.getReviewLike() - 1);
                reviewRepository.save(review); // 리뷰 업데이트
            }
        }
    }
    
}
