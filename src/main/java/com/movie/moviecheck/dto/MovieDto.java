package com.movie.moviecheck.dto;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private String movieKey;       // 영화 키
    private String movieTitle;     // 영화 제목
    private String movieOverview;   // 영화 개요
    private String moviePoster;     // 영화 포스터 URL
    private double movieScore;     // 영화 평점
    private String movieDirector;   // 감독
    private Integer movieRuntime;
    private LocalDate movieRelease;
    private List<ActorDto> actors;

    public MovieDto(String movieKey, String movieTitle, String moviePoster) {
        this.movieKey = movieKey;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;        
    }
}

