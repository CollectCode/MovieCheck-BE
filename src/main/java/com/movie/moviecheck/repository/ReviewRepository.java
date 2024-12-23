package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.Review;

import java.util.List;

import com.movie.moviecheck.model.User;

public interface ReviewRepository extends JpaRepository<Review,Integer> {
    List<Review> findByMovie_MovieKey(String movieKey); // 특정 영화의 리뷰 조회
    List<Review> findByUser_UserKey(int userKey); // 특정 사용자의 리뷰 조회
    Review findByUser_userKeyAndMovie_MovieKey(Integer userKey, String movieKey);
    Review findByReviewKey(Integer reviewKey);
    Review findByMovieAndUser(Movie movie, User user);
}


// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;

// public interface ReviewRepository extends JpaRepository<Review, Integer> {
//     List<Review> findByMovie_MovieKey(Integer movieKey); // 특정 영화의 리뷰 조회
//     List<Review> findByUser_UserKey(String userKey); // 특정 사용자의 리뷰 조회
// }
