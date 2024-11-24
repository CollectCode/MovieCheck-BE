package com.movie.moviecheck.embedded;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserLikeId {

    private Integer userKey;
    private ReviewId reviewId;

    public UserLikeId(){

    }

    public UserLikeId(Integer userKey, ReviewId reviewId) {
        this.userKey = userKey;
        this.reviewId = reviewId;
    }

    // equals와 hashCode 메서드 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLikeId that = (UserLikeId) o;
        return Objects.equals(userKey, that.userKey) &&
               Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userKey, reviewId);
    }

    // Setter 메서드
    public void setGenreKey(ReviewId reviewId) {
        this.reviewId = reviewId;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }
}
