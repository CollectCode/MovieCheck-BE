package com.movie.moviecheck.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "actor_table")
public class Actor {

    @Id
    @Column(name = "actor_key", nullable = false, length = 7)
    private String actorKey;

    @Column(name = "actor_name", nullable = false, length = 50)
    private String actorName;

    @Column(name = "actor_image", length = 255)
    private String actorImage;

    // actor_birth
    @Column(name = "actor_birthplace", length = 100)
    private String actorBirthplace;

    @Column(name = "actor_birthday", nullable = true)   
    private LocalDate actorBirthday;

    @Column(name = "actor_deathday", nullable = true)   
    private LocalDate actorDeathday;

    @OneToMany(mappedBy = "actor")
    private List<MovieActor> movieActor;
}
