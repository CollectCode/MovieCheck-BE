package com.movie.moviecheck.service;

import com.movie.moviecheck.converter.MovieConvertor;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.repository.MovieRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieConvertor movieConvertor;

    // 모든 영화 정보 가져오기
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                     .map(movieConvertor::convertToDto)
                     .collect(Collectors.toList());
    }

    public MovieDto getMovies(String movieKey){
        Movie movie = movieRepository.findByMovieKey(movieKey);
        return movieConvertor.convertToDto(movie);
    }

    // 영화 디테일 조회
    public ResponseEntity<MovieDto> getMovieDetails(MovieDto movieDto) {
        String movieKey = movieDto.getMovieKey();
        // 1. 요청 검증: movieKey가 없는 경우 400 Bad Request 반환
        if (movieKey == null || movieKey.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            // 2. Movie 상세 정보 조회
            MovieDto movieDetails = getMovies(movieKey);
            return ResponseEntity.ok(movieDetails);
        } catch (EntityNotFoundException e) {
            // 3. 조회 실패 시 404 Not Found 반환
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    
}
