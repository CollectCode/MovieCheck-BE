package com.movie.moviecheck.service;

import com.movie.moviecheck.dto.LoginDto;
import com.movie.moviecheck.model.User; // User 모델 클래스 필요
import com.movie.moviecheck.repository.UserRepository; // UserRepository 필요
// import com.movie.moviecheck.util.JwtUtil;

import lombok.RequiredArgsConstructor;

// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; // UserRepository 주입

    private static Map<String, User> userDatabase = new HashMap<>(); // 이메일로 사용자 관리
    private static Map<String, User> nameDatabase = new HashMap<>();  // 닉네임으로 사용자 관리

    // 회원 생성
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 회원 삭제
    public boolean deleteUser(Integer userKey) {
        if (userRepository.existsById(userKey)) {
            userRepository.deleteById(userKey);
            return true;
        }
        return false;
    }

    // 이름 변경
    public User updateName(Integer userKey, String newName) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserName(newName); // 이름 변경
            return userRepository.save(user);
        }
        return null;
    }

    // 비밀번호 변경
    public User updatePassword(Integer userKey, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserPassword(newPassword);
            return userRepository.save(user);
        }
        return null;
    }

    // 한줄소개 변경
    public User updateContent(Integer userKey, String newContent) {
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

    public boolean isNameExists(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User findByEmailAndPassword(String userEmail, String userPassword) {
        return userRepository.findByUserEmailAndUserPassword(userEmail, userPassword);
    }
    public User findByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }
    // 추가: 사용자 정보를 가져오는 메소드
    public User findByKey(Integer userKey) {
        return userRepository.findByUserKey(userKey);
    }

    // --------------------------------------------------

    private final ConcurrentHashMap<Integer, String> userSessions = new ConcurrentHashMap<>();

    public void saveSession(Integer userKey, String sessionId) {
        userSessions.put(userKey, sessionId);
    }

    public String getSession(Integer userKey) {
        return userSessions.get(userKey);
    }

    public void removeSession(Integer userKey) {
        userSessions.remove(userKey);
    }

    // -----------------------------------------------------------
    // image upload
    public void uploadUserImage(MultipartFile file, User user) {
        if (file != null && !file.isEmpty()) {
            try {
                // 이미지 저장 경로 설정
                String uploadDir = "src/main/resources/static/images/";
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File destinationFile = new File(uploadDir + fileName);
                file.transferTo(destinationFile); // 파일 저장
    
                // 사용자 객체에 이미지 경로 설정
                user.setUserProfile(destinationFile + fileName); // 웹에서 접근할 수 있는 경로로 설정
            } catch (IOException e) {
                e.printStackTrace();
                // 에러 처리 로직 추가
            }
        }
    }
    // 사용자 정보를 데이터베이스에 저장하는 로직
    public void saveImage(User user) {
        userRepository.save(user); 
    }
}
