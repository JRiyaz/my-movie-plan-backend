package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.*;
import com.MyMoviePlan.exception.AuditoriumNotFoundException;
import com.MyMoviePlan.exception.BookingNotFoundException;
import com.MyMoviePlan.exception.MovieShowNotFoundException;
import com.MyMoviePlan.exception.ShowNotFoundException;
import com.MyMoviePlan.model.TicketDetails;
import com.MyMoviePlan.model.UserRole;
import com.MyMoviePlan.repository.*;
import com.MyMoviePlan.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/auditorium")
@AllArgsConstructor
public class AuditoriumController {

    private final ShowRepository show;
    private final UserService service;
    private final BookingRepository booking;
    private final MovieRepository movie;
    private final MovieShowsRepository movieShow;
    private final AuditoriumRepository auditorium;

    @GetMapping({"/", "all"})
    public List<AuditoriumEntity> findAllAuditoriums() {
        return this.auditorium.findAll();
    }

    @GetMapping("{auditorium_id}")
    @PreAuthorize("hasAuthority('WRITE')")
    public AuditoriumEntity findAuditoriumById(@PathVariable final int auditorium_id) {
        return this.auditorium.findById(auditorium_id)
                .orElseThrow(() ->
                        new AuditoriumNotFoundException("Auditorium with id: " + auditorium_id + " not found."));
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('WRITE')")
    public AuditoriumEntity saveAuditorium(@RequestBody final AuditoriumEntity auditorium) {
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
                .filter(show -> show.getId() == show_id)
                .findFirst()
                .orElseThrow(() ->
                        new ShowNotFoundException("Show with Id: " + show_id + " not found"));
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
    }

    @DeleteMapping("{auditorium_id}/show/delete/{show_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteShow(@PathVariable final int auditorium_id,
                           @PathVariable final int show_id) {
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
        this.show.deleteById(show.getId());
    }

    /*
     *   ============================= Movie Show Controller ==========================
     */

    @GetMapping("movie/{movieId}")
    public List<AuditoriumEntity> findAuditoriumsByMovieId(@PathVariable final int movieId) {
        return this.findAllAuditoriums().stream()
                .filter(halls -> halls.getShows()
                        .stream()
                        .anyMatch(show -> show.getMovieShows()
                                .stream()
                                .anyMatch(m_show -> m_show.getMovieId() == movieId)))
                .collect(Collectors.toList());
    }

    @GetMapping("{auditorium_id}/movie/{movieId}")
    public List<ShowEntity> findShowsByMovieId(@PathVariable final int auditorium_id, @PathVariable final int movieId) {
        return this.findAllShows(auditorium_id).stream()
                .filter(show -> show.getMovieShows()
                        .stream()
                        .anyMatch(m_show -> m_show.getMovieId() == movieId))
                .collect(Collectors.toList());
    }

    @GetMapping("{auditorium_id}/show/{show_id}/movie-show/all")
    public List<MovieShowsEntity> findAllMovieShows(@PathVariable final int auditorium_id,
                                                    @PathVariable final int show_id) {
        return this.findShowById(auditorium_id, show_id)
                .getMovieShows();
    }

    @GetMapping("{auditorium_id}/show/{show_id}/movie-show/{movie_show_id}")
    public MovieShowsEntity findMovieShowById(@PathVariable final int auditorium_id,
                                              @PathVariable final int show_id,
                                              @PathVariable final int movie_show_id) {
        return this.findShowById(auditorium_id, show_id)
                .getMovieShows()
                .stream()
                .filter(movie_show -> movie_show.getId() == movie_show_id)
                .findFirst()
                .orElseThrow(
                        () -> new MovieShowNotFoundException("Movie Show with id: "
                                + movie_show_id + " not found"));
    }

    @PostMapping("{auditorium_id}/show/{show_id}/movie-show/add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieShowsEntity saveMovieShow(@PathVariable final int auditorium_id,
                                          @PathVariable final int show_id,
                                          @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
        final int movieId = movieShow.getMovieId();
        movieShow.setShow(show);
        movieShow.setMovieId(this.movie.findById(movieId).get().getId());
        return this.movieShow.save(movieShow);
    }

    @PutMapping("{auditorium_id}/show/{show_id}/movie-show/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public MovieShowsEntity updateMovieShow(@PathVariable final int auditorium_id,
                                            @PathVariable final int show_id,
                                            @RequestBody final MovieShowsEntity movieShow) {
        final ShowEntity show = this.findShowById(auditorium_id, show_id);
        movieShow.setShow(show);
        return this.movieShow.save(movieShow);
    }

    @DeleteMapping("{auditorium_id}/show/{show_id}/movie-show/delete/{movie_show_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteMovieShow(@PathVariable final int auditorium_id,
                                @PathVariable final int show_id,
                                @PathVariable final int movie_show_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(auditorium_id, show_id, movie_show_id);
        this.movieShow.deleteById(movieShow.getMovieId());
    }

    /*
     *   ============================= Booking Controller ==========================
     */

    @GetMapping("{auditorium_id}/show/{show_id}/movie-show/{movie_show_id}/booking/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public BookingEntity findBookingById(@PathVariable final int auditorium_id,
                                         @PathVariable final int show_id,
                                         @PathVariable final int movie_show_id,
                                         @PathVariable final int booking_id) {
        final MovieShowsEntity movieShow = this.findMovieShowById(auditorium_id, show_id, movie_show_id);
        return movieShow.getBookings()
                .stream().filter(booking -> booking.getId() == booking_id)
                .findFirst()
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: "
                        + booking_id + " not found."));
    }

    @GetMapping("{auditorium_id}/show/{show_id}/movie-show/{movie_show_id}/booking/all")
    @PreAuthorize("hasAuthority('WRITE')")
    public List<BookingEntity> allBookings(@PathVariable final int auditorium_id,
                                           @PathVariable final int show_id,
                                           @PathVariable final int movie_show_id) {
        final UserEntity user = this.service.getLoggedInUser();
        if (user.getUserRole().equals(UserRole.ROLE_ADMIN) || user.getUserRole().equals(UserRole.ROLE_SUPER_ADMIN))
            return this.findMovieShowById(auditorium_id, show_id, movie_show_id).getBookings();
        else
            return this.findMovieShowById(auditorium_id, show_id, movie_show_id).getBookings()
                    .stream().filter(booking -> booking.getUserId().equals(user.getId()))
                    .collect(Collectors.toList());
    }

    @PostMapping("{auditorium_id}/show/{show_id}/movie-show/{movie_show_id}/booking/add")
//    @PreAuthorize("hasAuthority('WRITE')")
    public BookingEntity saveBooking(@PathVariable final int auditorium_id,
                                     @PathVariable final int show_id,
                                     @PathVariable final int movie_show_id,
                                     @RequestBody final BookingEntity booking) {
        final MovieShowsEntity moveShow = this.findMovieShowById(auditorium_id, show_id, movie_show_id);
        booking.setUserId(this.service.getLoggedInUser().getId());
//        booking.setUserId(this.service.findByMobile("8099531318").get().getId());
        booking.setMovieShow(moveShow);
        booking.setBookingDetails(new BookingDetailsEntity(auditorium_id, show_id, movie_show_id, moveShow.getMovieId()));
        return this.booking.save(booking);
    }

    @PutMapping("{auditorium_id}/show/{show_id}/movie-show/{movie_show_id}/booking/update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public BookingEntity updateBooking(@PathVariable final int auditorium_id,
                                       @PathVariable final int show_id,
                                       @PathVariable final int movie_show_id,
                                       @RequestBody final BookingEntity booking) {
        final MovieShowsEntity moveShow = this.findMovieShowById(auditorium_id, show_id, movie_show_id);
        booking.setMovieShow(moveShow);
        return this.booking.save(booking);
    }

    @DeleteMapping("{auditorium_id}/show/{show_id}/movie-show/{movie_show_id}/booking/delete/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public void deleteBookingById(@PathVariable final int auditorium_id,
                                  @PathVariable final int show_id,
                                  @PathVariable final int movie_show_id,
                                  @PathVariable final int booking_id) {
        final BookingEntity booking = this.findBookingById(auditorium_id, show_id, movie_show_id, booking_id);
        this.booking.deleteById(booking.getId());
    }

    @GetMapping("ticket-details/{booking_id}")
    @PreAuthorize("hasAuthority('READ')")
    public TicketDetails getMovieDetails(@PathVariable final int booking_id) {

        final PaymentEntity payment = this.booking.findById(booking_id).get().getPayment();

        final MovieShowsEntity movieShow = this.movieShow.findAll().stream().filter(m_show -> m_show.getBookings()
                .stream().anyMatch(booking -> booking.getId() == booking_id)).findFirst().get();

        final MovieEntity movie = this.movie.findById(movieShow.getMovieId()).get();

        final ShowEntity showEntity = show.findAll().stream()
                .filter(show -> show.getMovieShows()
                        .stream().anyMatch(m_show -> m_show.getId() == movieShow.getId())).findFirst().get();

        final AuditoriumEntity auditorium = this.auditorium.findAll().stream().filter(hall -> hall.getShows()
                .stream().anyMatch(show -> show.getId() == showEntity.getId())).findFirst().get();

        return new TicketDetails(auditorium.getName(), showEntity.getName(), showEntity.getStartTime(), payment.getAmount(), movie.getName(), movie.getImage(), movie.getBgImage());
    }
}
