package com.movie.moviecheck.converter;

import com.movie.moviecheck.dto.ActorDto;
import com.movie.moviecheck.model.Actor;
import com.movie.moviecheck.model.Movie;
import com.movie.moviecheck.model.MovieActor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ActorConvertor {

    // Actor Entity를 ActorDto로 변환
    public ActorDto convertToDto(Actor actor) {
        if (actor == null) {
            return null;
        }

        return ActorDto.builder()
                .actorKey(actor.getActorKey())
                .actorName(actor.getActorName())
                .actorImage(actor.getActorImage())
                .movieTitles(actor.getMovieActor() != null ?
                    actor.getMovieActor().stream()
                        .map(MovieActor::getMovie)
                        .map(Movie::getMovieTitle)
                        .collect(Collectors.toList())
                    : null)
                .moviePosters(actor.getMovieActor() != null ?
                    actor.getMovieActor().stream()
                        .map(MovieActor::getMovie)
                        .map(Movie::getMoviePoster)
                        .collect(Collectors.toList())
                    : null)
                .build();
    }

    // ActorDto를 Actor Entity로 변환
    public Actor convertToEntity(ActorDto actorDto) {
        if (actorDto == null) {
            return null;
        }

        return new Actor(
                actorDto.getActorKey(),
                actorDto.getActorName(),
                actorDto.getActorImage(),
                actorDto.getActorBirthplace(),
                actorDto.getActorBirthday(),
                null // MovieActor는 DTO에서 변환하지 않음
        );
    }
}
