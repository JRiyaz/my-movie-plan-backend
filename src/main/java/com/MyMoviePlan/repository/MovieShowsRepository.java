package com.MyMoviePlan.repository;

import com.MyMoviePlan.entity.MovieShowsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieShowsRepository extends JpaRepository<MovieShowsEntity, Integer> {

    //    https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.limit-query-result
    //    https://stackoverflow.com/questions/11401229/how-to-use-select-distinct-with-random-function-in-postgresql
    //    https://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring
    //    https://dev.to/golovpavel/make-a-request-with-sub-condition-for-child-list-via-spring-data-jpa-4inn
    //    https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

    //    @Query(value = "SELECT DISTINCT ON(movie_id) * FROM movie_shows WHERE start >= CURRENT_DATE", nativeQuery = true)
    @Query(value = "SELECT DISTINCT ON(ms.movie_id) * FROM movie_shows ms WHERE ms.show_start > CURRENT_DATE", nativeQuery = true)
    List<MovieShowsEntity> findAllUpComing();

    @Query(value = "SELECT DISTINCT ON(ms.movie_id) * FROM movie_shows ms WHERE ms.show_start <= CURRENT_DATE AND ms.show_end >= CURRENT_DATE", nativeQuery = true)
    List<MovieShowsEntity> findAllNowPlaying();

    @Query(value = "SELECT DISTINCT ON(ms.movie_id) * FROM movie_shows ms WHERE ms.show_end >= CURRENT_DATE", nativeQuery = true)
    List<MovieShowsEntity> findAllNowPlayingAndUpComing();

    @Query(value = "SELECT DISTINCT ON(ms.movie_id) * FROM movie_shows ms WHERE ms.show_end < CURRENT_DATE", nativeQuery = true)
    List<MovieShowsEntity> findAllNotPlaying();

    //    @Query("FROM MovieShowsEntity ms LEFT JOIN ms.bookings b WHERE ms.id = ?1 AND b.dateOfBooking = ?2")
    //    @Query(value = "SELECT * FROM movie_shows ms INNER JOIN bookings b ON ms.id = :id and b.date_of_booking = ':dateOfBooking'", nativeQuery = true)
    //    Optional<MovieShowsEntity> findByIdAndDateOfBooking(@Param("id") final int id, @Param("dateOfBooking") final String dateOfBooking);

    //      SELECT * FROM (SELECT DISTINCT movie_id FROM movie_shows WHERE start > CURRENT_DATE) ms ORDER BY random() LIMIT :records
    @Query(value = "SELECT DISTINCT ON(ms.movie_id) * FROM movie_shows ms WHERE ms.show_start > CURRENT_DATE LIMIT :records", nativeQuery = true)
    List<MovieShowsEntity> findFewUpComing(@Param("records") final int records);

    @Query(value = "SELECT DISTINCT ON(ms.movie_id) * FROM movie_shows ms WHERE ms.show_start <= CURRENT_DATE AND ms.show_end >= CURRENT_DATE LIMIT :records", nativeQuery = true)
    List<MovieShowsEntity> findFewNowPlaying(@Param("records") final int records);

    //    @Query(value = "SELECT ms.id, ms.show_end, ms.movie_id, ms.show_start, ms.show_id, ms.price_id FROM movie_shows ms INNER JOIN bookings b ON b.id = :bookingId", nativeQuery = true)
    //    MovieShowsEntity findByBookingId(@Param("bookingId") final int bookingId);
}
