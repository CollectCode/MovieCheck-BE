package com.movie.moviecheck.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserGenreId implements Serializable {
    private Integer userKey; // user_key
    private String genreKey; // genre_key

    // 기본 생성자 추가
    public UserGenreId() {
    }

    // 매개변수가 있는 생성자
    public UserGenreId(Integer userKey, String genreKey) {
        this.userKey = userKey;
        this.genreKey = genreKey;
    }

    // equals와 hashCode 메서드 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGenreId that = (UserGenreId) o;
        return Objects.equals(userKey, that.userKey) &&
               Objects.equals(genreKey, that.genreKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userKey, genreKey);
    }

    // Setter 메서드
    public void setGenreKey(String genreKey) {
        this.genreKey = genreKey;
    }

    public void setUserKey(Integer userKey) {
        this.userKey = userKey;
    }
}