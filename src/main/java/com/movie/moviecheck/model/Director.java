package com.movie.moviecheck.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "directors_table")
public class Director {
    
    @Id
    @Column(name = "movie_key", length = 7, nullable = false)
    private String movieKey;

    @Column(name = "director_name", length = 50, nullable = false)
    private String directorName;

    @Column(name = "director_image", length = 255)
    private String directorImage;

    @OneToOne
    @JoinColumn(name = "movie_key", referencedColumnName = "movie_key")
    private Movie movie;

}