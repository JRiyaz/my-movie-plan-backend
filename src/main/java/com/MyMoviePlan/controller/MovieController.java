package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.MovieEntity;
import com.MyMoviePlan.exception.MovieNotFoundException;
import com.MyMoviePlan.repository.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieRepository repository;

    @GetMapping({"/", "all"})
    public List<MovieEntity> findAll() {
        return repository.findAll();
    }

    @GetMapping("{movie_id}")
    public MovieEntity findById(@PathVariable final int movie_id) {
        return repository.findById(movie_id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with movie_id: " + movie_id + " not found."));
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('WRITE')")
    public MovieEntity saveMovie(@RequestBody final MovieEntity movie) {
        return repository.save(movie);
    }

    @PutMapping("update/{movie_id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public MovieEntity updateMovie(@RequestBody final MovieEntity movie,
                                   @PathVariable final int movie_id) {
        return repository.save(movie);
    }

    @DeleteMapping("delete/{movie_id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteMovie(@PathVariable final int movie_id) {
        repository.deleteById(movie_id);
    }
}
