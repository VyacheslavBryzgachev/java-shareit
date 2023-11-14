package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.DbBookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final DbBookingStorage dbBookingStorage;

    @Override
    public Booking createBooking(BookingDto bookingDto, long userId) {
        return dbBookingStorage.createBooking(bookingDto, userId);
    }

    @Override
    public List<Booking> getUserBookings(String state, Long userId) {
        return dbBookingStorage.getUserBookings(state, userId);
    }

    @Override
    public List<Booking> getUserItemsBookings(String state, long userId) {
        return dbBookingStorage.getUserItemsBookings(state, userId);
    }

    @Override
    public Booking getBookingById(long bookingId, long userId) {
        return dbBookingStorage.getBookingById(bookingId, userId);
    }

    @Override
    public Booking updateBooking(long bookingId, boolean isApproved, long userId) {
        return dbBookingStorage.updateBooking(bookingId, isApproved, userId);
    }
}
