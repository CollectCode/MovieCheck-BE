package com.movie.moviecheck.converter;

import com.movie.moviecheck.dto.MovieGenreDto;
import com.movie.moviecheck.embedded.MovieGenreId;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.MovieGenre;
import com.movie.moviecheck.service.GenreService;
import com.movie.moviecheck.service.MovieService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MovieGenreConvertor {

    private final MovieService movieService;
    private final GenreService genreService;

    public MovieGenreDto convertToDto(MovieGenre movieGenre){
        return MovieGenreDto.builder()
                            .movieKey(movieGenre.getMovie().getMovieKey())
                            .genreKey(movieGenre.getGenre().getGenreKey())
                            .build();
    }

    public MovieGenre convertToEntity(MovieGenreDto movieGenreDto){
        MovieGenreId movieGenreId = new MovieGenreId(movieGenreDto.getMovieKey(), movieGenreDto.getGenreKey());
        Movie movie = movieService.getMovieByMovieKey(movieGenreDto.getMovieKey());
        Genre genre = genreService.getGenreById(movieGenreDto.getGenreKey());
        return MovieGenre.builder()
                         .id(movieGenreId)
                         .movie(movie)
                         .genre(genre)
                         .build();
    }

}
