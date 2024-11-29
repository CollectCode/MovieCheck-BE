package com.movie.moviecheck.service;

import java.io.File;
import java.io.IOException; // User 모델 클래스 필요
import java.nio.file.Files;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
            
            // userKey를 쿠키에 추가
            Cookie userKeyCookie = new Cookie("userKey", String.valueOf(authenticatedUser.getUserKey()));
            userKeyCookie.setHttpOnly(false); // 프론트엔드에서 접근 가능
            userKeyCookie.setPath("/");
            userKeyCookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (예: 1시간)
            response.addCookie(userKeyCookie);
            
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

        if(userDto.getUserPassword().length() < 8 || userDto.getUserPassword().length() > 21){
            msg = "비밀번호의 길이는 8글자 ~ 20글자로 해주세요";
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new WrapperClass<>(null, msg));
        }
        User user = userConvertor.convertToEntity(userDto);
        
        // 비밀번호 해싱
        String hashedPassword = PasswordUtils.hashPassword(user.getUserPassword());
        user.setUserPassword(hashedPassword); // 해싱된 비밀번호로 설정

        // 기본 프로필 이미지로 설정
        user.setUserProfile("http://localhost:8080/images/users/default.PNG");
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
    public ResponseEntity<WrapperClass<String>> emailCheck(String userEmail) {
        String msg = "이미 가입되어있는 이메일 입니다.";
        // 이메일의 형식 검사
        if(!isValidEmail(userEmail)){
            msg = "올바르지않은 이메일 형식입니다.";
            return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new WrapperClass<>(userEmail,msg));
        }
        // 이메일의 길이 검사
        if (userEmail.length() < 15 || userEmail.length() > 32) {
            msg = "이메일의 길이는 5글자 ~ 20글자로 해주세요.";
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new WrapperClass<>(null, msg));
        }
        // 이메일이 존재하는지 검사
        if (!isEmailExists(userEmail)) {
                msg = "사용가능한 이메일 입니다.";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(userEmail, msg));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 : 요청이 현재 서버 상태와 충돌할 때
                    .body(new WrapperClass<>(userEmail, msg)); // 이메일이 존재 할 때
        }
    }

    // 이메일 형식 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    // 이메일 자음 모음 검사 메서드
    private boolean isValidUserName(String userName) {
        // 한글 자음, 모음만 포함된 문자열을 차단하는 정규식
        String koreanVowelOrConsonantOnlyRegex = "^[ㄱ-ㅎㅏ-ㅣ]*$";
        // 닉네임이 null이 아니고, 자음/모음만으로 구성되지 않았는지 확인
        return userName != null && !userName.matches(koreanVowelOrConsonantOnlyRegex);
    }


    // 닉네임 중복체크 로직
    public ResponseEntity<WrapperClass<String>> isNameExist(String userName) {
        String msg = "사용 불가능한 닉네임 입니다.";
        // 닉네임의 자음 모음 검사
        if (!isValidUserName(userName)) {
            msg = "닉네임에 한글 자음 또는 모음만 사용할 수 없습니다.";
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new WrapperClass<>(null, msg));
        }
       // 닉네임의 길이 검사
        if (userName.length() < 3 || userName.length() > 15) {
            msg = "닉네임의 길이는 3글자 ~ 15글자로 해주세요.";
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new WrapperClass<>(null, msg));
        }
        // 닉네임이 존재하는지 검사
        if (!isNameExists(userName)) {
            msg = "사용 가능한 닉네임 입니다.";
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new WrapperClass<>(userName, msg));
        } else {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT) // 409 : 요청이 현재 서버 상태와 충돌할 때
                    .body(new WrapperClass<>(userName, msg)); // 닉네임이 존재 할 때
        }
    }

    // 마이페이지를 가져오는 로직
    public ResponseEntity<WrapperClass<UserDto>> getMyPage(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 기존 세션을 가져옴
        String msg = "";
        if (session != null) {
            Integer userKey = (Integer) session.getAttribute("userKey"); // 세션에서 userKey 가져오기
            User user = findByKey(userKey);
            if (userKey != null && user != null) {
                // 서버 메모리에서 세션 ID 확인 (필요시)
                String sessionId = getSession(userKey);
    
                // 사용자 정보 가져오기
                UserDto userDto = userConvertor.convertToDto(user);
                if(userDto.getUserProfile()==null || userDto.getUserProfile().equals("")){
                    userDto.setUserProfile("http://localhost:8080/images/users/default.PNG");
                }
                msg = "마이페이지 로딩 성공.";
                return ResponseEntity.ok(new WrapperClass<>(userDto, msg)); // 사용자 정보를 메시지와 함께 반환
            }
        }
        msg = "세션이 유효하지 않습니다."; // 403 : 서버가 요청을 이해했지만, 접근을 거부했을 때
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new WrapperClass<>(null, msg)); // 세션이 없거나 유효하지 않으면 403 반환
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
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        response.addCookie(jsessionCookie);

        Cookie sessionCookie = new Cookie("SESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);

        // 로그아웃 후 리다이렉트
        response.setHeader("Location", "/");
        response.setStatus(HttpServletResponse.SC_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(new WrapperClass<>(userDto, msg));
    }

    // 회원 탈퇴 로직
    public ResponseEntity<WrapperClass<UserDto>> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer userKey = (Integer) session.getAttribute("userKey");
        UserDto userDto = userConvertor.convertToDto(findByKey(userKey));
        User user = userConvertor.convertToEntity(userDto);
        String msg;

        if (deleteUser(user)) {
            msg = "회원탈퇴에 성공했습니다.";
            
        if (session != null) {
            session.invalidate();
        }
        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        response.addCookie(jsessionCookie);

        Cookie sessionCookie = new Cookie("SESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
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
                    user.setUserProfile("http://localhost:8080/images/users/" + fileName);
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
    private String convertImageToBase64(String userProfilePath) {
        if (userProfilePath == null || userProfilePath.isEmpty()) {
            return null;
        }
        String imagePath = new File("src/main/resources/static" + userProfilePath).getAbsolutePath();
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            return null; // 이미지 파일이 존재하지 않는 경우
        }
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            return Base64.getEncoder().encodeToString(imageBytes); // Base64 인코딩
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 변환 중 오류 발생 시 null 반환
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