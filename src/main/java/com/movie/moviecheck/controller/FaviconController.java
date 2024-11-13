package com.movie.moviecheck.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    @GetMapping("/favicon.ico")
    public void favicon() {
        // 빈 응답을 반환하거나, 필요시 설정 추가
    }
}
