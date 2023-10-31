package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getBooker(),
                booking.getBookingItem(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus());
    }
}
