package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.UserLikeDto;
import com.movie.moviecheck.embedded.UserLikeId;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserLike;
import com.movie.moviecheck.service.MovieService;
import com.movie.moviecheck.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserLikeConvertor {

    private final UserService userService;
    private final MovieService movieService;

    public UserLikeDto convertToDto(UserLike userLike) {
        return UserLikeDto.builder()
                           .userKey(userLike.getUser().getUserKey())
                           .movieKey(userLike.getMovie().getMovieKey())
                           .build();
    }
    
    public UserLike convertToEntity(UserLikeDto userLikeDto) {
        UserLikeId UserLikeId = new UserLikeId(userLikeDto.getUserKey(), userLikeDto.getMovieKey());
        User user = userService.findByKey(userLikeDto.getUserKey());
        Movie movie = movieService.getMovieByMovieKey(userLikeDto.getMovieKey());
        return UserLike.builder()
                        .id(UserLikeId)
                        .user(user)
                        .movie(movie)
                        .build();
    }

}
