package com.movie.moviecheck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

    // 메인 페이지 반환
    @GetMapping(path = "/home")
    public String mainPage() {
        return "home";
    }

    @GetMapping(path = "/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(path = "/signup")
    public String signupPage() {
        return "signup";
    }
    @GetMapping(path = "/mypage")
    public String myPage() {
        return "mypage";
    }    
}