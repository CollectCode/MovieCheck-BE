package com.movie.moviecheck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.model.User;

public interface UserRepository extends JpaRepository<User,String>  {
    // Optional<User> findByUserEmail(String userEmail);
}
