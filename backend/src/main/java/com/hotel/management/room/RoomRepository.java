package com.hotel.management.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomNumber(String roomNumber);

    boolean existsByRoomNumber(String roomNumber);

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByIsAvailableTrue();

    @Query("SELECT r FROM Room r WHERE r.isAvailable = true " +
           "AND (:roomType IS NULL OR r.roomType = :roomType) " +
           "AND (:minPrice IS NULL OR r.pricePerNight >= :minPrice) " +
           "AND (:maxPrice IS NULL OR r.pricePerNight <= :maxPrice) " +
           "AND (:capacity IS NULL OR r.capacity >= :capacity) " +
           "AND (:checkInDate IS NULL OR :checkOutDate IS NULL OR NOT EXISTS (" +
           "    SELECT b FROM Booking b WHERE b.room = r " +
           "    AND b.status IN ('PENDING', 'CONFIRMED', 'CHECKED_IN') " +
           "    AND b.checkInDate < :checkOutDate " +
           "    AND b.checkOutDate > :checkInDate" +
           "))")
    List<Room> searchRooms(
            @Param("roomType") RoomType roomType,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("capacity") Integer capacity,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );
}
