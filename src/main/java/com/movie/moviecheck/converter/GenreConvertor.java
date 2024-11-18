package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.model.Genre;


@Component
public class GenreConvertor {

    public Genre converToEntity(GenreDto dto)    {
        return Genre.builder()
                    .genreKey(dto.getGenreKey())
                    .genreName(dto.getGenreName())
                    .build();
    }   

    public GenreDto converToDto(Genre genre)    {
        return GenreDto.builder()
                        .genreKey(genre.getGenreKey())
                        .genreName(genre.getGenreName())
                        .build();
    }
}
