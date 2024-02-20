package com.MyMoviePlan.model;

import com.MyMoviePlan.entity.MovieShowsEntity;

import java.util.Objects;

public class MovieShowsFilter {
    private final MovieShowsEntity ms;

    public MovieShowsFilter(MovieShowsEntity ms) {
        this.ms = ms;
    }

    public MovieShowsEntity unwrap() {
        return ms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieShowsFilter that = (MovieShowsFilter) o;
        return Objects.equals(ms.getId(), that.ms.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ms.getId());
    }

}
