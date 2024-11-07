package com.movie.moviecheck.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Column(name = "actor_hometown", length = 60)
    private String actorHometown;

    @Column(name = "actor_image", length = 255)
    private String actorImage;
}
