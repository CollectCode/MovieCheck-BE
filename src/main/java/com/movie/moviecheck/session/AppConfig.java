package com.movie.moviecheck.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public SessionStore sessionStore() {
        return new InMemorySessionStore();  // 인메모리 세션 저장소 빈 등록
    }
}