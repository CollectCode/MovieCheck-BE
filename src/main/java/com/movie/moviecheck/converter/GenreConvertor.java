package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.model.Genre;

@Component
public class GenreConvertor {

    public Genre converToEntity(GenreDto dto)    {
        Genre genre = new Genre();
        genre.setGenreKey(dto.getGenreKey());
        genre.setGenreName(dto.getGenreName());
        return genre;
    }

    public GenreDto converToDto(Genre genre)    {
        GenreDto genreDto = new GenreDto();
        genreDto.setGenreKey(genre.getGenreKey());
        genreDto.setGenreName(genre.getGenreName());
        return genreDto;
    }
}
