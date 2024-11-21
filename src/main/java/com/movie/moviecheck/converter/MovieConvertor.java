package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.util.ImageUtil;

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
            .moviePoster(ImageUtil.encodeImageToBase64(movie.getMoviePoster())) // Base64로 변환
            .movieScore(movie.getMovieScore())
            .movieDirector(movie.getMovieDirector())
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
        .movieDirector(movieDto.getMovieDirector())
        .movieRuntime(movieDto.getMovieRuntime())
        .movieRelease(movieDto.getMovieRelease())
        .build();
    }
}
