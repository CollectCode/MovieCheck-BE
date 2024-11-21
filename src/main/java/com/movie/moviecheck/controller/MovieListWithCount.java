package com.movie.moviecheck.controller;

import java.util.List;

import com.movie.moviecheck.dto.MovieDto;

public class MovieListWithCount {

    private final List<MovieDto> movieList;
    private final int count;

    public MovieListWithCount(List<MovieDto> movieList, int count) {
        this.movieList = movieList;
        this.count = count;
    }

    public List<MovieDto> getMovieList() {
        return movieList;
    }

    public int getCount() {
        return count;
    }
}
