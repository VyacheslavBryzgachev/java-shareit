package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(BookingDto bookingDto, long userId);

    List<Booking> getUserBookings(String state, Long userId);

    List<Booking> getUserItemsBookings(String state, long userId);

    Booking getBookingById(long bookingId, long userId);

    Booking updateBooking(long bookingId, boolean isApproved, long userId);
}
