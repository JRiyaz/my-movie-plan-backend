package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.MovieEntity;
import com.MyMoviePlan.exception.MovieNotFoundException;
import com.MyMoviePlan.repository.MovieRepository;
import com.MyMoviePlan.repository.MovieShowsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;
    private final MovieShowsRepository movieShowsRepository;

    @GetMapping({"/", "all"})
    public List<MovieEntity> findAll() {
        return movieRepository.findAll();
    }

    @GetMapping("{movie_id}")
    public MovieEntity findById(@PathVariable final int movie_id) {
        return movieRepository.findById(movie_id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with movie_id: " + movie_id + " not found."));
    }

    @GetMapping("up-coming")
    public List<MovieEntity> upComing(@RequestParam(value = "records", required = false) Optional<String> records) {
        List<MovieEntity> movies;
        List<MovieEntity> allMovies;
        if (records.isPresent()) {
            movies = new ArrayList<>();
            allMovies = this.findAll();
            movieShowsRepository.findFewUpComing(Integer.parseInt(records.get()))
                    .forEach(m_show -> movies.add(allMovies.stream()
                            .filter(movie -> (movie.getId() == m_show.getMovieId() && movie.getRelease().getTime() > new Date().getTime()))
                            .findFirst().orElse(null)));
        } else {
            movies = new ArrayList<>();
            allMovies = this.findAll();
            movieShowsRepository.findAllUpComing()
                    .forEach(m_show -> movies.add(allMovies.stream()
                            .filter(movie -> movie.getId() == m_show.getMovieId() && movie.getRelease().getTime() > new Date().getTime())
                            .findFirst().orElse(null)));
        }
//        return (movies.size() > 0 && !movies.contains(null)) ? movies : new ArrayList<>();
        movies.removeAll(Collections.singletonList(null));
        return movies;
    }

    @GetMapping("now-playing")
    public List<MovieEntity> nowPlaying(@RequestParam(value = "records", required = false) Optional<String> records) {
        List<MovieEntity> movies;
        List<MovieEntity> allMovies;
        if (records.isPresent()) {
            movies = new ArrayList<>();
            allMovies = this.findAll();
            movieShowsRepository.findFewNowPlaying(Integer.parseInt(records.get()))
                    .forEach(m_show -> movies.add(allMovies.stream()
                            .filter(movie -> movie.getId() == m_show.getMovieId())
                            .findFirst().orElse(null)));
        } else {
            movies = new ArrayList<>();
            allMovies = this.findAll();
            movieShowsRepository.findAllNowPlaying()
                    .forEach(m_show -> movies.add(allMovies.stream()
                            .filter(movie -> movie.getId() == m_show.getMovieId())
                            .findFirst().orElse(null)));
        }
        movies.removeAll(Collections.singletonList(null));
        return movies;
    }

    @GetMapping("now-playing-up-coming")
    public List<MovieEntity> nowPlayingAndUpComing() {
        final List<MovieEntity> movies = new ArrayList<>();
        final List<MovieEntity> allMovies = this.findAll();
        movieShowsRepository.findAllNowPlayingAndUpComing()
                .forEach(m_show -> movies.add(allMovies.stream()
                        .filter(movie -> movie.getId() == m_show.getMovieId())
                        .findFirst().orElse(null)));
        movies.removeAll(Collections.singletonList(null));
        return movies;
    }

    @GetMapping("not-playing")
    public List<MovieEntity> notPlaying() {
        final List<MovieEntity> movies = new ArrayList<>();
        final List<MovieEntity> allMovies = this.findAll();
        movieShowsRepository.findAllNotPlaying()
                .forEach(m_show -> movies.add(allMovies.stream()
                        .filter(movie -> movie.getId() == m_show.getMovieId())
                        .findFirst().orElse(null)));
        movies.removeAll(Collections.singletonList(null));
        return movies;
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieEntity saveMovie(@RequestBody final MovieEntity movie) {
        return movieRepository.save(movie);
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('UPDATE')")
    public MovieEntity updateMovie(@RequestBody final MovieEntity movie) {
        return movieRepository.save(movie);
    }

    @DeleteMapping("delete/{movie_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteMovie(@PathVariable final int movie_id) {
        movieRepository.deleteById(movie_id);
    }
}
