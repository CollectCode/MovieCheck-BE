package com.movie.moviecheck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.movie.moviecheck.embedded.GenreMovieId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "movie_genre_table")
@NoArgsConstructor
@AllArgsConstructor
public class GenreMovie {

    @EmbeddedId
    private GenreMovieId id;

    @ManyToOne
    @MapsId("movieKey")
    @JoinColumn(name = "movie_key")
    @JsonBackReference
    private Movie movie;

    @ManyToOne
    @MapsId("genreKey")
    @JoinColumn(name = "genre_key")
    private Genre genre;
}
