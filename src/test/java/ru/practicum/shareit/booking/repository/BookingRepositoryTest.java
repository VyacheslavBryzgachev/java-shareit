package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.practicum.shareit.booking.model.Booking;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED;

@DataJpaTest
@Transactional
@Sql(scripts = {"classpath:schemaTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:dataTest.sql"},
        config = @SqlConfig(transactionMode = ISOLATED),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Test
    void getUserBookingsIfStateAllTest() {
        List<Booking> bookings = bookingRepository.getUserBookingsIfStateAll(1, 0, 10);
        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void getUserBookingsIfStateCurrentTest() {
        List<Booking> bookings = bookingRepository.getUserBookingsIfStateCurrent(1, PageRequest.of(0, 10));
        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void getUserBookingsIfStatePastTest() {
        List<Booking> bookings = bookingRepository.getUserBookingsIfStatePast(2, PageRequest.of(0, 10));
        Assertions.assertEquals(0, bookings.size());
    }

    @Test
    void getUserBookingsIfStateFutureTest() {
        List<Booking> bookings = bookingRepository.getUserBookingsIfStateFuture(2, PageRequest.of(0, 10));
        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void getUserBookingsIfStateWaitingTest() {
        List<Booking> bookings = bookingRepository.getUserBookingsIfStateWaiting(2, PageRequest.of(0, 10));
        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void getUserBookingsIfStateRejectedTest() {
        List<Booking> bookings = bookingRepository.getUserBookingsIfStateRejected(2, PageRequest.of(0, 10));
        Assertions.assertEquals(0, bookings.size());
    }

    @Test
    void getUserItemsBookingsIfStateAllTest() {
        List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateAll(2, PageRequest.of(0, 10));
        Assertions.assertEquals(2, bookings.size());
    }

    @Test
    void getUserItemsBookingsIfStateCurrentTest() {
        List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateCurrent(2, PageRequest.of(0, 10));
        Assertions.assertEquals(2, bookings.size());
    }

    @Test
    void getUserItemsBookingsIfStatePastTest() {
        List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStatePast(2, PageRequest.of(0, 10));
        Assertions.assertEquals(0, bookings.size());
    }

    @Test
    void getUserItemsBookingsIfStateFutureTest() {
        List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateFuture(2, PageRequest.of(0, 10));
        Assertions.assertEquals(0, bookings.size());
    }

    @Test
    void getUserItemsBookingsIfStateWaitingTest() {
        List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateWaiting(2, PageRequest.of(0, 10));
        Assertions.assertEquals(1, bookings.size());
    }

    @Test
    void getUserItemsBookingsIfStateRejectedTest() {
        List<Booking> bookings = bookingRepository.getUserItemsBookingsIfStateRejected(2, PageRequest.of(0, 10));
        Assertions.assertEquals(0, bookings.size());
    }

    @Test
    void getBookingByItemIdAndUserIdTest() {
        Optional<Booking> booking = bookingRepository.getBookingByItemIdAndUserId(2, 1);
        Assertions.assertNotNull(booking);
    }

    @Test
    void getLastBookingTest() {
        Booking booking = bookingRepository.getLastBooking(1);
        Assertions.assertNotNull(booking);
    }

    @Test
    void getNextBookingTest() {
        Booking booking = bookingRepository.getNextBooking(1);
        Assertions.assertNotNull(booking);
    }
}
