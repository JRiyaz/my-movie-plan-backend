package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.BookingEntity;
import com.MyMoviePlan.entity.MovieShowsEntity;
import com.MyMoviePlan.exception.BookingNotFoundException;
import com.MyMoviePlan.exception.MovieShowNotFoundException;
import com.MyMoviePlan.repository.MovieShowsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie-show")
@AllArgsConstructor
public class MovieShowController {

    private final MovieShowsRepository repository;

    @GetMapping("all")
    public List<MovieShowsEntity> findAllMovieShows() {
        return repository.findAll();
    }

    @GetMapping("{movie_show_id}")
    public MovieShowsEntity findMovieShowById(@PathVariable final int movie_show_id) {
        return repository.findById(movie_show_id)
                .orElseThrow(
                        () -> new MovieShowNotFoundException("Movie Show with id: " + movie_show_id + " not found")
                );
    }

    @DeleteMapping("delete/{movie_show_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteMovieShow(@PathVariable final int movie_show_id) {
        repository.deleteById(movie_show_id);
    }

    /*
     *   ============================= Booking Controller ==========================
     */

    @GetMapping("{movie_show_id}/booking/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public BookingEntity findBookingById(@PathVariable final int movie_show_id,
                                         @PathVariable final int booking_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(movie_show_id);
        return movieShow.getBookings()
                .stream().filter(booking -> booking.getId() == solveId(booking_id))
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: "
                        + actualId(booking_id) + " not found."));
    }

    @GetMapping("{movie_show_id}/booking/all")
    @PreAuthorize("hasAuthority('READ')")
    public List<BookingEntity> allBookings(@PathVariable final int movie_show_id) {
        return this.findMovieShowById(movie_show_id).getBookings();
    }

//    @PostMapping("{movie_show_id}/booking/add")
//    @PreAuthorize("hasAuthority('WRITE')")
//    public BookingEntity saveBooking(@PathVariable final int movie_show_id,
//                                     @RequestBody final BookingEntity booking) {
//        final ShowEntity show = this.findShowById(auditorium_id, show_id);
//        final MovieShowsEntity movieShow = this.getMovieShowEntity(show, movie_show_id);
//        movieShow.setShow(show)
//                .getBookings()
//                .add(booking);
//        this.movieShow.save(movieShow);
//        return booking;
//    }
//
//    @PutMapping("{movie_show_id}/booking/update")
//    @PreAuthorize("hasAuthority('UPDATE')")
//    public BookingEntity updateBooking(@PathVariable final int movie_show_id,
//                                       @RequestBody final BookingEntity booking) {
//        final int booking_id = booking.getId();
//        final ShowEntity show = this.findShowById(auditorium_id, show_id);
//        final MovieShowsEntity movieShow = this.getMovieShowEntity(show, movie_show_id);
//        movieShow.setShow(show)
//                .getBookings()
//                .remove(solveId(booking_id));
//        movieShow.getBookings()
//                .add(booking);
//        this.movieShow.save(movieShow);
//        return booking;
//    }

    /*
     *   ============================= Useful methods ==========================
     */

    /* In database primary key starts with 1, but in list index starts with 0.
    So to match the primary key with list index, I'm subtracting 1 from the path variable.*/
    private int solveId(final int id) {
        return id - 1;
    }

    private int actualId(final int id) {
        return id + 1;
    }
}
