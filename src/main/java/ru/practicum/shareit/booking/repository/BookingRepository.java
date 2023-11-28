package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * FROM bookings WHERE booker_id = :userId order by id desc limit :from offset :size", nativeQuery = true)
    List<Booking> getUserBookingsIfStateAll(@Param("userId") long userId, @Param("size") int size, @Param("from") int from);

    @Query(value = "SELECT b FROM Booking b where b.start < NOW() and b.end > NOW() and b.booker.id = :userId order by b.id ")
    List<Booking> getUserBookingsIfStateCurrent(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b WHERE b.end < NOW() and b.booker.id = :userId order by b.id desc")
    List<Booking> getUserBookingsIfStatePast(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b WHERE b.start > NOW() and b.booker.id = :userId order by b.id desc")
    List<Booking> getUserBookingsIfStateFuture(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b WHERE b.status = 'WAITING' and b.booker.id = :userId order by b.id desc")
    List<Booking> getUserBookingsIfStateWaiting(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b WHERE b.status = 'REJECTED' and b.booker.id = :userId order by b.id desc")
    List<Booking> getUserBookingsIfStateRejected(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :userId order by b.id DESC")
    List<Booking> getUserItemsBookingsIfStateAll(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :userId and b.start < NOW() and b.end > NOW() order by b.id ")
    List<Booking> getUserItemsBookingsIfStateCurrent(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :userId and b.end < NOW() order by b.id desc")
    List<Booking> getUserItemsBookingsIfStatePast(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :userId and b.start > NOW() order by b.id desc")
    List<Booking> getUserItemsBookingsIfStateFuture(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :userId and b.status = 'WAITING' order by b.id desc")
    List<Booking> getUserItemsBookingsIfStateWaiting(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT b FROM Booking b JOIN b.item i WHERE i.owner.id = :userId and b.status = 'REJECTED' order by b.id desc")
    List<Booking> getUserItemsBookingsIfStateRejected(@Param("userId") long userId, Pageable pageable);

    @Query(value = "SELECT * FROM bookings b\n" +
            "left join items i on b.item_id = i.id\n" +
            "left join users u on u.id = b.booker_id where b.item_id = :itemId and b.booker_id = :userId and b.status = 'APPROVED' limit 1", nativeQuery = true)
    Optional<Booking> getBookingByItemIdAndUserId(@Param("userId") long userId, @Param("itemId") long itemId);

    @Query(value = "SELECT * from bookings where item_id = :itemId and start_booking < current_timestamp order by start_booking desc limit 1", nativeQuery = true)
    Booking getLastBooking(@Param("itemId") long itemId);

    @Query(value = "SELECT * from bookings where item_id = :itemId and start_booking > current_timestamp order by start_booking limit 1", nativeQuery = true)
    Booking getNextBooking(@Param("itemId") long itemId);

}
