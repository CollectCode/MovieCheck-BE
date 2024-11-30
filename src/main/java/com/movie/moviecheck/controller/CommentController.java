package com.movie.moviecheck.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movie.moviecheck.dto.CommentDto;
import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    // 특정 영화에 남긴 리뷰에 댓글을 다는 기능
    @PostMapping
    public ResponseEntity<CommentDto> goToAddComment(@RequestBody CommentDto commnetDto, HttpServletRequest request) {
        return commentService.addComment(commnetDto, request);
    }

    // 특정 리뷰에 남긴 댓글을 삭제하는 기능
    @DeleteMapping
    public ResponseEntity<CommentDto> goToDeleteComment(@RequestBody CommentDto commnetDto, HttpServletRequest request) {
        return commentService.deleteComment(commnetDto, request);
    }
}
