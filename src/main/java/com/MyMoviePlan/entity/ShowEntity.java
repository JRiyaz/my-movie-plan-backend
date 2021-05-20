package com.MyMoviePlan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "shows")
public class ShowEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @Column(name = "start_time", length = 50)
    private String startTime;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "auditorium_id")
    @ManyToOne(targetEntity = AuditoriumEntity.class, cascade = CascadeType.ALL)
    private AuditoriumEntity auditorium;

    //    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(targetEntity = MovieShowsEntity.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "show")
    private List<MovieShowsEntity> movieShows;

    public ShowEntity(String name, String startTime, List<MovieShowsEntity> movieShows) {
        this.name = name;
        this.startTime = startTime;
        this.movieShows = movieShows;
    }

    public ShowEntity setId(int id) {
        this.id = id;
        return this;
    }

    public ShowEntity setName(String name) {
        this.name = name;
        return this;
    }

    public ShowEntity setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public ShowEntity setAuditorium(AuditoriumEntity auditorium) {
        this.auditorium = auditorium;
        return this;
    }

    public ShowEntity setMovieShows(List<MovieShowsEntity> movieShows) {
        this.movieShows = movieShows;
        return this;
    }
}
