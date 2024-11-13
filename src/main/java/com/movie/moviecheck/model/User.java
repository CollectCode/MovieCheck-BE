package com.movie.moviecheck.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "users_table")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userKey;

    @Column(name = "user_email", nullable = false, length = 30)
    private String userEmail;

    @Column(name = "user_passwd", nullable = false, length = 20)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Column(name = "user_good", nullable = false)
    private int userGood = 0; // 기본값 0

    @Column(name = "user_bad", nullable = false)
    private int userBad = 0; // 기본값 0

    @Column(name = "user_grade", nullable = false, length = 8)
    private String userGrade = "관람객";

    @Column(name = "user_content", length = 100)
    private String userContent = "내용을 입력해주세요";

    @Column(name = "user_gender", nullable = false)
    private int userGender;

    @Transient
    private String sessionId;

    // 성별을 인자로 받아 userKey 생성
    public User(Integer userKey,String userEmail, String userPassword, String userName,
                int userGood,int userBad,String userContent,int userGender) {
        this.userKey = userKey;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userGood = userGood;
        this.userBad = userBad;
        this.userContent = userContent;
        this.userGender = userGender;
    }

    public User(String userEmail, String userPassword, String userName, int userGender) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userGender = userGender;
    }

    public User(String userEmail, String userPassword){
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
}
