package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.DbBookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final DbBookingStorage dbBookingStorage;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper = new BookingMapper();

    @Override
    public BookingDtoOut createBooking(BookingDto bookingDto, long userId) {
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
                .createdTime(LocalDateTime.now())
                .build();
        return bookingMapper.bookingDtoOut(dbBookingStorage.createBooking(booking));
    }

    @Override
    public List<BookingDtoOut> getUserBookings(String state, long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + userId + " не найдено"));
        List<BookingDtoOut> bookingDtoList = new ArrayList<>();
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        if (state == null || state.equals("ALL")) {
            List<Booking> bookings = bookingRepository.getUserBookingsIfStateAll(userId, from, size);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("CURRENT")) {
            List<Booking> bookings = bookingRepository.getUserBookingsIfStateCurrent(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("FUTURE")) {
            List<Booking> bookings = bookingRepository.getUserBookingsIfStateFuture(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("REJECTED")) {
            List<Booking> bookings = bookingRepository.getUserBookingsIfStateRejected(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("PAST")) {
            List<Booking> bookings = bookingRepository.getUserBookingsIfStatePast(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("WAITING")) {
            List<Booking> bookings = bookingRepository.getUserBookingsIfStateWaiting(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else {
            throw new WrongStateException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDtoOut> getUserItemsBookings(String state, long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UnknownIdException("Пользователя с таким id=" + userId + " не найдено"));
        List<BookingDtoOut> bookingDtoList = new ArrayList<>();
        Pageable pageWithFromAndSize = PageRequest.of(from, size);
        if (state == null || state.equals("ALL")) {
            List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateAll(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("CURRENT")) {
            List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateCurrent(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("FUTURE")) {
            List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateFuture(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("REJECTED")) {
            List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateRejected(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("PAST")) {
            List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStatePast(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else if (state.equals("WAITING")) {
            List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateWaiting(userId, pageWithFromAndSize);
            for (Booking b : bookings) {
                bookingDtoList.add(bookingMapper.bookingDtoOut(b));
            }
            return bookingDtoList;
        } else {
            throw new WrongStateException("Unknown state: " + state);
        }
    }

    @Override
    public BookingDtoOut getBookingById(long bookingId, long userId) {
        return dbBookingStorage.getBookingById(bookingId)
                .filter(booking -> booking.getItem().getOwner().getId() == userId || booking.getBooker().getId() == userId)
                .map(bookingMapper::bookingDtoOut)
                .orElseThrow(() -> new UnknownIdException("Пользователю с id=" + userId + " не принадлежит данная вещь"));
    }

    @Override
    public BookingDtoOut updateBooking(long bookingId, boolean isApproved, long userId) {
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
        return bookingMapper.bookingDtoOut(dbBookingStorage.updateBooking(booking));
    }
}
