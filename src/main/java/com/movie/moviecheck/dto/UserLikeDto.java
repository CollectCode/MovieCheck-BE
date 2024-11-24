package com.movie.moviecheck.dto;

import com.movie.moviecheck.embedded.ReviewId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeDto {

    private Integer userKey;
    private ReviewId reviewId;
}
