package com.hotel.management.food;

import com.hotel.management.booking.Booking;
import com.hotel.management.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {
    List<FoodOrder> findByUserOrderByCreatedAtDesc(User user);
    List<FoodOrder> findByBooking(Booking booking);
    List<FoodOrder> findByBookingAndStatusNot(Booking booking, FoodOrderStatus status);
    List<FoodOrder> findAllByOrderByCreatedAtDesc();
}
