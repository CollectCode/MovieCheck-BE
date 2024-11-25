package com.movie.moviecheck.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.movie.moviecheck.embedded.UserGenreId;

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
@Table(name = "user_genre_table") // 매핑 테이블 이름
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGenre {

    @EmbeddedId
    private UserGenreId id; // 복합 기본 키

    @ManyToOne
    @MapsId("userKey") // 복합 키의 user_key를 매핑
    @JoinColumn(name = "user_key")
    @JsonBackReference
    private User user; // User 엔티티와의 관계

    @ManyToOne
    @MapsId("genreKey") // 복합 키의 genre_key를 매핑
    @JoinColumn(name = "genre_key")
    private Genre genre; // Genre 엔티티와의 관계

    // 추가적인 메서드가 필요할 경우 작성
}
