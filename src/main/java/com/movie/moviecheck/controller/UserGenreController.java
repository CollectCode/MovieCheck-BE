package com.movie.moviecheck.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.service.UserGenreService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-genre")
@RequiredArgsConstructor
public class UserGenreController { 

    private final UserGenreService userGenreService;

    // 유저의 선호 장르 불러오기
    @GetMapping("/genres")
    public ResponseEntity<WrapperClass<List<GenreDto>>> goToGetLikeGenre(HttpServletRequest request) {
        return userGenreService.getLikeGenre(request);
    }

    @GetMapping("/modal/genres")
    public ResponseEntity<WrapperClass<List<GenreDto>>> goToGetLikeGenre(@RequestParam(name="userKey")Integer userKey) {
        return userGenreService.getLikeGenre(userKey);
    }

    // 유저의 선호장르 수정하기
    @PutMapping("/genres")
    public ResponseEntity<WrapperClass<ArrayList<GenreDto>>> goToSetLikeGenre(HttpServletRequest request, @RequestBody GenreDto[] genreDto) {
        return userGenreService.setLikeGenre(request, genreDto);
    }
}