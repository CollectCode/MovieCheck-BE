package com.movie.moviecheck.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // User 모델 클래스 필요
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping; // 서비스 클래스 필요
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService; // UserService 주입
    private final UserConvertor userConvertor;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<WrapperClass<UserDto>> goToLogin(@RequestBody UserDto user, HttpServletRequest request, HttpServletResponse response) {
        return userService.login(user, request, response);        
    }   

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<WrapperClass<UserDto>> goToCreateUser(@RequestBody UserDto userDto) {
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
    public ResponseEntity<WrapperClass<UserDto>> goToDeleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }

    // 회원 정보 갱신
    @PutMapping("/update")
    public ResponseEntity<WrapperClass<UserDto>> goToUpdateUser(HttpServletRequest request, @RequestBody UserDto userDto) {
        return userService.updateUser(request, userDto);
    }

    // 이미지 업로드(추후 리팩토링)
    @PostMapping("/uploadImage/{userKey}")
    public ResponseEntity<WrapperClass<String>> uploadImage(@RequestParam("userImage") MultipartFile userImage, 
                                                        @PathVariable("userKey") Integer userKey) {
        String msg;
        UserDto userDto = userConvertor.convertToDto(userService.findByKey(userKey));
        User user = userConvertor.convertToEntity(userDto);
        // 사용자 정보를 찾지 못한 경우
        if (user == null) {
            msg = "User not found";
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new WrapperClass<>(null, msg));
        }
        // 파일 이름 생성 및 저장 경로 설정
        String uploadDir = new File("src/main/resources/static/images/users/").getAbsolutePath();
        String fileName = userKey + "_user.png"; // 파일 확장자 추가
        File destinationFile = new File(uploadDir, fileName);
        try {
            // 파일 저장
            userImage.transferTo(destinationFile);
            // 사용자 프로필 경로 설정 (상대 경로로 설정)
            user.setUserProfile("/images/users/" + fileName); // DB에 저장할 경로 설정
            // 사용자 정보를 데이터베이스에 저장
            userService.saveUser(user);
            msg = "Profile image uploaded successfully";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(null, msg));
        } catch (IOException e) {
            e.printStackTrace();
            msg = "Failed to upload profile image";
            return ResponseEntity // 500 : 서버에서 예상치 못한 오류가 발생했을 때
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new WrapperClass<>(null, msg));
        }
    }
}
