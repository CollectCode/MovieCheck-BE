package com.movie.moviecheck.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDto {

    private String actorKey;
    private String actorName;
    private String actorImage;
    private List<String> movieTitles;
    private List<String> moviePosters;

    public ActorDto (String actorKey, String actorName, String actorImage){
        this.actorKey = actorKey;
        this.actorName = actorName;
        this.actorImage = actorImage;
    }
}
