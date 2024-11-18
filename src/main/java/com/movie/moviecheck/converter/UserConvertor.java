package com.movie.moviecheck.converter;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.model.User;

@Component
public class UserConvertor {

    // UserDto를 User로 변환하는 메서드
    public User convertToEntity(UserDto userDto) {
        if (userDto == null) {
            return null; // null 체크
        }
        return User.builder()
                   .userKey(userDto.getUserKey())
                   .userEmail(userDto.getUserEmail())
                   .userPassword(userDto.getUserPassword())
                   .userName(userDto.getUserName())
                   .userContent(userDto.getUserContent())
                   .userGender(userDto.getUserGender())
                   .userProfile(userDto.getUserProfile())
                   .userGrade(userDto.getUserGrade())
                   .build();
    }

    // User를 UserDto로 변환하는 메서드
    public UserDto convertToDto(User user) {
        if (user == null) {
            return null; // null 체크
        }
        return UserDto.builder()
                   .userKey(user.getUserKey())
                   .userEmail(user.getUserEmail())
                   .userPassword(user.getUserPassword())
                   .userName(user.getUserName())
                   .userContent(user.getUserContent())
                   .userGender(user.getUserGender())
                   .userProfile(user.getUserProfile())
                   .userGrade(user.getUserGrade())
                   .build();
    }
}

