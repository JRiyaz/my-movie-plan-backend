package com.MyMoviePlan.repository;

import com.MyMoviePlan.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    List<BookingEntity> findAllByUserIdOrderByBookedOnAsc(final String userId);
}
