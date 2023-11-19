package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DbBookingStorage {
    private final BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBookingById(long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}
