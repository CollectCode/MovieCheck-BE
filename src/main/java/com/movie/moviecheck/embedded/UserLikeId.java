package com.movie.moviecheck.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserLikeId implements Serializable {
    private Integer userKey; // user_key
    private String movieKey; // movie_key

    // 기본 생성자 추가
    public UserLikeId() {
    }

    // 매개변수가 있는 생성자
    public UserLikeId(Integer userKey, String movieKey) {
        this.userKey = userKey;
        this.movieKey = movieKey;
    }

    // equals와 hashCode 메서드 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLikeId that = (UserLikeId) o;
        return Objects.equals(userKey, that.userKey) &&
               Objects.equals(movieKey, that.movieKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userKey, movieKey);
    }

    // Setter 메서드
    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }

    public void setMovieKey(String movieKey) {
        this.movieKey = movieKey;
    }
}
