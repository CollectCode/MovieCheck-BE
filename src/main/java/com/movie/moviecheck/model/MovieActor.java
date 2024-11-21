package com.movie.moviecheck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.movie.moviecheck.embedded.MovieActorId;

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
@Table(name = "movie_actor_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieActor {

    @EmbeddedId
    private MovieActorId id;

    @ManyToOne
    @MapsId("movieKey") // 복합 키의 user_key를 매핑
    @JoinColumn(name = "movie_key")
    @JsonBackReference
    private Movie movie; // User 엔티티와의 관계

    @ManyToOne
    @MapsId("actorKey") // 복합 키의 genre_key를 매핑
    @JoinColumn(name = "actor_key")
    private Actor actor; // Genre 엔티티와의 관계
}
