package com.movie.moviecheck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserLike;

public interface UserLikeRepository extends JpaRepository<UserLike,Integer> {
    boolean existsByUser(User user);
    Optional<UserLike> findByUserAndReview(User user, Review review);
}
