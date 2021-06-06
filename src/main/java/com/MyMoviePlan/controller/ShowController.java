package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.BookingEntity;
import com.MyMoviePlan.entity.MovieShowsEntity;
import com.MyMoviePlan.entity.ShowEntity;
import com.MyMoviePlan.exception.BookingNotFoundException;
import com.MyMoviePlan.exception.MovieShowNotFoundException;
import com.MyMoviePlan.exception.ShowNotFoundException;
import com.MyMoviePlan.repository.BookingRepository;
import com.MyMoviePlan.repository.MovieRepository;
import com.MyMoviePlan.repository.MovieShowsRepository;
import com.MyMoviePlan.repository.ShowRepository;
import com.MyMoviePlan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/show")
@AllArgsConstructor
public class ShowController {

    private final ShowRepository show;
    private final MovieShowsRepository movieShow;
    private final MovieRepository movie;
    private final UserService service;
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

    @DeleteMapping("delete/{show_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteShow(@PathVariable final int show_id) {
        this.show.deleteById(show_id);
    }


    /*
     *   ============================= Movie Show Controller ==========================
     */

    @GetMapping("{show_id}/movie-show/all")
    public List<MovieShowsEntity> findAllMovieShows(@PathVariable final int show_id) {
        return this.findShowById(show_id)
                .getMovieShows();
    }

    @GetMapping("{show_id}/movie-show/{movie_show_id}")
    public MovieShowsEntity findMovieShowById(@PathVariable final int show_id,
                                              @PathVariable final int movie_show_id) {
        return this.findShowById(show_id)
                .getMovieShows()
                .stream()
                .filter(movie_show -> movie_show.getId() == movie_show_id)
                .findFirst()
                .orElseThrow(
                        () -> new MovieShowNotFoundException("Movie Show with id: "
                                + movie_show_id + " not found"));
    }

    @PostMapping("{show_id}/movie-show/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieShowsEntity saveMovieShow(@PathVariable final int show_id,
                                          @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(show_id);
        final int movieId = movieShow.getMovieId();
        movieShow.setShow(show);
        movieShow.setMovieId(this.movie.findById(movieId).get().getId());
        return this.movieShow.save(movieShow);
    }

    @PutMapping("{show_id}/movie-show/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public MovieShowsEntity updateMovieShow(@PathVariable final int show_id,
                                            @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(show_id);
        movieShow.setShow(show);
        return this.movieShow.save(movieShow);
    }

    @DeleteMapping("{show_id}/movie-show/delete/{movie_show_id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public void deleteMovieShow(@PathVariable final int show_id,
                                @PathVariable final int movie_show_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(show_id, movie_show_id);
        this.movieShow.deleteById(movieShow.getMovieId());
    }

    /*
     *   ============================= Booking Controller ==========================
     */

    @GetMapping("{show_id}/movie-show/{movie_show_id}/booking/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public BookingEntity findBookingById(@PathVariable final int show_id,
                                         @PathVariable final int movie_show_id,
                                         @PathVariable final int booking_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(show_id, movie_show_id);
        return movieShow.getBookings()
                .stream().filter(booking -> booking.getId() == booking_id)
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: "
                        + booking_id + " not found."));
    }

    @GetMapping("{show_id}/movie-show/{movie_show_id}/booking/all")
    @PreAuthorize("hasAuthority('READ')")
    public List<BookingEntity> allBookings(@PathVariable final int show_id,
                                           @PathVariable final int movie_show_id) {
        return this.findMovieShowById(show_id, movie_show_id).getBookings();
    }

    @PostMapping("{show_id}/movie-show/{movie_show_id}/booking/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public BookingEntity saveBooking(@PathVariable final int show_id,
                                     @PathVariable final int movie_show_id,
                                     @RequestBody final BookingEntity booking) {
        final MovieShowsEntity moveShow = this.findMovieShowById(show_id, movie_show_id);
//        booking.setUserId(this.service.getLoggedInUser().getId());
        booking.setUserId(this.service.findByMobile("8099531318").get().getId());
        booking.setMovieShow(moveShow);
        return this.booking.save(booking);
    }

    @PutMapping("{show_id}/movie-show/{movie_show_id}/booking/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public BookingEntity updateBooking(@PathVariable final int show_id,
                                       @PathVariable final int movie_show_id,
                                       @RequestBody final BookingEntity booking) {
        final MovieShowsEntity moveShow = this.findMovieShowById(show_id, movie_show_id);
        booking.setMovieShow(moveShow);
        return this.booking.save(booking);
    }

    @DeleteMapping("{show_id}/movie-show/{movie_show_id}/booking/delete/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public void deleteBookingById(@PathVariable final int show_id,
                                  @PathVariable final int movie_show_id,
                                  @PathVariable final int booking_id) {
        final BookingEntity booking = this.findBookingById(show_id, movie_show_id, booking_id);
        this.booking.deleteById(booking.getId());
    }
}
