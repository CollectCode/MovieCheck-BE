package com.movie.moviecheck.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.movie.moviecheck.embedded.ReviewId;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Integer reviewKey;        // 리뷰 키
    private String movieKey;         // 영화 키
    private Integer userKey;           // 사용자 키
    private String reviewContent;      // 리뷰 내용
    private LocalDateTime reviewTime; // 리뷰 시간
    private Integer reviewLike;       // 긍정적인 평가 수

    public ReviewDto(Integer reviewKey, Integer userKey, String reviewContent, LocalDateTime reviewTime,
            Integer reviewLike) {
                this.reviewKey = reviewKey;
                this.userKey = userKey;
                this.reviewContent = reviewContent;
                this.reviewTime = reviewTime;
                this.reviewLike = reviewLike;
    }
}
