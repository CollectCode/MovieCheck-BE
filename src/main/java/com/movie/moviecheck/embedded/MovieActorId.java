package com.movie.moviecheck.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MovieActorId implements Serializable{

    private String movieKey;
    private String actorKey;

    public MovieActorId() {
    }

    // 매개변수가 있는 생성자
    public MovieActorId(String movieKey, String actorKey) {
        this.movieKey = movieKey;
        this.actorKey = actorKey;
    }

    // equals와 hashCode 메서드 오버라이드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieActorId that = (MovieActorId) o;
        return Objects.equals(movieKey, that.movieKey) &&
               Objects.equals(actorKey, that.actorKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieKey, actorKey);
    }

    // Setter 메서드
    public void setGenreKey(String movieKey) {
        this.movieKey = movieKey;
    }

    public void setUserKey(String actorKey) {
        this.actorKey = actorKey;
    }
}
