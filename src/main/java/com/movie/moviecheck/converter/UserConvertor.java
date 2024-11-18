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
        return new User(
            userDto.getUserKey(),
            userDto.getUserEmail(),
            userDto.getUserPassword(),
            userDto.getUserName(),
            0, // 초기 누적 좋아요
            0, // 초기 누적 싫어요
            userDto.getUserContent(),
            userDto.getUserGender(),
            userDto.getUserProfile()
        );
    }

    // User를 UserDto로 변환하는 메서드
    public UserDto convertToDto(User user) {
        if (user == null) {
            return null; // null 체크
        }
        return new UserDto(
            user.getUserKey(),
            user.getUserEmail(),
            user.getUserPassword(),
            user.getUserName(),
            user.getUserGood(),
            user.getUserBad(),
            user.getUserContent(),
            user.getUserGender(),
            user.getUserProfile(),
            user.getUserGrade()
        );
    }
}

