package com.MyMoviePlan.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "movie_shows")
public class MovieShowsEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date from;

    @Temporal(TemporalType.DATE)
    private Date to;

    @ToString.Exclude
    @ManyToOne(targetEntity = ShowEntity.class)
    @JoinColumn(name = "show_id")
    private ShowEntity show;

    @ToString.Exclude
    @OneToOne(targetEntity = MovieEntity.class)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    public MovieShowsEntity(Date from, Date to, ShowEntity show, MovieEntity movie) {
        this.from = from;
        this.to = to;
        this.show = show;
        this.movie = movie;
    }

    public MovieShowsEntity setId(int id) {
        this.id = id;
        return this;
    }

    public MovieShowsEntity setFrom(Date from) {
        this.from = from;
        return this;
    }

    public MovieShowsEntity setTo(Date to) {
        this.to = to;
        return this;
    }

    public MovieShowsEntity setShow(ShowEntity show) {
        this.show = show;
        return this;
    }

    public MovieShowsEntity setMovie(MovieEntity movie) {
        this.movie = movie;
        return this;
    }
}
