package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.dto.UserGenreDto;
import com.movie.moviecheck.embedded.UserGenreId;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserGenre;
import com.movie.moviecheck.service.GenreService;
import com.movie.moviecheck.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserGenreConvertor {

    private final UserService userService;
    private final GenreService genreService;

    public UserGenreDto convertToDto(UserGenre userGenre) {
        UserGenreDto userGenreDto = new UserGenreDto();
        userGenreDto.setGenreKey(userGenre.getGenre().getGenreKey());
        userGenreDto.setUserKey(userGenre.getUser().getUserKey());
        return userGenreDto;
    }

    public UserGenre convertToEntity(UserGenreDto userGenreDto) {
        UserGenreId userGenreId = new UserGenreId(userGenreDto.getUserKey(), userGenreDto.getGenreKey());
        UserGenre userGenre = new UserGenre();
        userGenre.setId(userGenreId);
        return userGenre;
    }
}

