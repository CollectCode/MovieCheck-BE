package com.movie.moviecheck.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "user_genre_table")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserGenre implements Serializable {

    @Column(name = "user_key", nullable = false)
    private Integer userKey;

    @Column(name = "genre_key", nullable = false)
    private Integer genreKey;

    // Getter, Setter
}
