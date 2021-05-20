package com.MyMoviePlan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    private Date start;

    @Temporal(TemporalType.DATE)
    private Date end;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(targetEntity = ShowEntity.class)
    @JoinColumn(name = "show_id")
    private ShowEntity show;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(targetEntity = MovieEntity.class)
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(targetEntity = BookingEntity.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinTable(name = "movie_show_bookings",
            joinColumns = @JoinColumn(name = "movie_show_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "booking_id", unique = false))
    private List<BookingEntity> bookings;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(targetEntity = PriceEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private PriceEntity price;

    public MovieShowsEntity(Date start, Date end, ShowEntity show, MovieEntity movie) {
        this.start = start;
        this.end = end;
        this.show = show;
        this.movie = movie;
    }

    public MovieShowsEntity setId(int id) {
        this.id = id;
        return this;
    }

    public MovieShowsEntity setStart(Date start) {
        this.start = start;
        return this;
    }

    public MovieShowsEntity setEnd(Date end) {
        this.end = end;
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
