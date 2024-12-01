package com.movie.moviecheck.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.movie.moviecheck.converter.ReviewConvertor;
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

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserLikeRepository userLikeRepository;
    
    private final ReviewRepository reviewRepository;

    private final ReviewService reviewService;

    private final ReviewConvertor reviewConvertor;


    // 리뷰 좋아요 요청이 왔을때 실행되는 메서드
    public ResponseEntity<ReviewDto> addLike(Integer reviewKey, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        Review review = reviewRepository.findByReviewKey(reviewKey);
        ReviewDto reviewDto = null;
        User user = userService.findByKey(userKey);
    
        // 사용자가 해당 리뷰에 좋아요를 눌렀는지 확인
        Optional<UserLike> userLike = userLikeRepository.findByUserAndReview(user, review);
        if (userLike.isPresent()) {
            // 이미 좋아요를 눌렀다면 좋아요 제거
            userLikeRepository.delete(userLike.get());

            // 제거에 따른 리뷰의 좋아요 수 감소
            review.setReviewLike(review.getReviewLike() > 0 ? review.getReviewLike() - 1 : 0);

            // 작성자 등급 업데이트
            updateUserGrade(review);
            reviewRepository.save(review); // 리뷰 업데이트
        } else {
            // 좋아요 추가
            UserLike newUserLike = new UserLike();
            newUserLike.setUser(user);
            newUserLike.setReview(review);
            userLikeRepository.save(newUserLike);

            // 리뷰의 좋아요 수 증가
            review.setReviewLike(review.getReviewLike() != null ? review.getReviewLike() + 1 : 1);

            // 작성자 등급 업데이트
            updateUserGrade(review);

            reviewRepository.save(review); // 리뷰 업데이트
        }
        reviewDto = reviewConvertor.convertToDto(review);

        return ResponseEntity.status(HttpStatus.OK).body(reviewDto);
    }

    // 리뷰 좋아요 조회하는 메서드
    public ResponseEntity<List<ReviewDto>> getLikeInfo(HttpServletRequest request)  {
        HttpSession session = request.getSession();
        Integer userKey = (Integer) session.getAttribute("userKey");
        User user = userService.findByKey(userKey);
        List<UserLike> userLikes = userLikeRepository.findByUser(user);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        
        for(UserLike userLike : userLikes)  {
            Review review = userLike.getReview();
            ReviewDto reviewDto = reviewConvertor.convertToDto(review);
            reviewDtos.add(reviewDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(reviewDtos);
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
        reviewUser.setUserLikeCount(totalLikes);
        reviewUser.setUserGrade(newGrade);
        userRepository.save(reviewUser);
    }
}
