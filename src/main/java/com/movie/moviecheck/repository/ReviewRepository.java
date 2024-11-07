package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movie.moviecheck.model.Review;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer>  {
    List<Review> findByMovie_MovieKey(String movieKey); // 특정 영화의 리뷰 조회
    List<Review> findByUser_UserKey(String userKey); // 특정 사용자의 리뷰 조회
}


// import org.springframework.data.jpa.repository.JpaRepository;

// import java.util.List;

// public interface ReviewRepository extends JpaRepository<Review, Integer> {
//     List<Review> findByMovie_MovieKey(Integer movieKey); // 특정 영화의 리뷰 조회
//     List<Review> findByUser_UserKey(String userKey); // 특정 사용자의 리뷰 조회
// }
