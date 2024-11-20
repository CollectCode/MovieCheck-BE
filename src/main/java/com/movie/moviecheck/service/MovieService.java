package com.movie.moviecheck.service;

import com.movie.moviecheck.controller.MovieController;
import com.movie.moviecheck.converter.MovieConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.MovieDto;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor 
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieConvertor movieConvertor;
    // 영화 조회
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();  // Movie 리스트를 가져옴
        return movies.stream()
                     .map(movie -> {
                         // MovieDto로 변환
                         MovieDto movieDto = movieConvertor.convertToDto(movie);
                         // 줄거리(movie_overview)가 255자를 초과하거나 같으면 '...' 추가
                         if (movieDto.getMovieOverview().length() >= 255) {
                             movieDto.setMovieOverview(movieDto.getMovieOverview().substring(0, 255) + "...");
                         }
                         return movieDto;
                     })
                     .collect(Collectors.toList());
    }

    // 영화 디테일
    public MovieDto getMovieDetails(MovieDto movieDto) {
        // 요청으로 받은 movieDto에서 영화 ID를 사용해 해당 영화를 DB에서 조회
        Movie movie = movieRepository.findById(movieDto.getMovieKey()).orElse(null);
        
        if (movie != null) {
            // Movie 객체를 MovieDto로 변환
            movieDto = movieConvertor.convertToDto(movie);
    
            // 줄거리 255자 초과 시 '...' 추가
            if (movieDto.getMovieOverview().length() > 255) {
                movieDto.setMovieOverview(movieDto.getMovieOverview().substring(0, 255) + "...");
            }
            
            return movieDto;
        } else {
            return null; // 영화가 없는 경우 null 반환
        }
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
