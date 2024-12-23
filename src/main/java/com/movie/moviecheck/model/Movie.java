package com.movie.moviecheck.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies_table")
public class Movie {

    @Id
    @Column(name = "movie_key", nullable = false, length = 7)
    private String movieKey;

    @Column(name = "movie_title", nullable = false, length = 255)
    private String movieTitle;

    @Column(name = "movie_overview", length = 10000)
    private String movieOverview;

    @Column(name = "movie_poster", length = 255)
    private String moviePoster;

    @Column(name = "movie_score", nullable = true)
    private double movieScore = 0.0;  // 기본값 0

    @Column(name = "movie_runtime")
    private int movieRuntime;

    @Column(name = "movie_release", nullable = false)
    private LocalDate movieRelease;  // 날짜만 저장

    // 영화와 배우 매핑
    @OneToMany(mappedBy="movie", cascade=CascadeType.REMOVE)
    @JsonManagedReference
    private List<MovieActor> movieActor;

    @OneToMany(mappedBy="movie", cascade=CascadeType.REMOVE)
    @JsonManagedReference
    private List<Review> review;

    // 장르와 영화 매핑
    @OneToMany(mappedBy = "movie")
    @JsonManagedReference
    private List<GenreMovie> genreMovie;

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL)
    private Director director;
}