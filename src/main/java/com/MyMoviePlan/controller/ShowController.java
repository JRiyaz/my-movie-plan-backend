package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.BookingEntity;
import com.MyMoviePlan.entity.MovieShowsEntity;
import com.MyMoviePlan.entity.ShowEntity;
import com.MyMoviePlan.exception.BookingNotFoundException;
import com.MyMoviePlan.exception.MovieShowNotFoundException;
import com.MyMoviePlan.exception.ShowNotFoundException;
import com.MyMoviePlan.repository.BookingRepository;
import com.MyMoviePlan.repository.MovieShowsRepository;
import com.MyMoviePlan.repository.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/show")
@AllArgsConstructor
public class ShowController {

    private final ShowRepository show;
    private final MovieShowsRepository movieShow;
    private final BookingRepository booking;

    @GetMapping("{show_id}")
    public ShowEntity findShowById(@PathVariable final int show_id) {
        return this.show.findById(show_id)
                .orElseThrow(() -> new ShowNotFoundException("Show with Id: " + show_id + " not found"));
    }

    @GetMapping({"/", "all"})
    public List<ShowEntity> findAllShows() {
        return this.show.findAll();
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ShowEntity updateShow(@RequestBody final ShowEntity show) {
        return this.show.save(show);
    }

    @DeleteMapping("delete/{show_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteShow(@PathVariable final int show_id) {
        this.show.deleteById(show_id);
    }


    /*
     *   ============================= Movie Show Controller ==========================
     */

    @GetMapping("{show_id}/movie-shows/all")
    public List<MovieShowsEntity> findAllMovieShows(@PathVariable final int show_id) {
        return this.findShowById(show_id)
                .getMovieShows();
    }

    @GetMapping("{show_id}/movie-shows/{movie_show_id}")
    public MovieShowsEntity findMovieShowById(@PathVariable final int show_id,
                                              @PathVariable final int movie_show_id) {
        return this.getMovieShowEntity(this.findShowById(show_id), movie_show_id);
    }

    @PostMapping("{show_id}/movie-shows/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieShowsEntity saveMovieShow(@PathVariable final int show_id,
                                          @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(show_id);
        movieShow.setShow(show);
        return this.movieShow.save(movieShow);
    }

    @PutMapping("{show_id}/movie-shows/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public MovieShowsEntity updateMovieShow(@PathVariable final int show_id,
                                            @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(show_id);
        movieShow.setShow(show);
        return this.movieShow.save(movieShow);
    }

    /*
     *   ============================= Booking Controller ==========================
     */

    @GetMapping("{show_id}/movie-shows/{movie_show_id}/booking/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public BookingEntity findBookingById(@PathVariable final int show_id,
                                         @PathVariable final int movie_show_id,
                                         @PathVariable final int booking_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(show_id, movie_show_id);
        return movieShow.getBookings()
                .stream().filter(booking -> booking.getId() == solveId(booking_id))
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: "
                        + actualId(booking_id) + " not found."));
    }

    @GetMapping("{show_id}/movie-shows/{movie_show_id}/booking/all")
    @PreAuthorize("hasAuthority('READ')")
    public List<BookingEntity> allBookings(@PathVariable final int show_id,
                                           @PathVariable final int movie_show_id) {
        return this.findMovieShowById(show_id, movie_show_id).getBookings();
    }

    @PostMapping("{show_id}/movie-shows/{movie_show_id}/booking/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public BookingEntity saveBooking(@PathVariable final int show_id,
                                     @PathVariable final int movie_show_id,
                                     @RequestBody final BookingEntity booking) {
        final ShowEntity show = this.findShowById(show_id);
        final MovieShowsEntity movieShow = this.getMovieShowEntity(show, movie_show_id);
        movieShow.setShow(show)
                .getBookings()
                .add(booking);
        this.movieShow.save(movieShow);
        return booking;
    }

    @PutMapping("{show_id}/movie-shows/{movie_show_id}/booking/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public BookingEntity updateBooking(@PathVariable final int show_id,
                                       @PathVariable final int movie_show_id,
                                       @RequestBody final BookingEntity booking) {
        final int booking_id = booking.getId();
        final ShowEntity show = this.findShowById(show_id);
        final MovieShowsEntity movieShow = this.getMovieShowEntity(show, movie_show_id);
        movieShow.setShow(show)
                .getBookings()
                .remove(solveId(booking_id));
        movieShow.getBookings()
                .add(booking);
        this.movieShow.save(movieShow);
        return booking;
    }

    /*
     *   ============================= Useful methods ==========================
     */

    private MovieShowsEntity getMovieShowEntity(final ShowEntity show, final int movie_show_id) {
        return show.getMovieShows()
                .stream()
                .filter(movie_show -> movie_show.getId() == solveId(movie_show_id))
                .findFirst()
                .orElseThrow(() -> new MovieShowNotFoundException("Movie Show with id: "
                        + actualId(movie_show_id) + " not found"));
    }

    /* In database primary key starts with 1, but in list index starts with 0.
    So to match the primary key with list index, I'm subtracting 1 from the path variable.*/
    private int solveId(final int id) {
        return id - 1;
    }

    private int actualId(final int id) {
        return id + 1;
    }
}
