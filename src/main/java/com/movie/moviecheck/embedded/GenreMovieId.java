package com.movie.moviecheck.embedded;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class GenreMovieId implements Serializable {

    private String genreKey;
    private String movieKey;
    

    public GenreMovieId() {

    }

    public GenreMovieId(String movieKey, String genreKey) {
        this.genreKey = genreKey;
        this.movieKey = movieKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreMovieId that = (GenreMovieId) o;
        return Objects.equals(genreKey, that.genreKey) &&
               Objects.equals(movieKey, that.movieKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreKey, movieKey);
    }

    // Setter 메서드
    public void setGenreKey(String genreKey) {
        this.genreKey = genreKey;
    }

    public void setUserKey(String movieKey) {
        this.movieKey = movieKey;
    }
}
