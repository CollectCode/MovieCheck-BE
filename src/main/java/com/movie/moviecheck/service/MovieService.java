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

    // 영화 생성
    public MovieDto createMovie(MovieDto movieDto) {
        // 중복 확인
        if (movieRepository.existsByMovieKey(movieDto.getMovieKey())) {
            throw new RuntimeException("Movie with the same movieKey already exists.");
        }
        // 중복이 없으면 저장
        Movie movie = movieConvertor.convertToEntity(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        // 저장된 데이터를 DTO로 변환하여 반환
        return movieConvertor.convertToDto(savedMovie);
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
