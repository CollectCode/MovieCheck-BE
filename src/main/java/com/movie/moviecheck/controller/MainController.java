package com.movie.moviecheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    // 메인 페이지 반환
    @GetMapping("/main")
    public String mainPage() {
        return "main"; // main.html 파일 반환
    }
}