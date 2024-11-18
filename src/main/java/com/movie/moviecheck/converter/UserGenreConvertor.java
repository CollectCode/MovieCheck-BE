package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.UserGenreDto;
import com.movie.moviecheck.embedded.UserGenreId;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserGenre;
import com.movie.moviecheck.service.GenreService;
import com.movie.moviecheck.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserGenreConvertor {

    private final UserService userService;
    private final GenreService genreService;

    public UserGenreDto convertToDto(UserGenre userGenre) {
        return UserGenreDto.builder()
                           .genreKey(userGenre.getGenre().getGenreKey())
                           .userKey(userGenre.getUser().getUserKey())
                           .build();
    }

    public UserGenre convertToEntity(UserGenreDto userGenreDto) {
        UserGenreId userGenreId = new UserGenreId(userGenreDto.getUserKey(), userGenreDto.getGenreKey());
        User user = userService.findByKey(userGenreDto.getUserKey());
        Genre genre = genreService.getGenreById(userGenreDto.getGenreKey());
        return UserGenre.builder()
                        .id(userGenreId)
                        .user(user)
                        .genre(genre)
                        .build();
    }
}

