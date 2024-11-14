package com.movie.moviecheck.controller;

import com.movie.moviecheck.dto.LoginDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User; // User 모델 클래스 필요
import com.movie.moviecheck.password.PasswordUtils;
import com.movie.moviecheck.service.UserService; // 서비스 클래스 필요
// import com.movie.moviecheck.session.SessionStore;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입
    // private final SessionStore sessionStore;  

    @PostMapping("/login")
    public ResponseEntity<WrapperClass<String>> login(
            @RequestBody User user, HttpServletRequest request, HttpServletResponse response) {

        String msg = "";
        // 이메일 존재 여부 확인
        if (!userService.isEmailExists(user.getUserEmail())) {
            msg = "Email not found";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(null,msg)); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }
        // 입력한 비밀번호 해싱
        String hashedInputPassword = PasswordUtils.hashPassword(user.getUserPassword());
        // 사용자 정보 검증
        User authenticatedUser = userService.findByEmailAndPassword(user.getUserEmail(), hashedInputPassword);
        if (authenticatedUser != null) {
            // 로그인 성공 시 세션 생성
            HttpSession session = request.getSession(true); // 세션이 없으면 새로 생성
            session.setAttribute("userKey", authenticatedUser.getUserKey()); // userKey 저장

            // 세션 ID를 메모리에 저장
            userService.saveSession(authenticatedUser.getUserKey(), session.getId());

            // 클라이언트에 세션 ID를 쿠키로 전달
            Cookie sessionCookie = new Cookie("SESSIONID", session.getId());
            sessionCookie.setHttpOnly(false);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (예: 1시간)
            response.addCookie(sessionCookie);
            msg = "Login successful";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(null,msg)); // 로그인 성공 메시지 반환
        } else {
            msg = "Invalid password";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(null,msg)); // 로그인 실패 메시지 반환
        }
    }
    // 회원가입
    @PostMapping("/signup")
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = convertToEntity(userDto);
        
        // 비밀번호 해싱
        String hashedPassword = PasswordUtils.hashPassword(user.getUserPassword());
        user.setUserPassword(hashedPassword); // 해싱된 비밀번호로 설정

        User savedUser = userService.createUser(user);
        return convertToDto(savedUser);
    }
    // 회원가입 아이디 중복체크
    @PostMapping("/signup/email")
    public ResponseEntity<WrapperClass<String>> existEmail(@RequestBody UserDto userDto) {
        String msg = "";
        if (!userService.isEmailExists(userDto.getUserEmail())) {
            msg = "available email";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(null,msg));
        }
        else{
            msg = "unavailable email";
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(new WrapperClass<>(null,msg)); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }
    }
    // 회원가입 닉네임 중복체크
    @PostMapping("/signup/name")
    public ResponseEntity<WrapperClass<String>> existName(@RequestBody UserDto userDto) {
        String msg = "";
        if (!userService.isNameExists(userDto.getUserName())) {
            msg = "available nickname";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(null,msg));
        }
        else{
            msg = "unavailable nickname";
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(new WrapperClass<>(null,msg)); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }
    }
    // 마이페이지
    @GetMapping("/mypage")
    public ResponseEntity<WrapperClass<User>> myPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션을 가져옴
        String msg = "";
        if (session != null) {
            Integer userKey = (Integer) session.getAttribute("userKey"); // 세션에서 userKey 가져오기
            if (userKey != null) {
                // 서버 메모리에서 세션 ID 확인 (필요시)
                String sessionId = userService.getSession(userKey);
                // 사용자 정보 가져오기
                User user = userService.getUserByKey(userKey);
                msg = "User information has been successfully retrieved.";
                return ResponseEntity.ok(new WrapperClass<>(user, msg)); // 사용자 정보를 메시지와 함께 반환
            }
        }
        msg = "Session does not exist or is invalid.";
        return ResponseEntity.status(HttpStatus.OK).body(new WrapperClass<>(null, msg)); // 세션이 없거나 유효하지 않으면 401 반환
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<WrapperClass<String>> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String msg = "";
        if (session != null) {
            session.invalidate();
        }else{
            msg = "logout failed";
            return ResponseEntity
            .status(HttpStatus.OK)
            .body(new WrapperClass<>(null,msg)); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }

        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setHttpOnly(true);
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        response.addCookie(jsessionCookie);

        Cookie sessionCookie = new Cookie("SESSIONID", null);
        sessionCookie.setHttpOnly(false);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        // 로그아웃 후 리다이렉트
        response.setHeader("Location", "/");
        response.setStatus(HttpServletResponse.SC_FOUND);
        msg = "logout sucessed";
        return ResponseEntity.status(HttpStatus.OK).body(new WrapperClass<>(null,msg));
    }
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
        String hashedPassword = PasswordUtils.hashPassword(newPassword); // 비밀번호 해싱
        User updatedUser = userService.updatePassword(userKey, hashedPassword);
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
}
