package com.movie.moviecheck.util;

import com.movie.moviecheck.embedded.ReviewId;

public interface ReviewRepositoryCustom {
    void incrementLike(ReviewId reviewId);
    void decrementLike(ReviewId reviewId);
}
