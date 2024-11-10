package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.LoginDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User; // User 모델 클래스 필요
import com.movie.moviecheck.service.UserService; // 서비스 클래스 필요
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입

    // // 로그인
    // @PostMapping("/login")
    // public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
    //     String token = userService.login(loginDto);
    //     return ResponseEntity.ok(token); // JWT 토큰 반환
    // }

    @PostMapping("/login")
    public ResponseEntity<WrapperClass<String>> login(@RequestBody User user) {
    // 이메일 존재 여부 확인
    if (!userService.isEmailExists(user.getUserEmail())) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new WrapperClass<>("Email not found")); // 이메일이 존재하지 않을 경우 에러 메시지 반환
    }

    // 사용자 정보 검증
    User user1 = userService.findByEmailAndPassword(user.getUserEmail(), user.getUserPassword());
    
    if (user1 != null) {
        // 로그인 성공
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new WrapperClass<>("Login successful")); // 로그인 성공 메시지 반환
    } else {
        // 로그인 실패
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new WrapperClass<>("Invalid password")); // 로그인 실패 메시지 반환
    }
}




    // 로그인
    // /api/users/login 
    // @PostMapping("/login")
    // public String showLoginForm(@RequestBody User user,Model model) {
    //     // 이메     일 존재 여부 확인
    //     if (!userService.isEmailExists(user.getUserEmail())) {
    //         model.addAttribute("error", "Email not found");
    //         return "login"; // 이메일이 존재하지 않을 경우 로그인 페이지로 돌아갑니다.
    //     }

    //     // 사용자 정보 검증
    //     User user1 = userService.findByEmailAndPassword(user.getUserEmail(),user.getUserPassword());
        
    //     if (user1 != null) {
    //         // 로그인 성공
    //         return "redirect:/main/home"; // 로그인 성공 시 홈으로 리다이렉트
    //     } else {
    //         // 로그인 실패
    //         model.addAttribute("error", "Invalid password");
    //         return "login"; // 로그인 페이지로 돌아가면서 에러 메시지 전달
    //     }
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
            userDto.getUserName(), // 이름으로 변경
            0, // 초기 누적 좋아요
            0, // 초기 누적 싫어요
            userDto.getUserContent(),
            userDto.getUserGender()
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
            user.getUserContent(),
            user.getUserGender()
        );
    }

    // @GetMapping("/check-email")
    // @ResponseBody
    // public Map<String, Boolean> checkEmail(@RequestParam String userEmail) {
    //     boolean available = !userService.isEmailTaken(userEmail);
    //     return Collections.singletonMap("available", available);
    // }


    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String userEmail) {
    boolean exists = userService.isEmailExists(userEmail); // 이메일 존재 여부 확인
    return ResponseEntity.ok(exists);
}


}
