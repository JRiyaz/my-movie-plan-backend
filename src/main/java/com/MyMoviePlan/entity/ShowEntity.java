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
@Table(name = "shows")
public class ShowEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50)
    private String name;

    @Column(name = "start_time",length = 50)
    private String startTime;

    @ToString.Exclude
    @ManyToOne(targetEntity = AuditoriumEntity.class)
    @JoinColumn(name = "auditorium_id")
    private AuditoriumEntity auditorium;

    @ToString.Exclude
    @OneToMany(targetEntity = MovieShowsEntity.class, cascade = CascadeType.ALL, mappedBy = "show")
    private List<MovieShowsEntity> movieShows;

    @ToString.Exclude
    @OneToMany(targetEntity = BookingEntity.class, cascade = CascadeType.ALL, mappedBy = "show")
    private List<BookingEntity> bookings;

    @ToString.Exclude
    @OneToOne(targetEntity = PriceEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "price_id")
    private PriceEntity price;

    public ShowEntity(String name, String startTime, AuditoriumEntity auditorium,
                      List<MovieShowsEntity> movieShows, List<BookingEntity> bookings, PriceEntity price) {
        this.name = name;
        this.startTime = startTime;
        this.auditorium = auditorium;
        this.movieShows = movieShows;
        this.bookings = bookings;
        this.price = price;
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

    public ShowEntity setBookings(List<BookingEntity> bookings) {
        this.bookings = bookings;
        return this;
    }

    public ShowEntity setPrice(PriceEntity price) {
        this.price = price;
        return this;
    }
}
