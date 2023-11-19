package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class BookingMapper {

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .booker(User.builder()
                        .id(bookingDto.getBookerId())
                        .build())
                .item(Item.builder()
                        .id(bookingDto.getItemId())
                        .build())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
    }

    public BookingDtoOut bookingDtoOut(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }
}
