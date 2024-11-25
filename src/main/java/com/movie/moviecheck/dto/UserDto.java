package com.movie.moviecheck.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Integer userKey;       // 회원번호

    @Email       
    @NotNull
    @Size(min=17,max=32)
    private String userEmail;      // 회원 이메일

    @NotNull
    @Size(min=8,max=20)
    private String userPassword;   // 회원 비밀번호
    
    @NotNull
    @Size(min=3,max=20)
    private String userName;       // 사용자 이름 (닉네임)
    
    private String userContent;    // 한줄소개
    
    @NotNull
    private int userGender;
    private String userProfile;
    private String userGrade = "관람객";
    
    public UserDto(String userEmail, String userPassword, String userName, int userGender) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userGender = userGender;
    }

    
    public UserDto(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }


    // userDto.getUserKey(),
    // userDto.getUserEmail(),
    // userDto.getUserPassword(),
    // userDto.getUserName(), // 이름으로 변경
    // 0, // 초기 누적 좋아요
    // 0, // 초기 누적 싫어요
    // userDto.getUserGrade(),
    // userDto.getUserContent(),
    // userDto.getUserGender()
}
