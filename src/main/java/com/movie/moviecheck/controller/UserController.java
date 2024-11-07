package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.LoginDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User; // User 모델 클래스 필요
import com.movie.moviecheck.service.UserService; // 서비스 클래스 필요
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    // // 로그인
    // @PostMapping("/login")
    // public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
    //     String token = userService.login(loginDto);
    //     return ResponseEntity.ok(token); // JWT 토큰 반환
    // }


    // 회원 생성
    // "/api/user/signup"
    @PostMapping("/signup")
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = convertToEntity(userDto);
        User savedUser = userService.createUser(user);
        return convertToDto(savedUser);
    }

    // 회원 삭제
    // /api/users/{userKey}
    @DeleteMapping("/{userKey}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userKey) {
        if (userService.deleteUser(userKey)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 이름 변경
    // /api/users/{userKey}/name
    @PutMapping("/{userKey}/name")
    public ResponseEntity<UserDto> updateName(@PathVariable String userKey, @RequestParam String newName) {
        User updatedUser = userService.updateName(userKey, newName);
        if (updatedUser != null) {
            return ResponseEntity.ok(convertToDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 비밀번호 변경
    // /api/users/{userKey}/password
    @PutMapping("/{userKey}/password")
    public ResponseEntity<UserDto> updatePassword(@PathVariable String userKey, @RequestParam String newPassword) {
        User updatedUser = userService.updatePassword(userKey, newPassword);
        if (updatedUser != null) {
            return ResponseEntity.ok(convertToDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 한줄소개 변경
    // /api/users/{userKey}/content
    @PutMapping("/{userKey}/content")
    public ResponseEntity<UserDto> updateContent(@PathVariable String userKey, @RequestParam String newContent) {
        User updatedUser = userService.updateContent(userKey, newContent);
        if (updatedUser != null) {
            return ResponseEntity.ok(convertToDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // UserDto를 User로 변환하는 메서드
    private User convertToEntity(UserDto userDto) {
        return new User(
            userDto.getUserKey(),
            userDto.getUserEmail(),
            userDto.getUserPassword(),
            userDto.getName(), // 이름으로 변경
            0, // 초기 누적 좋아요
            0, // 초기 누적 싫어요
            userDto.getUserGrade(),
            userDto.getUserContent(),
            userDto.getGender()
        );
    }

    // User를 UserDto로 변환하는 메서드
    private UserDto convertToDto(User user) {
        return new UserDto(
            user.getUserKey(),
            user.getUserEmail(),
            user.getUserPassword(),
            user.getUserName(), // 이름으로 변경
            user.getUserGood(),
            user.getUserBad(),
            user.getUserGrade(),
            user.getUserContent(),
            user.getUsergender()
        );
    }
}
