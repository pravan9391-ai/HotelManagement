package com.hotel.management.booking;

import com.hotel.management.room.Room;
import com.hotel.management.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserOrderByCreatedAtDesc(User user);

    List<Booking> findByUserAndStatusNotOrderByCreatedAtDesc(User user, BookingStatus status);

    List<Booking> findByRoomAndStatusIn(Room room, Collection<BookingStatus> statuses);

    @Query("SELECT b FROM Booking b WHERE b.room = :room AND b.status IN :statuses AND b.checkOutDate > :date ORDER BY b.checkInDate ASC")
    List<Booking> findUpcomingAndActiveByRoom(
            @Param("room") Room room,
            @Param("statuses") Collection<BookingStatus> statuses,
            @Param("date") LocalDate date
    );

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.room = :room " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
           "AND b.checkInDate < :checkOutDate " +
           "AND b.checkOutDate > :checkInDate")
    boolean existsConflictingBooking(
            @Param("room") Room room,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
           "WHERE b.room = :room " +
           "AND b.id <> :bookingId " +
           "AND b.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
           "AND b.checkInDate < :checkOutDate " +
           "AND b.checkOutDate > :checkInDate")
    boolean existsConflictingBookingExcluding(
            @Param("room") Room room,
            @Param("bookingId") Long bookingId,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

    List<Booking> findAllByOrderByCreatedAtDesc();

    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);

    List<Booking> findByUser(User user);
}
