package com.movie.moviecheck.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.movie.moviecheck.converter.ReviewConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.MovieRepository;
import com.movie.moviecheck.repository.ReviewRepository;
import com.movie.moviecheck.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    private final ReviewConvertor reviewConvertor;
    private final UserConvertor userConvertor;
    
    // 리뷰 추가
    public ResponseEntity<ReviewDto> addReview(@RequestBody ReviewDto reviewDto, HttpServletRequest request) {
        // 1. 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userKey") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // 로그인 필요 응답
        }
        String movieKey = reviewDto.getMovieKey();
        Integer userKey = (Integer) session.getAttribute("userKey");
        Optional<Movie> om = movieRepository.findById(movieKey);
        Optional<User> ou = userRepository.findById(userKey);
        User user = null;
        Movie movie = null;
        if(ou.isPresent())  {
            user = ou.get();
        }
        if(om.isPresent())   {
            movie = om.get();
        }

        // 2. 기존 리뷰 확인
        if (reviewDto.getReviewKey() != null) {
            Review existingReview = reviewRepository.findById(reviewDto.getReviewKey())
                    .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. reviewKey: " + reviewDto.getReviewKey()));
    
            // 2.1 리뷰 작성자 확인
            if (!existingReview.getUser().getUserKey().equals(userKey)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 권한 없음 응답
            }

            // 2.2 리뷰 내용 수정
            existingReview.setReviewContent(reviewDto.getReviewContent());
            existingReview.setReviewTime(LocalDateTime.now());
    
            // 2.3 저장 후 DTO 변환 및 반환
            Review updatedReview = reviewRepository.save(existingReview);
            ReviewDto responseDto = reviewConvertor.convertToDto(updatedReview);
            return ResponseEntity.ok(responseDto);  
        }
    
        // 3. 리뷰 작성 전 중복체크
        Review existingReviewForNew = reviewRepository.findByMovieAndUser(movie, user);
        if (existingReviewForNew != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 이미 리뷰가 존재함 응답
        }

        // 4. 새로운 리뷰 작성
        reviewDto.setMovieKey(reviewDto.getMovieKey());
        reviewDto.setUserKey(userKey);
        reviewDto.setReviewTime(LocalDateTime.now());
        reviewDto.setReviewLike(0);
    
        // 5. Review 엔티티 변환 및 저장
        Review review = reviewConvertor.convertToEntity(reviewDto);
        Review savedReview = reviewRepository.save(review);
    
        // 6. 저장된 Review 엔티티를 ReviewDto로 변환하여 반환
        ReviewDto responseDto = reviewConvertor.convertToDto(savedReview);
        return ResponseEntity.ok(responseDto);
    }
    

    // 리뷰 삭제
    public ResponseEntity<ReviewDto> deleteReview(Integer reviewKey, HttpServletRequest request) {

        // 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
        if (session == null || session.getAttribute("userKey") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 인증 실패 응답
        }
        Integer userKey = (Integer) session.getAttribute("userKey");

        // 리뷰 삭제 로직
        Review review = reviewRepository.findByReviewKey(reviewKey);

        // 리뷰가 없으면 404 응답
        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 리뷰 없음
        }
        // 리뷰 작성자가 현재 로그인한 사용자와 일치하는지 확인
        if (!review.getUser().getUserKey().equals(userKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 삭제 권한 없음
        }
        try {
            // 삭제되는 리뷰의 좋아요 수
            int deletedReviewLikes = review.getReviewLike() != null ? review.getReviewLike() : 0;

            // 리뷰 삭제
            reviewRepository.delete(review);
            // 작성자의 모든 리뷰 좋아요 수 재계산
            User reviewUser = review.getUser();
            int totalLikes = 0;

            List<Review> userReviews = reviewRepository.findByUser_UserKey(reviewUser.getUserKey());
            for (Review userReview : userReviews) {
                if (userReview.getReviewLike() != null) {
                    totalLikes += userReview.getReviewLike();
                }
            }
            // 작성자 등급 재조정
            String newGrade = calculateUserGrade(totalLikes);
            reviewUser.setUserGrade(newGrade);
            userRepository.save(reviewUser);
            return ResponseEntity.noContent().build(); // 성공적으로 삭제
        } catch (Exception e) {
            System.out.println(e);
            // 예외 발생 시 500 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 특정 영화의 모든 리뷰 조회
    public ResponseEntity<Map<String, Object>> getReviewsByMovie(String movieId) {
        List<Review> reviews = reviewRepository.findByMovie_MovieKey(movieId);
        List<ReviewDto> reviewDtos = new ArrayList<>();
        List<UserDto> reviewers = new ArrayList<>();
        
        // 좋아요 순으로 내림차순
        Collections.sort(reviewDtos,new Comparator<ReviewDto>() {
            @Override
            public int compare(ReviewDto r1, ReviewDto r2){
                return r2.getReviewLike().compareTo(r1.getReviewLike());
            }
        });

        for(Review review : reviews)    {
            ReviewDto reviewDto = reviewConvertor.convertToDto(review);
            User user = review.getUser();
            UserDto userDto = userConvertor.convertToDto(user);
            reviewDtos.add(reviewDto);
            reviewers.add(userDto);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewDtos);
        response.put("reviewers", reviewers);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 특정 사용자의 모든 리뷰 조회
    public List<Review> getReviewsByUser(int userKey) {
        return reviewRepository.findByUser_UserKey(userKey);
    }

    // 특정 리뷰 조회
    public Review getReviewByKey(Integer reviewKey) {
        return reviewRepository.findByReviewKey(reviewKey);
    }

    // 등급 계산 메서드
    public String calculateUserGrade(int totalLikes) {
        if (totalLikes >= 500) {
            return "박평식";
        } else if (totalLikes >= 100) {
            return "감독";
        } else if (totalLikes >= 50) {
            return "주연";
        } else if (totalLikes >= 2) {
            return "조연";
        } else {
            return "관람객";
        }
    }
}
