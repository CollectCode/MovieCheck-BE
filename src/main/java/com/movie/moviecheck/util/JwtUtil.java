// package com.movie.moviecheck.util;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Component;

// import javax.crypto.SecretKey;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.Map;

// @Component
// public class JwtUtil {

//     private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("your_secret_key_your_secret_key_your_secret_key_".getBytes()); // 비밀 키 (32바이트 이상)
//     private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

//     // 토큰 생성
//     public String generateToken(String email) {
//         Map<String, Object> claims = new HashMap<>();
//         return createToken(claims, email);
//     }

//     // 토큰 생성 메서드
//     private String createToken(Map<String, Object> claims, String subject) {
//         return Jwts.builder()
//                 .setClaims(claims)
//                 .setSubject(subject)
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                 .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     // 토큰 검증
//     public boolean validateToken(String token, String email) {
//         final String username = extractUsername(token);
//         return (username.equals(email) && !isTokenExpired(token));
//     }

//     // 이메일 추출
//     public String extractUsername(String token) {
//         return extractAllClaims(token).getSubject();
//     }

//     // 만료 여부 확인
//     private boolean isTokenExpired(String token) {
//         return extractAllClaims(token).getExpiration().before(new Date());
//     }

//     // 모든 클레임 추출
//     private Claims extractAllClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(SECRET_KEY)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }
// }
