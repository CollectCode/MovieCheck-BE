package com.movie.moviecheck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movie.moviecheck.embedded.UserGenreId;
import com.movie.moviecheck.model.UserGenre;

public interface UserGenreRepository extends JpaRepository<UserGenre,UserGenreId>  {
    boolean existsByUser_UserKey(Integer userKey);
    void deleteByUser_UserKey(Integer userKey);

    List<UserGenre> findByUser_UserKey(Integer userKey);
}
