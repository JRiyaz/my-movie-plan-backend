package com.MyMoviePlan.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "actors")
public class ActorEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String role;

    @Column(length = 1000)
    private String image;

    @ToString.Exclude
    @ManyToOne(targetEntity = MovieEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public ActorEntity(String name, String role, String image) {
        this.name = name;
        this.role = role;
        this.image = image;
    }

    public ActorEntity(String name, String role, String image, MovieEntity movie) {
        this.name = name;
        this.role = role;
        this.image = image;
        this.movie = movie;
    }
}
