package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity; // User 모델 클래스 필요
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping; // 서비스 클래스 필요
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<WrapperClass<UserDto>> goToLogin(@RequestBody UserDto user, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(user, request, response);        
    }   

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<WrapperClass<UserDto>> goToCreateUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    // 회원가입 이메일 중복체크
    @PostMapping("/check/email")
    public ResponseEntity<WrapperClass<UserDto>> goToEmailCheck(@RequestBody UserDto userDto) {
        return userService.emailCheck(userDto);
    }

    // 회원가입 닉네임 중복체크
    @PostMapping("/check/name")
    public ResponseEntity<WrapperClass<UserDto>> goToExistName(@RequestBody UserDto userDto) {
        return userService.isNameExist(userDto);
    }

    // 마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<WrapperClass<UserDto>> goTomyPage(HttpServletRequest request) {
        return userService.getMyPage(request);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<WrapperClass<UserDto>> goToLogout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logout(request, response);
    }

    // 회원 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<WrapperClass<UserDto>> goToDeleteUser(HttpServletRequest request, HttpServletResponse response) {
        return userService.deleteUser(request,response);
    }

    // 회원 정보 갱신
    @PutMapping("/update")
    public ResponseEntity<WrapperClass<UserDto>> goToUpdateUser(HttpServletRequest request, @RequestBody UserDto userDto) {
        return userService.updateUser(request, userDto);
    }

    // 이미지 업로드
    @PostMapping("/uploadimage")
    public ResponseEntity<WrapperClass<UserDto>> goToUpLoadImage(@RequestParam("userImage") MultipartFile userImage, HttpServletRequest request) {
        return userService.uploadImage(request, userImage);
    }

}