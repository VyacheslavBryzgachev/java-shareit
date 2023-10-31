package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private int id;
    private User booker;
    private Item bookingItem;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
}
