package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.AuditoriumEntity;
import com.MyMoviePlan.entity.BookingEntity;
import com.MyMoviePlan.entity.MovieShowsEntity;
import com.MyMoviePlan.entity.ShowEntity;
import com.MyMoviePlan.exception.AuditoriumNotFoundException;
import com.MyMoviePlan.exception.BookingNotFoundException;
import com.MyMoviePlan.exception.MovieShowNotFoundException;
import com.MyMoviePlan.exception.ShowNotFoundException;
import com.MyMoviePlan.repository.AuditoriumRepository;
import com.MyMoviePlan.repository.MovieShowsRepository;
import com.MyMoviePlan.repository.ShowRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorium")
@AllArgsConstructor
public class AuditoriumController {

    private final AuditoriumRepository auditorium;
    private final MovieShowsRepository movieShow;
    private final ShowRepository show;

    @GetMapping({"/", "all"})
    public List<AuditoriumEntity> findAllAuditoriums() {
        return this.auditorium.findAll();
    }

    @GetMapping("{auditorium_id}")
    public AuditoriumEntity findAuditoriumById(@PathVariable final int auditorium_id) {
        return this.auditorium.findById(auditorium_id)
                .orElseThrow(() -> new AuditoriumNotFoundException("Auditorium with id: " + auditorium_id + " not found."));
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('WRITE')")
    public AuditoriumEntity saveAuditorium(@RequestBody final AuditoriumEntity auditorium) {
//        Linking all the shows to the auditorium
        auditorium.getShows()
                .stream()
                .forEach(show -> show.setAuditorium(auditorium));
        return this.auditorium.save(auditorium);
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public AuditoriumEntity updateAuditorium(@RequestBody final AuditoriumEntity auditorium) {
        return this.auditorium.save(auditorium);
    }

    @DeleteMapping("delete/{auditorium_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteAuditorium(@PathVariable final int auditorium_id) {
        this.auditorium.deleteById(auditorium_id);
    }

    /*
     *   ============================= Show Controller ==========================
     */

    @GetMapping("{auditorium_id}/show/{show_id}")
    public ShowEntity findShowById(@PathVariable final int auditorium_id,
                                   @PathVariable final int show_id) {
        return this.findAuditoriumById(auditorium_id).getShows()
                .stream()
                .filter(show -> show.getId() == solveId(show_id))
                .findFirst()
                .orElseThrow(() -> new ShowNotFoundException("Show with Id: " + actualId(show_id) + " not found"));
    }

    @GetMapping("{auditorium_id}/show/all")
    public List<ShowEntity> findAllShows(@PathVariable final int auditorium_id) {
        return this.findAuditoriumById(auditorium_id).getShows();
    }

    @PostMapping("{auditorium_id}/show/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public ShowEntity saveShow(@PathVariable final int auditorium_id,
                               @RequestBody final ShowEntity show) {
        final AuditoriumEntity auditorium = this.findAuditoriumById(auditorium_id);
        show.setAuditorium(auditorium);
        return this.show.save(show);
    }

    @PutMapping("{auditorium_id}/show/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public ShowEntity updateShow(@PathVariable final int auditorium_id,
                                 @RequestBody final ShowEntity show) {
        final AuditoriumEntity auditorium = this.findAuditoriumById(auditorium_id);
        show.setAuditorium(auditorium);
        return this.show.save(show);
//        auditorium.getShows().remove(solveId(show_id));
//        auditorium.getShows().add(show);
//
////        Linking all the shows to the auditorium
//        auditorium.getShows()
//                .stream()
//                .forEach(showEntity -> showEntity.setAuditorium(auditorium));
//
//        return this.auditorium.save(auditorium)
//                .getShows()
//                .stream()
//                .filter(showEntity -> showEntity.getId() == solveId(show_id))
//                .findFirst()
//                .orElseThrow(() -> new ShowNotFoundException("Show with Id: " + actualId(show_id) + " not found"));
    }

    @DeleteMapping("{auditorium_id}/show/delete/{show_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteShow(@PathVariable final int auditorium_id,
                           @PathVariable final int show_id) {
        this.show.deleteById(show_id);
    }

    /*
     *   ============================= Movie Show Controller ==========================
     */

    @GetMapping("{auditorium_id}/show/{show_id}/movie-shows/all")
    public List<MovieShowsEntity> findAllMovieShows(@PathVariable final int auditorium_id,
                                                    @PathVariable final int show_id) {
        return this.findShowById(auditorium_id, show_id)
                .getMovieShows();
    }

    @GetMapping("{auditorium_id}/show/{show_id}/movie-shows/{movie_show_id}")
    public MovieShowsEntity findMovieShowById(@PathVariable final int auditorium_id,
                                              @PathVariable final int show_id,
                                              @PathVariable final int movie_show_id) {
        return this.findShowById(auditorium_id, show_id)
                .getMovieShows()
                .stream()
                .filter(movie_show -> movie_show.getId() == solveId(movie_show_id))
                .findFirst()
                .orElseThrow(
                        () -> new MovieShowNotFoundException("Movie Show with id: "
                                + actualId(movie_show_id) + " not found"));
    }

    @PostMapping("{auditorium_id}/show/{show_id}/movie-shows/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieShowsEntity saveMovieShow(@PathVariable final int auditorium_id,
                                          @PathVariable final int show_id,
                                          @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
        movieShow.setShow(show);
        return this.movieShow.save(movieShow);
    }

    @PutMapping("{auditorium_id}/show/{show_id}/movie-shows/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public MovieShowsEntity updateMovieShow(@PathVariable final int auditorium_id,
                                            @PathVariable final int show_id,
                                            @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
        movieShow.setShow(show);
        return this.movieShow.save(movieShow);
    }

    /*
     *   ============================= Booking Controller ==========================
     */

    @GetMapping("{auditorium_id}/show/{show_id}/movie-shows/{movie_show_id}/booking/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public BookingEntity findBookingById(@PathVariable final int auditorium_id,
                                         @PathVariable final int show_id,
                                         @PathVariable final int movie_show_id,
                                         @PathVariable final int booking_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(auditorium_id, show_id, movie_show_id);
        return movieShow.getBookings()
                .stream().filter(booking -> booking.getId() == solveId(booking_id))
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: "
                        + actualId(booking_id) + " not found."));
    }

    @GetMapping("{auditorium_id}/show/{show_id}/movie-shows/{movie_show_id}/booking/all")
    @PreAuthorize("hasAuthority('READ')")
    public List<BookingEntity> allBookings(@PathVariable final int auditorium_id,
                                           @PathVariable final int show_id,
                                           @PathVariable final int movie_show_id) {
        return this.findMovieShowById(auditorium_id, show_id, movie_show_id).getBookings();
    }

    @PostMapping("{auditorium_id}/show/{show_id}/movie-shows/{movie_show_id}/booking/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public BookingEntity saveBooking(@PathVariable final int auditorium_id,
                                     @PathVariable final int show_id,
                                     @PathVariable final int movie_show_id,
                                     @RequestBody final BookingEntity booking) {
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
        final MovieShowsEntity movieShow = this.getMovieShowEntity(show, movie_show_id);
        movieShow.setShow(show)
                .getBookings()
                .add(booking);
        this.movieShow.save(movieShow);
        return booking;
    }

    @PutMapping("{auditorium_id}/show/{show_id}/movie-shows/{movie_show_id}/booking/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public BookingEntity updateBooking(@PathVariable final int auditorium_id,
                                       @PathVariable final int show_id,
                                       @PathVariable final int movie_show_id,
                                       @RequestBody final BookingEntity booking) {
        final int booking_id = booking.getId();
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
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
