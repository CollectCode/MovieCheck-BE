package com.movie.moviecheck.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException; // User 모델 클래스 필요
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap; // UserRepository 필요
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.movie.moviecheck.controller.WrapperClass;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.password.PasswordUtils;
import com.movie.moviecheck.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository; // UserRepository 주입
    private final UserConvertor userConvertor;

    private static Map<String, User> userDatabase = new HashMap<>(); // 이메일로 사용자 관리
    private static Map<String, User> nameDatabase = new HashMap<>(); // 닉네임으로 사용자 관리

    // 로그인 로직
    public ResponseEntity<WrapperClass<UserDto>> login(
            UserDto userDto, HttpServletRequest request, HttpServletResponse response) {
        String msg = "";
        User user = userConvertor.convertToEntity(userDto);

        // 이메일 존재 여부 확인
        if (!isEmailExists(user.getUserEmail())) {
            msg = "등록되지 않은 이메일입니다.";
            return ResponseEntity // 404 : 요청한 리소스를 찾을 수 없을 때
                    .status(HttpStatus.NOT_FOUND)
                    .body(new WrapperClass<>(userDto, msg)); // 이메일이 존재하지 않을 경우 에러 메시지 반환
        }

        // 입력한 비밀번호 해싱
        String hashedInputPassword = PasswordUtils.hashPassword(user.getUserPassword());
        // 사용자 정보 검증
        User authenticatedUser = findByEmailAndPassword(user.getUserEmail(), hashedInputPassword);

        // 검증된 정보 바탕으로 세션 생성
        if (authenticatedUser != null) {
            // 로그인 성공 시 세션 생성
            HttpSession session = request.getSession(true); // 세션이 없으면 새로 생성
            session.setAttribute("userKey", authenticatedUser.getUserKey()); // userKey 저장
            // 세션 ID를 메모리에 저장
            saveSession(authenticatedUser.getUserKey(), session.getId());
            // 클라이언트에 세션 ID를 쿠키로 전달
            Cookie sessionCookie = new Cookie("SESSIONID", session.getId());
            sessionCookie.setHttpOnly(false);
            sessionCookie.setPath("/");
            sessionCookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (예: 1시간)
            response.addCookie(sessionCookie);
            msg = "로그인 성공";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(userDto, msg)); // 로그인 성공 메시지 반환
        } else {
            msg = "비밀번호가 일치하지 않습니다.";
            return ResponseEntity// 401 : 인증이 필요한 요청이지만, 인증 정보가 없거나 잘못되었을 때
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new WrapperClass<>(userDto, msg)); // 로그인 실패 메시지 반환
        }
    }

    // 회원가입 로직
    public ResponseEntity<WrapperClass<UserDto>> createUser(UserDto userDto) {
        String msg = "회원가입 성공";
        User user = userConvertor.convertToEntity(userDto);

        // 비밀번호 해싱
        String hashedPassword = PasswordUtils.hashPassword(user.getUserPassword());
        user.setUserPassword(hashedPassword); // 해싱된 비밀번호로 설정

        // 유저저장
        User savedUser = saveUser(user);

        // DB에 저장이 안되었을때
        if (savedUser == null) {
            msg = "사용자 저장에 실패했습니다. 다시 시도해주세요.";
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new WrapperClass<>(null, msg));
        }

        // 회원가입이 성공적으로 되었을때
        UserDto sendUser = userConvertor.convertToDto(savedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new WrapperClass<>(sendUser, msg));
    }

    // 이메일 중복체크 로직
    public ResponseEntity<WrapperClass<UserDto>> emailCheck(UserDto userDto) {
        String msg = "이미 가입되어있는 이메일 입니다.";
        if (!isEmailExists(userDto.getUserEmail())) {
            msg = "사용가능한 이메일 입니다.";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(userDto, msg));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 : 요청이 현재 서버 상태와 충돌할 때
                    .body(new WrapperClass<>(userDto, msg)); // 이메일이 존재 할 때
        }
    }

    // 닉네임 중복체크 로직
    public ResponseEntity<WrapperClass<UserDto>> isNameExist(UserDto userDto) {
        String msg = "중복된 닉네임 입니다.";
        if (!isNameExists(userDto.getUserName())) {
            msg = "사용 가능한 닉네임 입니다.";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(userDto, msg));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 : 요청이 현재 서버 상태와 충돌할 때
                    .body(new WrapperClass<>(userDto, msg)); // 닉네임이 존재 할 때
        }
    }

    // 마이페이지를 가져오는 로직
    public ResponseEntity<WrapperClass<UserDto>> getMyPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션을 가져옴
        String msg = "";
        if (session != null) {
            Integer userKey = (Integer) session.getAttribute("userKey"); // 세션에서 userKey 가져오기
            UserDto user = userConvertor.convertToDto(findByKey(userKey));
            if (userKey != null) {
                // 서버 메모리에서 세션 ID 확인 (필요시)
                String sessionId = getSession(userKey);
                // 사용자 정보 가져오기
                msg = "마이페이지 로딩성공.";
                return ResponseEntity.ok(new WrapperClass<>(user, msg)); // 사용자 정보를 메시지와 함께 반환
            }
        }
        msg = "세션이 유효하지 않습니다."; // 403 : 서버가 요청을 이해했지만, 접근을 거부했을 때
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new WrapperClass<>(null, msg)); // 세션이 없거나 유효하지 않으면 401
                                                                                                // 반환
    }

    // 로그아웃을 진행하는 로직
    public ResponseEntity<WrapperClass<UserDto>> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        UserDto userDto = userConvertor.convertToDto(findByKey(userKey));
        String msg = "로그아웃 성공";
        if (session != null) {
            session.invalidate();
        }
        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setHttpOnly(false);
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
        return ResponseEntity.status(HttpStatus.OK).body(new WrapperClass<>(userDto, msg));
    }

    // 회원 탈퇴 로직
    public ResponseEntity<WrapperClass<UserDto>> deleteUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Integer userKey = (Integer) session.getAttribute("userKey");
        UserDto userDto = userConvertor.convertToDto(findByKey(userKey));
        User user = userConvertor.convertToEntity(userDto);
        String msg;

        if (deleteUser(user)) {
            msg = "회원탈퇴에 성공했습니다.";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(userDto, msg));
        } else {
            msg = "유저가 발견되지 않았습니다.";
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new WrapperClass<>(userDto, msg));
        }
    }

    // 마이페이지 수정 로직
    public ResponseEntity<WrapperClass<UserDto>> updateUser(HttpServletRequest request, UserDto userDto) {
        HttpSession session = request.getSession(false);
        HttpStatus status = HttpStatus.NOT_FOUND;
        Integer userKey = (Integer) session.getAttribute("userKey");
        String msg = "마이페이지 변경 완료";
        try {
            User user = findByKey(userKey);
            UserDto updatedUser = null;
            if (user == null) {
                msg = "사용자를 찾을 수 없습니다.";
                return ResponseEntity
                        .status(status)
                        .body(new WrapperClass<>(userDto, msg));
            }
            // 이름 및 소개 업데이트
            else if ((userDto.getUserName() != null)) {
                user.setUserName(userDto.getUserName());
                user.setUserContent(userDto.getUserContent());
            }
            // 업데이트 후 저장
            user = saveUser(user);
            updatedUser = userConvertor.convertToDto(user);
            status = HttpStatus.OK;
            return ResponseEntity
                    .status(status)
                    .body(new WrapperClass<>(updatedUser, msg));
        } catch (Exception exception) {
            msg = exception.getMessage();
            status = HttpStatus.BAD_REQUEST; // 400 : 서버가 요청을 이해할 수 없을 때
            return ResponseEntity
                    .status(status)
                    .body(new WrapperClass<>(userDto, msg));
        }
    }

    // 업로드 이미지 세팅로직
    public ResponseEntity<WrapperClass<UserDto>> uploadImage(HttpServletRequest request, MultipartFile userImage) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        String msg = "업로드 실패";
        User user = findByKey(userKey);
        UserDto userDto = null;
        if (user != null) {
            if (userImage != null && !userImage.isEmpty()) {
                String uploadDir = new File("src/main/resources/static/images/users/").getAbsolutePath(); // 절대 경로로 변경
                String fileName = "user_" + user.getUserKey() + ".png"; // UUID 추가
                File destinationFile = new File(uploadDir, fileName);
                try {
                    userImage.transferTo(destinationFile);
                    user.setUserProfile("/images/users/" + fileName);
                    saveUser(user);
                    msg = "이미지 업로드 성공";
                    userDto = userConvertor.convertToDto(user);
                    return ResponseEntity.status(HttpStatus.OK)
                                        .body(new WrapperClass<>(userDto, msg));
                } catch (IOException e) {
                    e.printStackTrace();
                    msg = "파일 저장 중 오류 발생";
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(new WrapperClass<>(userDto, msg));
                }
            } else {
                msg = "이미지가 존재하지 않거나 비어 있습니다.";
                userDto = userConvertor.convertToDto(user);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(new WrapperClass<>(userDto, msg));
            }
        } else {
            msg = "사용자가 존재하지 않습니다.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new WrapperClass<>(userDto, msg));
        }
    }

    // 이미지를 반환하는 메서드
    public ResponseEntity<WrapperClass<String>> getUserImage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(401)
                    .body(new WrapperClass<>(null, "세션이 없습니다."));
        }

        Integer userKey = (Integer) session.getAttribute("userKey");
        if (userKey == null) {
            return ResponseEntity.status(401)
                    .body(new WrapperClass<>(null, "유효한 사용자 키가 없습니다."));
        }

        // 사용자 조회
        User user = findByKey(userKey);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(new WrapperClass<>(null, "사용자 또는 프로필 이미지가 없습니다."));
        }

        // 이미지 파일 경로
        String imagePath = new File("src/main/resources/static" + user.getUserProfile()).getAbsolutePath();
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            return ResponseEntity.status(404)
                    .body(new WrapperClass<>(null, "이미지 파일이 존재하지 않습니다."));
        }

        try {
            // 이미지 파일을 Base64로 변환
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            return ResponseEntity.ok()
                    .body(new WrapperClass<>(base64Image, "이미지 변환 성공"));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new WrapperClass<>(null, "이미지 처리 중 오류가 발생했습니다."));
        }
    }

    // 회원 생성 및 갱신
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // 회원 삭제
    public boolean deleteUser(User user) {
        if (userRepository.existsById(user.getUserKey())) {
            userRepository.deleteById(user.getUserKey());
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
            return saveUser(user);
        }
        return null;
    }

    // 비밀번호 변경
    public User updatePassword(Integer userKey, String newPassword) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserPassword(newPassword);
            return saveUser(user);
        }
        return null;
    }

    // 한줄소개 변경
    public User updateContent(Integer userKey, String newContent) {
        Optional<User> userOptional = userRepository.findById(userKey);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUserContent(newContent);
            return saveUser(user);
        }
        return null;
    }

    // 이메일이 이미 존재하는가?
    public boolean isEmailExists(String userEmail) {
        return userRepository.existsByUserEmail(userEmail);
    }

    // 닉네임이 이미 존재하는가?
    public boolean isNameExists(String userName) {
        return userRepository.existsByUserName(userName);
    }

    // 이메일과 비밀번호로써 user검색
    public User findByEmailAndPassword(String userEmail, String userPassword) {
        return userRepository.findByUserEmailAndUserPassword(userEmail, userPassword);
    }

    // 이메일로써 user검색
    public User findByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail);
    }

    // 사용자 정보를 가져오는 메소드
    public User findByKey(Integer userKey) {
        return userRepository.findByUserKey(userKey);
    }

    // 사용자 정보를 데이터베이스에 저장하는 로직
    public void saveImage(User user) {
        userRepository.save(user);
    }
    
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
}