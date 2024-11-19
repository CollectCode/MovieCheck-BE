package com.movie.moviecheck.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class MovieGenreId implements Serializable {

    private String movieKey;
    private String genreKey;

    public MovieGenreId() {

    }

    public MovieGenreId(String movieKey, String genreKey) {
        this.movieKey = movieKey;
        this.genreKey = genreKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieGenreId that = (MovieGenreId) o;
        return Objects.equals(movieKey, that.movieKey) &&
               Objects.equals(genreKey, that.genreKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieKey, genreKey);
    }

    // Setter 메서드
    public void setGenreKey(String genreKey) {
        this.genreKey = genreKey;
    }

    public void setUserKey(String movieKey) {
        this.movieKey = movieKey;
    }
}
