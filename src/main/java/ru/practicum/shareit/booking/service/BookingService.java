package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut createBooking(BookingDto bookingDto, long userId);

    List<BookingDtoOut> getUserBookings(String state, long userId, Integer from, Integer size);

    List<BookingDtoOut> getUserItemsBookings(String state, long userId, Integer from, Integer size);

    BookingDtoOut getBookingById(long bookingId, long userId);

    BookingDtoOut updateBooking(long bookingId, boolean isApproved, long userId);
}
