package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.enums.Status;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.exceptions.UnknownIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@SpringBootTest
@Sql(scripts = {"classpath:schemaTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:dataTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase
public class BookingServiceImplTest {

    @Autowired
    BookingService bookingService;

    @Test
    void createBookingReturnValidBookingIfValidArgument() {
        BookingDtoOut expected = BookingDtoOut
                .builder()
                .id(3)
                .booker(User
                        .builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .item(Item
                        .builder()
                        .id(3)
                        .owner(User
                                .builder()
                                .id(2)
                                .name("User2")
                                .email("User2@mail.ru")
                                .build())
                        .name("Item3")
                        .description("Item3Desc")
                        .available(true)
                        .requestId(0)
                        .build())
                .start(LocalDateTime.of(2023, 11, 10, 15, 30, 15))
                .end(LocalDateTime.of(2023, 11, 11, 15, 30, 15))
                .status(Status.WAITING)
                .build();

        BookingDto bookingDto = BookingDto
                .builder()
                .bookerId(1)
                .itemId(3)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2023, 11, 10, 15, 30, 15))
                .end(LocalDateTime.of(2023, 11, 11, 15, 30, 15))
                .build();
        BookingDtoOut actual = bookingService.createBooking(bookingDto, 1);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createBookingThrowExceptionIfBookerEqualsOwnerItem() {
        BookingDto bookingDto = BookingDto
                .builder()
                .bookerId(1)
                .itemId(1)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2023, 11, 10, 15, 30, 15))
                .end(LocalDateTime.of(2023, 11, 11, 15, 30, 15))
                .build();
        UnknownIdException exception = Assertions.assertThrows(UnknownIdException.class,
                () -> bookingService.createBooking(bookingDto, 1));
        Assertions.assertEquals(exception.getMessage(), "Нельзя забронировать свою вещь");
    }

    @Test
    void createBookingThrowExceptionsIfItemAvailableFalse() {
        BookingDto bookingDto = BookingDto
                .builder()
                .bookerId(1)
                .itemId(2)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2023, 11, 10, 15, 30, 15))
                .end(LocalDateTime.of(2023, 11, 11, 15, 30, 15))
                .build();
        BookingException exception = Assertions.assertThrows(BookingException.class,
                () -> bookingService.createBooking(bookingDto, 1));
        Assertions.assertEquals(exception.getMessage(), "Вещь недоступна для бронирования");
    }

    @Test
    void getBookingByIdReturnValidBookingIfValidArguments() {
        BookingDtoOut expected = BookingDtoOut
                .builder()
                .id(1)
                .booker(User
                        .builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .item(Item
                        .builder()
                        .id(1)
                        .owner(User
                                .builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(0)
                        .build())
                .start(LocalDateTime.of(2023,11,24,15,35,30))
                .end(LocalDateTime.of(2023,12,24,15,35,30))
                .status(Status.WAITING)
                .build();
        BookingDtoOut actual = bookingService.getBookingById(1, 1);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getUserBookingsReturnValidListIfValidArguments() {
        List<BookingDtoOut> bookingDtoOuts = new ArrayList<>();
        BookingDtoOut bookingDto = BookingDtoOut
                .builder()
                .id(1)
                .booker(User
                        .builder()
                        .id(1)
                        .name("User1")
                        .email("User1@mail.ru")
                        .build())
                .item(Item
                        .builder()
                        .id(1)
                        .owner(User
                                .builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(0)
                        .build())
                .start(LocalDateTime.of(2023,11,24,15,35,30))
                .end(LocalDateTime.of(2023,12,24,15,35,30))
                .status(Status.WAITING)
                .build();
        bookingDtoOuts.add(bookingDto);
        List<BookingDtoOut> actual = bookingService.getUserBookings("ALL", 1, 0, 2);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bookingDtoOuts.size(), actual.size());
        Assertions.assertEquals(bookingDtoOuts.get(0), actual.get(0));
    }

    @Test
    void getUserItemsBookingsReturnValidListIfValidArguments() {
        List<BookingDtoOut> bookingDtoOuts = new ArrayList<>();
        BookingDtoOut bookingDto1 = BookingDtoOut
                .builder()
                .id(2)
                .booker(User
                        .builder()
                        .id(2)
                        .name("User2")
                        .email("User2@mail.ru")
                        .build())
                .item(Item
                        .builder()
                        .id(1)
                        .owner(User
                                .builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(0)
                        .build())
                .start(LocalDateTime.of(2023,11,24,15,35,30))
                .end(LocalDateTime.of(2023,12,24,15,35,30))
                .status(Status.APPROVED)
                .build();
        BookingDtoOut bookingDto2 = BookingDtoOut
                .builder()
                .id(2)
                .booker(User
                        .builder()
                        .id(2)
                        .name("User2")
                        .email("User2@mail.ru")
                        .build())
                .item(Item
                        .builder()
                        .id(1)
                        .owner(User
                                .builder()
                                .id(1)
                                .name("User1")
                                .email("User1@mail.ru")
                                .build())
                        .name("Item1")
                        .description("Item1Desc")
                        .available(true)
                        .requestId(0)
                        .build())
                .start(LocalDateTime.of(2023,11,24,15,35,30))
                .end(LocalDateTime.of(2023,12,24,15,35,30))
                .status(Status.APPROVED)
                .build();
        bookingDtoOuts.add(bookingDto1);
        bookingDtoOuts.add(bookingDto2);
        List<BookingDtoOut> actual = bookingService.getUserItemsBookings("ALL", 1, 0, 2);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(bookingDtoOuts.size(), actual.size());
        Assertions.assertEquals(bookingDtoOuts.get(0), actual.get(0));
    }

    @Test
    void updateBookingSetStatusApprovedIfArgumentTrue() {
        BookingDtoOut bookingDto = bookingService.updateBooking(1, true, 1);
        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(Status.APPROVED, bookingDto.getStatus());
    }

    @Test
    void updateBookingSetStatusRejectedIfArgumentFalse() {
        BookingDtoOut bookingDto = bookingService.updateBooking(1, false, 1);
        Assertions.assertNotNull(bookingDto);
        Assertions.assertEquals(Status.REJECTED, bookingDto.getStatus());
    }
}
