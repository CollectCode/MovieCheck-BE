package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Movie;

@Component
public class MovieConvertor {

    public MovieDto convertToDto(Movie movie) {
        if (movie == null) {
            return null;
        }
        return MovieDto.builder()
            .movieKey(movie.getMovieKey())
            .movieTitle(movie.getMovieTitle())
            .movieOverview(movie.getMovieOverview())
            .moviePoster(movie.getMoviePoster()) // Base64로 변환
            .movieScore(movie.getMovieScore())
            .movieRuntime(movie.getMovieRuntime())
            .movieRelease(movie.getMovieRelease())
            .build();
    }

    // MovieDto를 Movie로 변환하는 메서드
    public Movie convertToEntity(MovieDto movieDto) {
        if(movieDto == null){
            return null;
        }
        return Movie.builder()
        .movieKey(movieDto.getMovieKey())
        .movieTitle(movieDto.getMovieTitle())
        .movieOverview(movieDto.getMovieOverview())
        .moviePoster(movieDto.getMoviePoster())
        .movieScore(movieDto.getMovieScore())
        .movieRuntime(movieDto.getMovieRuntime())
        .movieRelease(movieDto.getMovieRelease())
        .build();
    }
}
