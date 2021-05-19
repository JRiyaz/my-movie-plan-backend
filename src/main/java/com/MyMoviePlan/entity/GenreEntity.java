package com.MyMoviePlan.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "genres")
public class GenreEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @ToString.Exclude
    @ManyToMany(targetEntity = MovieEntity.class)
    @JoinTable(name = "movie_genres",
            joinColumns = @JoinColumn(name = "genre_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "movie_id", unique = false))
    private List<MovieEntity> movies;

    public GenreEntity(String name) {
        this.name = name;
    }

    public GenreEntity(String name, List<MovieEntity> movies) {
        this.name = name;
        this.movies = movies;
    }
}
