package com.movie.moviecheck.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ReviewId implements Serializable{

    private String movieKey;
    private Integer userKey;

    public ReviewId() {

    }

    public ReviewId(String movieKey, Integer userKey) {
        this.movieKey = movieKey;
        this.userKey = userKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewId that = (ReviewId) o;
        return Objects.equals(movieKey, that.movieKey) &&
               Objects.equals(userKey, that.userKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieKey, userKey);
    }

    // Setter 메서드
    public void setGenreKey(String movieKey) {
        this.movieKey = movieKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }
}
