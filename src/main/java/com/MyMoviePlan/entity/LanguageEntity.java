package com.MyMoviePlan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

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

//    @JsonIgnore
//    @ToString.Exclude
//    @EqualsAndHashCode.Exclude
//    @ManyToMany(targetEntity = MovieEntity.class)
//    @JoinTable(name = "movie_languages",
//            joinColumns = @JoinColumn(name = "language_id", unique = false),
//            inverseJoinColumns = @JoinColumn(name = "movie_id", unique = false))
//    private List<MovieEntity> movies;

    public LanguageEntity(String name) {
        this.name = name;
    }
}
