package com.movie.moviecheck.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.User;

public interface UserRepository extends JpaRepository<User,Integer>  {
    // Optional<User> findByUserEmail(String userEmail);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserName(String userName);
    
    User findByUserEmailAndUserPassword(String userEmail, String userPassword);
    User findByUserEmail(String userEmail);
    User findByUserKey(Integer userKey);
}
