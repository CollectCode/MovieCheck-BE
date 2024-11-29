package com.movie.moviecheck.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserLike;
import com.movie.moviecheck.repository.ReviewRepository;
import com.movie.moviecheck.repository.UserLikeRepository;
import com.movie.moviecheck.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserLikeService {

    
    private final UserLikeRepository userLikeRepository;

    private final UserService userService;
    
    private final ReviewRepository reviewRepository;

    private final ReviewService reviewService;

    private final UserRepository userRepository;

    public void addLike(Integer reviewKey, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        Review review = reviewRepository.findByReviewKey(reviewKey);
        User user = userService.findByKey(userKey);
    
        // 사용자가 해당 리뷰에 이미 좋아요를 눌렀는지 확인
        if (!userLikeRepository.existsByUser(user)) {
            // 좋아요 추가
            UserLike userLike = new UserLike();
            userLike.setUser(user);
            userLike.setReview(review);
            userLikeRepository.save(userLike);
    
            // 리뷰의 좋아요 수 증가
            review.setReviewLike(review.getReviewLike() != null ? review.getReviewLike() + 1 : 1);
    
            // 작성자 등급 업데이트
            updateUserGrade(review);
    
            reviewRepository.save(review); // 리뷰 업데이트
        } else {
            // 좋아요 삭제
            Optional<UserLike> userLike = userLikeRepository.findByUserAndReview(user, review);
            if (userLike.isPresent()) {
                userLikeRepository.delete(userLike.get());
    
                // 리뷰의 좋아요 수 감소
                review.setReviewLike(review.getReviewLike() > 0 ? review.getReviewLike() - 1 : 0);
    
                // 작성자 등급 업데이트
                updateUserGrade(review);
    
                reviewRepository.save(review); // 리뷰 업데이트
            }
        }
    }
    
    // 등급 계산 및 업데이트 메서드
    public void updateUserGrade(Review review) {
        User reviewUser = review.getUser();
        int totalLikes = 0;
    
        // 리뷰 목록을 순회하며 좋아요 수 합산
        for (Review r : reviewRepository.findByUser_UserKey(reviewUser.getUserKey())) {
            totalLikes += (r.getReviewLike() != null) ? r.getReviewLike() : 0;
        }
    
        String newGrade = reviewService.calculateUserGrade(totalLikes);
        reviewUser.setUserGrade(newGrade);
        userRepository.save(reviewUser);
    }
}
