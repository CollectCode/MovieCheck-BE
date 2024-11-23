package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.DirectorDto;
import com.movie.moviecheck.model.Director;

@Component
public class DirectorConvertor {

    public Director converToEntity(DirectorDto dto) {
        Director director = new Director();
        director.setDirectorName(dto.getDirectorName());
        director.setDirectorImage(dto.getDirectorImage());
        return director;
    }

    public DirectorDto converToDto(Director director) {
        return new DirectorDto(
                director.getDirectorName(),
                director.getDirectorImage()
        );
    }
}
