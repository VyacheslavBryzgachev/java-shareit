package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.DbBookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.exceptions.WrongStateException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final DbBookingStorage dbBookingStorage;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto, long userId) {
        return dbBookingStorage.createBooking(bookingDto, userId);
    }

    @Override
    public List<Booking> getUserBookings(String state, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + userId + " не найдено"));
        if (state == null || state.equals("ALL")) {
            return bookingRepository.getUserBookingsIfStateAll(userId);
        } else if (state.equals("CURRENT")) {
            return bookingRepository.getUserBookingsIfStateCurrent(userId);
        } else if (state.equals("FUTURE")) {
            return bookingRepository.getUserBookingsIfStateFuture(userId);
        } else if (state.equals("REJECTED")) {
            return bookingRepository.getUserBookingsIfStateRejected(userId);
        } else if (state.equals("PAST")) {
            return bookingRepository.getUserBookingsIfStatePast(userId);
        } else if (state.equals("WAITING")) {
            return bookingRepository.getUserBookingsIfStateWaiting(userId);
        } else {
            throw new WrongStateException("Unknown state: " + state);
        }
    }

    @Override
    public List<Booking> getUserItemsBookings(String state, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + userId + " не найдено"));
        if (state == null || state.equals("ALL")) {
            return bookingRepository.getUserItemsBookingsIfStateAll(userId);
        } else if (state.equals("CURRENT")) {
            return bookingRepository.getUserItemsBookingsIfStateCurrent(userId);
        } else if (state.equals("FUTURE")) {
            return bookingRepository.getUserItemsBookingsIfStateFuture(userId);
        } else if (state.equals("REJECTED")) {
            return bookingRepository.getUserItemsBookingsIfStateRejected(userId);
        } else if (state.equals("PAST")) {
            return bookingRepository.getUserItemsBookingsIfStatePast(userId);
        } else if (state.equals("WAITING")) {
            return bookingRepository.getUserItemsBookingsIfStateWaiting(userId);
        } else {
            throw new WrongStateException("Unknown state: " + state);
        }
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
