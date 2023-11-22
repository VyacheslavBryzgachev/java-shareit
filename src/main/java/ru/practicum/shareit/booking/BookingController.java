package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut createBooking(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateBooking(@Valid @PathVariable long bookingId, @RequestParam(value = "approved") boolean isApproved,
                                       @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return bookingService.updateBooking(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable long bookingId, @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoOut> getUserBookings(@RequestParam(value = "state", required = false) String state,
                                               @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                               @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                               @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return bookingService.getUserBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getUserItemBookings(@RequestParam(value = "state", required = false) String state,
                                                   @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                   @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return bookingService.getUserItemsBookings(state, userId, from, size);
    }
}
