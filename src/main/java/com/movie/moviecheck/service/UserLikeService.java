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

    private final UserRepository userRepository;

    public void addLike(ReviewDto reviewDto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        Review review = reviewRepository.findByReviewKey(reviewDto.getReviewKey());
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
            if(review.getReviewLike() >= 10){
                User reviewuser = userService.findByKey(review.getUser().getUserKey());
                reviewuser.setUserGrade("조연");
                userRepository.save(reviewuser);
            }
            else if(review.getReviewLike() >= 50){
                User reviewuser = userService.findByKey(review.getUser().getUserKey());
                reviewuser.setUserGrade("주연");
                userRepository.save(reviewuser);
            }
            else if(review.getReviewLike() >= 100){
                User reviewuser = userService.findByKey(review.getUser().getUserKey());
                reviewuser.setUserGrade("감독");
                userRepository.save(reviewuser);
            }
            else if(review.getReviewLike() >= 500){
                User reviewuser = userService.findByKey(review.getUser().getUserKey());
                reviewuser.setUserGrade("박평식");
                userRepository.save(reviewuser);
            }
            reviewRepository.save(review); // 리뷰 업데이트
        } else {
            // 좋아요 삭제
            Optional<UserLike> userLike = userLikeRepository.findByUserAndReview(user, review);
            if (userLike.isPresent()) {
                userLikeRepository.delete(userLike.get());

                // 리뷰의 좋아요 수 감소
                review.setReviewLike(review.getReviewLike() > 0 ? review.getReviewLike() - 1 : 0);
                if(review.getReviewLike() < 10){
                    User reviewuser = userService.findByKey(review.getUser().getUserKey());
                    reviewuser.setUserGrade("관람객");
                    userRepository.save(reviewuser);
                }
                else if(review.getReviewLike() < 50){
                    User reviewuser = userService.findByKey(review.getUser().getUserKey());
                    reviewuser.setUserGrade("조연");
                    userRepository.save(reviewuser);
                }
                else if(review.getReviewLike() < 100){
                    User reviewuser = userService.findByKey(review.getUser().getUserKey());
                    reviewuser.setUserGrade("주연");
                    userRepository.save(reviewuser);
                }
                else if(review.getReviewLike() < 500){
                    User reviewuser = userService.findByKey(review.getUser().getUserKey());
                    reviewuser.setUserGrade("감독");
                    userRepository.save(reviewuser);
                }
                reviewRepository.save(review); // 리뷰 업데이트
            }
        }
    }    
}