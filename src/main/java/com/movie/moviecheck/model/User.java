package com.movie.moviecheck.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "users_table")
@NoArgsConstructor
@Getter
@Setter
public class User {

    // AtomicInteger를 사용하여 카운트번호 관리
    private static final AtomicInteger count = new AtomicInteger(0);

    @Id
    @Column(name = "user_key", nullable = false, length = 7)
    private String userKey;

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

    @Transient
    private int usergender;

    // 성별을 인자로 받아 userKey 생성
    public User(String userKey,String userEmail, String userPassword, String userName,
                int userGood,int userBad, String userGrade,String userContent,int usergender) {
        this.userKey = generateUserKey(usergender);
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userGood = userGood;
        this.userBad = userBad;
        this.userGrade = userGrade;
        this.userContent = userContent;
    }

    public User(String userEmail, String userPassword, String userName, int usergender) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.usergender = usergender;
    }

    public User(String userEmail, String userPassword){
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }


    // public User(String userKey ,String userEmail, String userPassword, String userName,
    //             int userGood,int userBad, String userGrade,String userContent, char usergender) {
    //     this.userKey = userKey;
    //     this.userEmail = userEmail;
    //     this.userPassword = userPassword;
    //     this.userName = userName;
    //     this.userGood = userGood;
    //     this.userBad = userBad;
    //     this.userGrade = userGrade;
    //     this.userContent = userContent;
    //     this.usergender = usergender;
    // }  

    // userKey 생성 메서드
    private String generateUserKey(int usergender) {
        int currentCount = count.incrementAndGet(); // 카운트번호 증가
        return usergender + String.format("%06d", currentCount); // 성별 + 6자리 카운트번호
    }
    
}
