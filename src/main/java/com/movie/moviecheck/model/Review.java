package com.movie.moviecheck.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_table")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_key", nullable = false)
    private Integer reviewKey;

    @ManyToOne
    @JoinColumn(name = "movie_key", referencedColumnName = "movie_key", nullable = false)
    private Movie movie; // Movie 엔티티와의 관계

    @ManyToOne
    @JoinColumn(name = "user_key", referencedColumnName = "userKey") // userKey로 매핑
    private User user; // User 엔티티와의 관계

    @Column(name = "review_content", nullable = false, length = 255)
    private String reviewContent;

    @Column(name = "review_time", nullable = false)
    private LocalDateTime reviewTime;

    @Column(name = "review_good", nullable = false)
    private Integer reviewGood;

    @Column(name = "review_bad", nullable = false)
    private Integer reviewBad;
}
