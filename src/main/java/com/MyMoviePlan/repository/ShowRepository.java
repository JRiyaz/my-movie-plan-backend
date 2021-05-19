package com.MyMoviePlan.repository;

import com.MyMoviePlan.entity.ShowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<ShowEntity, Integer> {
}
