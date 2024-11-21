package com.movie.moviecheck.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "genre_table")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre {

    @Id
    @Column(name = "genre_key", nullable = false, length = 7)
    private String genreKey;

    @Column(name = "genre_name", length = 20)
    private String genreName;

    // 회원과 장르 매핑
    @OneToMany(mappedBy = "genre")
    private List<UserGenre> userGenre;

    // 장르와 영화 매핑
    @OneToMany(mappedBy="genre", cascade=CascadeType.REMOVE)
    @JsonManagedReference
    private List<GenreMovie> movieGenre;

}