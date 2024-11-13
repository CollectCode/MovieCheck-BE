package com.movie.moviecheck.password;

import java.security.NoSuchAlgorithmException;

import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;

@RequiredArgsConstructor
public class PasswordUtils {

    private final UserRepository  userRepository;

    // 비밀번호 해싱 메서드
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // 최대 64자
            byte[] hashedBytes = digest.digest(password.getBytes());

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("비밀번호 해싱 알고리즘을 찾을 수 없습니다.", e);
        }
    }

    public void registerUser(User user) {
        // 비밀번호 해싱
        String hashedPassword = PasswordUtils.hashPassword(user.getUserPassword());
        user.setUserPassword(hashedPassword);
        
        // 사용자 정보를 데이터베이스에 저장하는 로직
        userRepository.save(user);
    }
    
}
