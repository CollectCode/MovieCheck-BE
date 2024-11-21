package com.movie.moviecheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.embedded.UserGenreId;
import com.movie.moviecheck.model.UserGenre;

public interface UserGenreRepository extends JpaRepository<UserGenre,UserGenreId>  {
    // userkey 존재하는지 확인
    boolean existsByUser_UserKey(Integer userKey);
    
    // 선호장르 삭제할 때 사용
    void deleteByUser_UserKey(Integer userKey);

    // userkey 찾아오기
    List<UserGenre> findByUser_UserKey(Integer userKey);
}
