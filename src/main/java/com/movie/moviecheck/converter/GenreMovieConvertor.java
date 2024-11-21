package com.movie.moviecheck.converter;

import com.movie.moviecheck.dto.GenreMovieDto;
import com.movie.moviecheck.embedded.GenreMovieId;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.GenreMovie;
import com.movie.moviecheck.service.GenreService;
import com.movie.moviecheck.service.MovieService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenreMovieConvertor {

    private final MovieService movieService;
    private final GenreService genreService;

    public GenreMovieDto convertToDto(GenreMovie movieGenre){
        return GenreMovieDto.builder()
                            .movieKey(movieGenre.getMovie().getMovieKey())
                            .genreKey(movieGenre.getGenre().getGenreKey())
                            .build();
    }

    public GenreMovie convertToEntity(GenreMovieDto movieGenreDto){
        GenreMovieId movieGenreId = new GenreMovieId(movieGenreDto.getMovieKey(), movieGenreDto.getGenreKey());
        // Movie movie = movieService.getMovieByMovieKey(movieGenreDto.getMovieKey());
        Genre genre = genreService.getGenreById(movieGenreDto.getGenreKey());
        return GenreMovie.builder()
                         .id(movieGenreId)
                        //  .movie(movie)
                         .genre(genre)
                         .build();
    }

}
