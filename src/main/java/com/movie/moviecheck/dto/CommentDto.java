package com.movie.moviecheck.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Integer commentKey;
    private Integer userKey;
    private Integer reviewKey;
    private String commentContent;
    private LocalDateTime commenTime;
    
}
