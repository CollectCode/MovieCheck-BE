package com.movie.moviecheck.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.movie.moviecheck.dto.CommentDto;
import com.movie.moviecheck.model.Comment;
import com.movie.moviecheck.model.Review;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.repository.ReviewRepository;
import com.movie.moviecheck.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentConvertor {

    private final UserService userService;
    private final ReviewRepository reviewRepository;


    public Comment converToEntity(CommentDto commentDto){
        if(commentDto == null){
            return null;
        }
        User user = userService.findByKey(commentDto.getUserKey());
        Review review = reviewRepository.findByReviewKey(commentDto.getReviewKey());
        Comment comment = new Comment();
        comment.setCommentKey(commentDto.getCommentKey());
        comment.setCommentContent(commentDto.getCommentContent());
        comment.setCommentTime(commentDto.getCommenTime());
        comment.setUser(user);
        comment.setReview(review);
        return comment;
    }
    

    public CommentDto convertToDto(Comment comment) {
        if (comment == null) {
            return null; // null 체크
        }
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentKey(comment.getCommentKey());
        commentDto.setCommentContent(comment.getCommentContent());
        commentDto.setCommenTime(comment.getCommentTime());
        commentDto.setUserKey(comment.getUser().getUserKey());
        commentDto.setReviewKey(comment.getReview().getReviewKey());
        return commentDto;
    }

}
