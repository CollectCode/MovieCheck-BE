package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.LoginDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User; // User 모델 클래스 필요
import com.movie.moviecheck.service.UserService; // 서비스 클래스 필요
// import com.movie.moviecheck.session.SessionStore;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입
    // private final SessionStore sessionStore;  

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
    
    // 회원가입
    @PostMapping("/signup")
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = convertToEntity(userDto);
        User savedUser = userService.createUser(user);
        return convertToDto(savedUser);
    }

    // 회원가입 아이디 중복체크
    @PostMapping("/signup/email")
    public ResponseEntity<WrapperClass<String>> existEmail(@RequestBody UserDto userDto) {
        if (!userService.isEmailExists(userDto.getUserEmail())) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>("사용가능한 이메일 입니다."));
        }
        else{
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new WrapperClass<>("중복된 이메일 입니다.")); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }
    }

    // 회원가입 닉네임 중복체크
    @PostMapping("/signup/name")
    public ResponseEntity<WrapperClass<String>> existName(@RequestBody UserDto userDto) {
        if (!userService.isEmailExists(userDto.getUserName())) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>("사용가능한 닉네임 입니다."));
        }
        else{
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new WrapperClass<>("중복된 닉네임 입니다.")); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }
    }
        // 사용자의 프로필 가져오기
        // @GetMapping("/profile")
        // public ResponseEntity<WrapperClass<Map<String, String>>> getProfile(
        //         @CookieValue(value = "SESSIONID", required = false) String sessionId) {
            
        //     // 세션 ID가 없는 경우, 로그인되지 않은 상태이므로 UNAUTHORIZED 응답 반환
        //     if (sessionId == null) {
        //         return ResponseEntity
        //                 .status(HttpStatus.UNAUTHORIZED)
        //                 .body(new WrapperClass<>(Map.of("message", "User not logged in")));
        //     }

        //     // 세션 ID를 통해 사용자를 조회
        //     User user = userService.findBySessionId(sessionId);
            
        //     // 사용자가 없는 경우, 세션이 유효하지 않으므로 UNAUTHORIZED 응답 반환
        //     if (user == null) {
        //         return ResponseEntity
        //                 .status(HttpStatus.UNAUTHORIZED)
        //                 .body(new WrapperClass<>(Map.of("message", "Invalid session")));
        //     }

        //     // 닉네임 정보 반환
        //     Map<String, String> responseBody = new HashMap<>();
        //     responseBody.put("userName", user.getUserName()); // 사용자 닉네임

        //     return ResponseEntity
        //             .status(HttpStatus.OK)
        //             .body(new WrapperClass<>(responseBody));
        // }



    // 회원 삭제
    // /api/users/{userKey}
    @DeleteMapping("/{userKey}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userKey) {
        if (userService.deleteUser(userKey)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 이름 변경
    // /api/users/{userKey}/name
    @PutMapping("/{userKey}/name")
    public ResponseEntity<UserDto> updateName(@PathVariable Integer userKey, @RequestParam String newName) {
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
    public ResponseEntity<UserDto> updatePassword(@PathVariable Integer userKey, @RequestParam String newPassword) {
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
    public ResponseEntity<UserDto> updateContent(@PathVariable Integer userKey, @RequestParam String newContent) {
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

    // @PostMapping("/check-email")
    // public ResponseEntity<Boolean> checkEmail(@RequestParam String userEmail) {
    // boolean exists = userService.isEmailExists(userEmail); // 이메일 존재 여부 확인
    // return ResponseEntity.ok(exists);
    // }
}
