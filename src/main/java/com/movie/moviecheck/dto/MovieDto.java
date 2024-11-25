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
    private Integer movieRuntime; // 영화 상영 시간
    private LocalDate movieRelease; // 영화 개봉일
    private List<ActorDto> actorDto; // 배우 이름, 사진
    private List<ReviewDto> reviewDto; // 리뷰 남긴 유저의 키, 코멘트, 남긴 시간, 좋아요
    private DirectorDto directorDto; // 감독 이름, 이미지 추가
    private List<String> genresName; // 장르 키 리스트 추가

    public MovieDto(String movieKey, String movieTitle, String moviePoster) {
        this.movieKey = movieKey;
        this.movieTitle = movieTitle;
        this.moviePoster = moviePoster;        
    }
}

