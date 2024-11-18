package com.movie.moviecheck.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "genre_table")
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @Column(name = "genre_key", nullable = false, length = 7)
    private String genreKey;

    @Column(name = "genre_name", length = 20)
    private String genreName;

    @OneToMany(mappedBy = "genre")
    private List<UserGenre> userGenres;
}
