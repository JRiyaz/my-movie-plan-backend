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
@Table(name = "languages")
public class LanguageEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @ToString.Exclude
    @ManyToMany(targetEntity = MovieEntity.class)
    @JoinTable(name = "movie_languages",
            joinColumns = @JoinColumn(name = "language_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "movie_id", unique = false))
    private List<MovieEntity> movies;

    public LanguageEntity(String name) {
        this.name = name;
    }

    public LanguageEntity(String name, List<MovieEntity> movies) {
        this.name = name;
        this.movies = movies;
    }
}
