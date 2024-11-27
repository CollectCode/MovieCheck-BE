package com.movie.moviecheck.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.movie.moviecheck.dto.CommentDto;
import com.movie.moviecheck.model.Comment;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.CommentRepository;
import com.movie.moviecheck.repository.ReviewRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    // 댓글을 추가하는 메서드
    public void addComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        // 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userKey") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Integer userKey = (Integer) session.getAttribute("userKey");
        // 리뷰 조회
        Review review = reviewRepository.findByReviewKey(commentDto.getReviewKey());
        if (review == null) {
            throw new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. reviewKey: " + commentDto.getReviewKey());
        }
        // 사용자 조회
        User user = userService.findByKey(userKey);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. userKey: " + userKey);
        }
        // 기존에 있던 댓글을 확인
        if (commentDto.getCommentKey() != null) {

            // commentKey가 존재하면 해당 댓글 수정
            Comment existingComment = commentRepository.findById(commentDto.getCommentKey())
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. commentKey: " + commentDto.getCommentKey()));
    
            // 댓글 수정 권한 확인 (작성자 확인)
            if (!existingComment.getUser().getUserKey().equals(userKey)) {
                throw new IllegalStateException("수정 권한이 없습니다.");
            }
            // 댓글 내용 업데이트
            existingComment.setCommentContent(commentDto.getCommentContent());
            existingComment.setCommentTime(LocalDateTime.now());
            commentRepository.save(existingComment);
        } else {
            // Comment 엔티티 생성 및 저장
            Comment comment = Comment.builder()
                    .review(review)
                    .user(user)
                    .commentContent(commentDto.getCommentContent())
                    .commentTime(LocalDateTime.now())
                    .build();
    
            commentRepository.save(comment);
        }
    }
    
    // 댓글을 삭제하는 메서드
    public void deleteComment(CommentDto commentDto, HttpServletRequest request) {
        // 1. 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userKey") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Integer userKey = (Integer) session.getAttribute("userKey");

        // 2. 댓글 조회
        Comment comment = commentRepository.findById(commentDto.getCommentKey())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. commentKey: " + commentDto.getCommentKey()));

        // 3. 댓글 작성자 확인
        if (!comment.getUser().getUserKey().equals(userKey)) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }

        // 4. 댓글 삭제
        commentRepository.delete(comment);
    }
}
