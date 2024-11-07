package com.movie.moviecheck.service;

import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor 
public class MovieService {

    private final MovieRepository movieRepository; 

    // 영화 조회
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // id로 영화 조회
    public Movie getMovieById(String id) {
        return movieRepository.findById(id).orElse(null);
    }

    // 영화 생성
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // 영화 삭제
    public boolean deleteMovie(String id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
