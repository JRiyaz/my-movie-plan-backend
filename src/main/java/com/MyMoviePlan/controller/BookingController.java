package com.MyMoviePlan.controller;

import com.MyMoviePlan.entity.BookingEntity;
import com.MyMoviePlan.exception.BookingNotFoundException;
import com.MyMoviePlan.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
@AllArgsConstructor
public class BookingController {

    private final BookingRepository repository;

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('READ')")
    public BookingEntity findById(@PathVariable final int id) {
        return repository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id: " + id + " not found."));
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthority('READ')")
    public List<BookingEntity> allBookings() {
        return repository.findAll();
    }

    @PostMapping("add")
    @PreAuthorize("hasAuthority('WRITE')")
    public BookingEntity saveBooking(@RequestBody final BookingEntity booking) {
        return repository.save(booking);
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    public BookingEntity updateBooking(@RequestBody final BookingEntity booking,
                                       @PathVariable final int id) {
        return repository.save(booking);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public void deleteBooking(@PathVariable final int id) {
        repository.deleteById(id);
    }
}
