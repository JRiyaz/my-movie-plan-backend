package com.MyMoviePlan.repository;

import com.MyMoviePlan.entity.MovieShowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieShowsRepository extends JpaRepository<MovieShowsEntity, Integer> {
}
