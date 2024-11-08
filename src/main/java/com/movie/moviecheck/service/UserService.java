package com.movie.moviecheck.service;

import com.movie.moviecheck.dto.LoginDto;
import com.movie.moviecheck.model.User; // User 모델 클래스 필요
import com.movie.moviecheck.repository.UserRepository; // UserRepository 필요
// import com.movie.moviecheck.util.JwtUtil;

import lombok.RequiredArgsConstructor;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; // UserRepository 주입

    // private final BCryptPasswordEncoder passwordEncoder;

    // private final JwtUtil jwtUtil;

    // // 로그인 메서드
    // public String login(LoginDto loginDto) {
    //     Optional<User> user = userRepository.findByUserEmail(loginDto.getUserEmail());

    //     if (user.isPresent() && passwordEncoder.matches(loginDto.getUserPassword(), user.get().getUserPassword())) {
    //         // 비밀번호가 일치하면 JWT 토큰 생성
    //         return jwtUtil.generateToken(user.get().getUserEmail());
    //     } else {
    //         throw new RuntimeException("Invalid email or password");
    //     }
    // }

    private static Map<String, User> userDatabase = new HashMap<>(); // 이메일로 사용자 관리
    private static Map<String, User> nameDatabase = new HashMap<>();  // 닉네임으로 사용자 관리

    // 회원 생성
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // 회원 삭제
    public boolean deleteUser(String userKey) {
        if (userRepository.existsById(userKey)) {
            userRepository.deleteById(userKey);
            return true;
        }
        return false;
    }

    // 이름 변경
    public User updateName(String userKey, String newName) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserName(newName); // 이름 변경
            return userRepository.save(user);
        }
        return null;
    }

    // 비밀번호 변경
    public User updatePassword(String userKey, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserPassword(newPassword);
            return userRepository.save(user);
        }
        return null;
    }

    // 한줄소개 변경
    public User updateContent(String userKey, String newContent) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserContent(newContent);
            return userRepository.save(user);
        }
        return null;
    }

    public boolean isEmailExists(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    public User findByEmailAndPassword(String userEmail, String userPassword) {
        return userRepository.findByUserEmailAndUserPassword(userEmail, userPassword);
    }
}
