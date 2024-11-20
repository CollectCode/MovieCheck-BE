package com.movie.moviecheck.service;

import com.movie.moviecheck.controller.MovieController;
import com.movie.moviecheck.converter.MovieConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor 
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieConvertor movieConvertor;

    // 영화 조회
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // movieKey로 영화 조회
    public Movie getMovieByMovieKey(String movieKey) {
        return movieRepository.findByMovieKey(movieKey);
    }

    // 영화 삭제
    public boolean deleteMovie(String movieKey) {
        if (movieRepository.existsByMovieKey(movieKey)) {
            movieRepository.existsByMovieKey(movieKey);
            return true;
        }
        return false;
    }
}
