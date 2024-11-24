package com.movie.moviecheck.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(
    name = "review_table",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"movie_key", "user_key"}) // 복합 유니크 제약 조건 추가
    }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 단일 기본 키 유지
    private Integer reviewKey;

    @ManyToOne
    @JoinColumn(name = "movie_key", nullable = false)
    @JsonBackReference
    private Movie movie; // Movie 엔티티와의 관계

    @ManyToOne
    @JoinColumn(name = "user_key", nullable = false)
    private User user; // User 엔티티와의 관계

    @Column(name = "review_content", nullable = false, length = 255)
    private String reviewContent;

    @Column(name = "review_time", nullable = false)
    private LocalDateTime reviewTime;

    @Column(name = "review_like", nullable = false)
    private Integer reviewLike;
}
