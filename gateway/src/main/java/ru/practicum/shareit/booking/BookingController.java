package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.exceptions.BookingException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@Valid @RequestBody BookingDto bookingDto,
                                                @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingException("Дата окончания бронирования не может быть меньше или быть такой же как дата начала бронирования");
        }
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(@PathVariable @Valid Long bookingId, @RequestParam(value = "approved") boolean isApproved,
                                                @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return bookingClient.updateBooking(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId, @RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                                  @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @RequestParam(value = "from", defaultValue = "0", required = false) @Positive Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10", required = false) @Positive Integer size) {
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getUserItemBookings(@RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                                      @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                      @RequestParam(value = "from", defaultValue = "0", required = false) @Positive Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10", required = false) @Positive Integer size) {
        return bookingClient.getUserItemsBookings(userId, state, from, size);
    }
}
