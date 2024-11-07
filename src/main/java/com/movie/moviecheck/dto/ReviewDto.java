package com.movie.moviecheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Integer reviewKey;        // 리뷰 키
    private Integer movieKey;         // 영화 키
    private String userKey;           // 사용자 키
    private String reviewContent;      // 리뷰 내용
    private LocalDateTime reviewTime; // 리뷰 시간
    private Integer reviewGood;       // 긍정적인 평가 수
    private Integer reviewBad;        // 부정적인 평가 수
}
