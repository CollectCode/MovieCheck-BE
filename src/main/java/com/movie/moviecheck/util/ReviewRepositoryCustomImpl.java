package com.movie.moviecheck.util;

import com.movie.moviecheck.embedded.ReviewId;
import com.movie.moviecheck.model.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    @Override
    public void incrementLike(ReviewId reviewId) {
        Review review = entityManager.find(Review.class, reviewId);
        if (review != null) {
            review.setReviewLike(review.getReviewLike() + 1);
            entityManager.merge(review); // 변경 내용을 반영
        }
    }
    @Transactional
    @Override
    public void decrementLike(ReviewId reviewId) {
        Review review = entityManager.find(Review.class, reviewId);
        if (review != null) {
            review.setReviewLike(review.getReviewLike() - 1);
            entityManager.merge(review); // 변경 내용을 반영
        }
    }
}
