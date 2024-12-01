package com.movie.moviecheck.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "users_table")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userKey;

    @Column(name = "user_email", nullable = false, length = 30)
    private String userEmail;

    @Column(name = "user_passwd", nullable = false, length = 64)
    private String userPassword;

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Column(name = "user_grade", nullable = false, length = 8)
    private String userGrade = "관람객";

    @Column(name = "user_content", length = 100)
    private String userContent = "내용을 입력해주세요";

    @Column(name = "user_gender", nullable = false)
    private int userGender;

    @Column(name = "user_profile", length = 255) // 이미지 경로
    private String userProfile;

    // 누적 좋아요
    @Column(name = "user_like_count")
    private int userLikeCount;

    @OneToMany(mappedBy="user", cascade=CascadeType.REMOVE)
    @JsonManagedReference
    private List<UserGenre> userGenre;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<UserLike> userLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews;


    @Transient
    private String sessionId;

    // 성별을 인자로 받아 userKey 생성
    public User(Integer userKey,String userEmail, String userPassword, String userName,
                String userContent,int userGender, String userProfile) {
        this.userKey = userKey;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userContent = userContent;
        this.userGender = userGender;
        this.userProfile = userProfile;
    }

    
    public User(String userEmail, String userPassword, String userName, int userGender) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userGender = userGender;
    }

    // 로그인
    public User(String userEmail, String userPassword){
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    // 이미지 업로드
    public User(String userProfile){
        this.userProfile = userProfile;
    }
}
