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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "comment_table")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentKey;

    @ManyToOne
    @JoinColumn(name = "review_key", nullable = false) // 리뷰와의 관계
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_key", nullable = false) // 좋아요를 누른 사용자와의 관계
    private User user;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;
    
    @Column(name = "comment_time", nullable = false)
    private LocalDateTime commentTime;
}
