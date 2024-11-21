package com.movie.moviecheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.movie.moviecheck.model.Movie;

public interface MovieRepository extends JpaRepository<Movie,String>  {

    // movieKey로 영화 찾기
    Movie findByMovieKey(String movieKey);
    
    // 영화 제목으로 찾기
    Movie findByMovieTitle(String movieTitle);

    // movieKey가 존재하는지 확인
    boolean existsByMovieKey(String movieKey);

    // 여러 movieKey로 영화 가져오기
    List<Movie> findByMovieKeyIn(List<String> movieKey);

    List<Movie> findByMovieTitleIn(List<String> movieTitle);

    List<Movie> findByMovieTitleContainingIgnoreCase(String movieTitle);
}
