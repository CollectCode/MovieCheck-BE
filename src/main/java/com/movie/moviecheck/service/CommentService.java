package com.movie.moviecheck.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.movie.moviecheck.converter.CommentConvertor;
import com.movie.moviecheck.dto.CommentDto;
import com.movie.moviecheck.dto.ReviewDto;
import com.movie.moviecheck.model.Comment;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.CommentRepository;
import com.movie.moviecheck.repository.ReviewRepository;
import com.mysql.cj.protocol.x.Ok;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final CommentConvertor commentConvertor;

    // 댓글을 추가하는 메서드
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto, HttpServletRequest request) {
        // 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userKey") == null) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }
        Integer userKey = (Integer) session.getAttribute("userKey");
        // 리뷰 조회
        Review review = reviewRepository.findByReviewKey(commentDto.getReviewKey());
        if (review == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
        // 사용자 조회
        User user = userService.findByKey(userKey);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(null);
        }
        // 기존에 있던 댓글을 확인
        if (commentDto.getCommentKey() != null) {

            // commentKey가 존재하면 해당 댓글 수정
            Comment existingComment = commentRepository.findById(commentDto.getCommentKey())
                    .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. commentKey: " + commentDto.getCommentKey()));
    
            // 댓글 수정 권한 확인 (작성자 확인)
            if (!existingComment.getUser().getUserKey().equals(userKey)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            // 댓글 내용 업데이트
            existingComment.setCommentContent(commentDto.getCommentContent());
            existingComment.setCommentTime(LocalDateTime.now());
            commentRepository.save(existingComment);
            CommentDto responseDto = commentConvertor.convertToDto(existingComment);
            return ResponseEntity.ok(responseDto);
        } else {
            // Comment 엔티티 생성 및 저장
            Comment comment = Comment.builder()
                    .review(review)
                    .user(user)
                    .commentContent(commentDto.getCommentContent())
                    .commentTime(LocalDateTime.now())
                    .build();
            
            commentRepository.save(comment);
            CommentDto responseDto = commentConvertor.convertToDto(comment);
            return ResponseEntity.ok(responseDto);
        }
    }
    // 댓글을 삭제하는 메서드
    public ResponseEntity<CommentDto> deleteComment(CommentDto commentDto, HttpServletRequest request) {
        // 1. 세션에서 userKey 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userKey") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Integer userKey = (Integer) session.getAttribute("userKey");

        // 2. 댓글 조회
        Comment comment = commentRepository.findById(commentDto.getCommentKey())
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다. commentKey: " + commentDto.getCommentKey()));

        // 3. 댓글 작성자 확인
        if (!comment.getUser().getUserKey().equals(userKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // 4. 댓글 삭제
        commentRepository.delete(comment);
        CommentDto responseDto = commentConvertor.convertToDto(comment);
            return ResponseEntity.ok(responseDto);
    }

    // 리뷰키를 통해 댓글을 조회
    public List<Comment> getCommentsByReviewKey(Integer reviewKey)   {
        return commentRepository.findByReview_ReviewKey(reviewKey);
    }
}
