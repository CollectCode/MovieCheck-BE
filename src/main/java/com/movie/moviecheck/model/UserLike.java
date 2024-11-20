package com.movie.moviecheck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.movie.moviecheck.embedded.UserGenreId;
import com.movie.moviecheck.embedded.UserLikeId;

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
@Table(name = "user_like_table") // 매핑 테이블 이름
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLike {

    @EmbeddedId
    private UserLikeId id; // 복합 기본 키


    @ManyToOne
    @MapsId("userKey")
    @JoinColumn(name = "userKey")
    @JsonBackReference
    private User user;

    @ManyToOne
    @MapsId("movieKey")
    @JoinColumn(name = "movieKey")
    private Movie movie;

}
