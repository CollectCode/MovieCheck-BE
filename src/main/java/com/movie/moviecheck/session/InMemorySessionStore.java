package com.movie.moviecheck.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.movie.moviecheck.model.User;

public class InMemorySessionStore implements SessionStore {
    private final Map<String, User> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(String sessionId, User user) {
        sessions.put(sessionId, user);  // 세션 ID와 사용자 정보를 메모리에 저장
    }

    @Override
    public User findBySessionId(String sessionId) {
        return sessions.get(sessionId);  // 세션 ID로 사용자 정보를 조회
    }

    @Override
    public void delete(String sessionId) {
        sessions.remove(sessionId);  // 로그아웃 시 세션 삭제
    }
}