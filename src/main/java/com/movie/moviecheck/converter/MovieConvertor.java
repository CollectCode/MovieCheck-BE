package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Movie;

@Component
public class MovieConvertor {

    public MovieDto convertToDto(Movie movie) {
        if(movie == null){
            return null;
        }
        return MovieDto.builder()
            .movieKey(movie.getMovieKey())
            .movieTitle(movie.getMovieTitle())
            .movieOverview(movie.getMovieOverview())
            .moviePoster(movie.getMoviePoster())
            .movieScore(movie.getMovieScore())
            .movieDirector(movie.getMovieDirector())
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
        .movieDirector(movieDto.getMovieDirector())
        .build();
    }
}
