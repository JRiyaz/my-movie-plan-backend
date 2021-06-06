package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.BookingEntity;
import com.MyMoviePlan.entity.MovieShowsEntity;
import com.MyMoviePlan.exception.MovieShowNotFoundException;
import com.MyMoviePlan.model.BookedSeats;
import com.MyMoviePlan.repository.MovieShowsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/movie-show")
@AllArgsConstructor
public class MovieShowController {

    private final MovieShowsRepository repository;

    @PostMapping("add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieShowsEntity save(@RequestBody MovieShowsEntity movieShow) {
        return repository.save(movieShow);
    }

    @GetMapping("up-coming")
    @PreAuthorize("hasAuthority('READ')")
    public List<MovieShowsEntity> upComing(@RequestParam(value = "records", required = false) Optional<String> records) {
        if (records.isPresent())
            return repository.findFewUpComing(Integer.parseInt(records.get()));
        return repository.findAllUpComing();
    }

    @GetMapping("now-playing")
    public List<MovieShowsEntity> nowPlaying(@RequestParam(value = "records", required = false) Optional<String> records) {
        if (records.isPresent())
            return repository.findFewNowPlaying(Integer.parseInt(records.get()));
        return repository.findAllNowPlaying();
    }

    @GetMapping("now-playing-up-coming")
    public List<MovieShowsEntity> nowPlayingAndUpComing() {
        return repository.findAllNowPlayingAndUpComing();
    }

    @GetMapping("not-playing")
    @PreAuthorize("hasAuthority('WRITE')")
    public List<MovieShowsEntity> notPlaying() {
        return repository.findAllNotPlaying();
    }

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

    @GetMapping("{movie_show_id}/booked-seats/{on}")
    @PreAuthorize("hasAuthority('READ')")
    public BookedSeats bookedSeats(@PathVariable final int movie_show_id, @PathVariable final String on) {
        final List<BookingEntity> bookings = this.findMovieShowById(movie_show_id).getBookings()
                .stream().filter(m_show -> m_show.getDateOfBooking().toString().equals(on))
                .collect(Collectors.toList());

        int count = 0;
        List<String> seats = new ArrayList<>();
        for (BookingEntity booking : bookings) {
            count += booking.getTotalSeats();
            seats.addAll(booking.getSeatNumbers());
        }
        return new BookedSeats(count, seats);
    }
}
