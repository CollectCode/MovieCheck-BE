package com.movie.moviecheck.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.movie.moviecheck.embedded.ReviewId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "review_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @EmbeddedId
    private ReviewId id;

    @ManyToOne
    @MapsId("movieKey")
    @JoinColumn(name = "movie_key")
    @JsonBackReference
    private Movie movie; // Movie 엔티티와의 관계

    @ManyToOne
    @MapsId("userKey")
    @JoinColumn(name = "user_key")
    private User user; // User 엔티티와의 관계

    @Column(name = "review_content", nullable = false, length = 255)
    private String reviewContent;

    @Column(name = "review_time", nullable = false)
    private LocalDateTime reviewTime;

    @Column(name = "review_like", nullable = false)
    private Integer reviewLike;
}
