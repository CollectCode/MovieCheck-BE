package com.movie.moviecheck.session;

import com.movie.moviecheck.model.User;

public interface SessionStore {
    void save(String sessionId, User user);  // 세션 ID와 사용자 정보를 저장

    User findBySessionId(String sessionId);  // 세션 ID로 사용자 정보를 조회
    
    void delete(String sessionId);           // 로그아웃 시 세션 삭제
}