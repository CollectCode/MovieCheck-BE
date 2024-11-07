package com.movie.moviecheck.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {
    private String userEmail;    // 사용자 이메일
    private String userPassword; // 사용자 비밀번호
    private String userName;     // 사용자 이름
}