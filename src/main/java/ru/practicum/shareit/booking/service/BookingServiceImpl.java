package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.DbBookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.exceptions.WrongStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final DbBookingStorage dbBookingStorage;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(BookingDto bookingDto, long userId) {
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingException("Дата окончания бронирования не может быть меньше или быть такой же как дата начала бронирования");
        }
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new UnknownIdException("Вещь с таким id=" + bookingDto.getItemId() + " не найдена"));
        if (item.getAvailable().equals(false)) {
            throw new BookingException("Вещь недоступна для бронирования");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + userId + " не найдено"));
        if (user.getId() == item.getOwner().getId()) {
            throw new UnknownIdException("Нельзя забронировать свою вещь");
        }
        Booking booking = Booking.builder()
                .booker(user)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .status(Status.WAITING)
                .build();
        return dbBookingStorage.createBooking(booking);
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
        return dbBookingStorage.getBookingById(bookingId)
                .filter(booking -> booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)
                .orElseThrow(() -> new UnknownIdException("Пользователю с id=" + userId + " не принадлежит данная вещь"));
    }

    @Override
    public Booking updateBooking(long bookingId, boolean isApproved, long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new UnknownIdException("Id бронирования не найден"));
        if (!(booking.getItem().getOwner().getId() == userId)) {
            throw new UnknownIdException("Пользователю с id=" + userId + " не принадлежит данная вещь");
        } else if (!(booking.getStatus().equals(Status.WAITING))) {
            throw new BookingException("Ошибка бронирования");
        } else if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return dbBookingStorage.updateBooking(booking);
    }
}
