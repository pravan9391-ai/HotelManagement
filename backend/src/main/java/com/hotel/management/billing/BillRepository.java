package com.hotel.management.billing;

import com.hotel.management.booking.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBooking(Booking booking);
    Optional<Bill> findByBookingId(Long bookingId);
    boolean existsByBookingId(Long bookingId);
}
