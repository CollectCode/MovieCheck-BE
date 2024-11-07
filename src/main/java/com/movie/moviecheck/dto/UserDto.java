package com.movie.moviecheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userKey;       // 회원번호
    private String userEmail;     // 회원 이메일
    private String userPassword;   // 회원 비밀번호
    private String name;           // 사용자 이름 (닉네임)
    private int userGood;          // 누적 좋아요
    private int userBad;           // 누적 싫어요
    private String userGrade;      // 등급
    private String userContent;     // 한줄소개
    private char gender;
}
