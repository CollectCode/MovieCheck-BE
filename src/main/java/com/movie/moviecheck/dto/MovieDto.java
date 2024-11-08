package com.movie.moviecheck.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private String movieKey;       // 영화 키
    private String movieTitle;     // 영화 제목
    private String movieOverview;   // 영화 개요
    private String moviePoster;     // 영화 포스터 URL
    private Integer movieScore;     // 영화 평점
    private String movieDirector;   // 감독
}

