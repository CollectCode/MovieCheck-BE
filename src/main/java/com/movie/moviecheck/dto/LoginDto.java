package com.movie.moviecheck.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginDto {
    private String userEmail;    // 사용자 이메일
    private String userPassword; // 사용자 비밀번호
}