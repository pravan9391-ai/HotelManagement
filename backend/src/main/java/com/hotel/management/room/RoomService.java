package com.hotel.management.room;

import com.hotel.management.booking.Booking;
import com.hotel.management.booking.BookingRepository;
import com.hotel.management.booking.BookingStatus;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.room.dto.BookedDateRangeDto;
import com.hotel.management.room.dto.CreateRoomRequest;
import com.hotel.management.room.dto.RoomDto;
import com.hotel.management.room.dto.UpdateRoomRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::enrichRoomDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        return enrichRoomDto(room);
    }

    @Transactional(readOnly = true)
    public List<RoomDto> searchRooms(RoomType roomType, BigDecimal minPrice, BigDecimal maxPrice,
                                     Integer capacity, LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate != null && checkOutDate != null && !checkOutDate.isAfter(checkInDate)) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        List<Room> rooms = roomRepository.searchRooms(roomType, minPrice, maxPrice, capacity, checkInDate, checkOutDate);
        return rooms.stream()
                .map(this::enrichRoomDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public RoomDto createRoom(CreateRoomRequest request) {
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new BadRequestException("Room number " + request.getRoomNumber() + " already exists");
        }

        Room room = new Room(
                request.getRoomNumber(),
                request.getRoomType(),
                request.getPricePerNight(),
                request.getCapacity(),
                request.getDescription(),
                request.getIsAvailable(),
                request.getImageUrl()
        );

        Room saved = roomRepository.save(room);
        return enrichRoomDto(saved);
    }

    @Transactional
    public RoomDto updateRoom(Long id, UpdateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));

        if (request.getRoomNumber() != null && !request.getRoomNumber().equals(room.getRoomNumber())) {
            if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
                throw new BadRequestException("Room number " + request.getRoomNumber() + " already exists");
            }
            room.setRoomNumber(request.getRoomNumber());
        }

        if (request.getRoomType() != null) {
            room.setRoomType(request.getRoomType());
        }
        if (request.getPricePerNight() != null) {
            room.setPricePerNight(request.getPricePerNight());
        }
        if (request.getCapacity() != null) {
            room.setCapacity(request.getCapacity());
        }
        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }
        if (request.getIsAvailable() != null) {
            room.setIsAvailable(request.getIsAvailable());
        }
        if (request.getImageUrl() != null) {
            room.setImageUrl(request.getImageUrl());
        }

        Room updated = roomRepository.save(room);
        return enrichRoomDto(updated);
    }

    @Transactional
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + id));
        roomRepository.delete(room);
    }

    private RoomDto enrichRoomDto(Room room) {
        RoomDto dto = RoomDto.fromEntity(room);
        LocalDate today = LocalDate.now();

        List<BookingStatus> activeStatuses = Arrays.asList(
                BookingStatus.PENDING,
                BookingStatus.CONFIRMED,
                BookingStatus.CHECKED_IN
        );

        List<Booking> upcomingAndActive = bookingRepository.findUpcomingAndActiveByRoom(room, activeStatuses, today);

        List<BookedDateRangeDto> bookedDateRanges = upcomingAndActive.stream()
                .map(b -> new BookedDateRangeDto(b.getCheckInDate(), b.getCheckOutDate(), b.getStatus().name()))
                .collect(Collectors.toList());
        dto.setBookedDates(bookedDateRanges);

        // Calculate availability status
        if (!Boolean.TRUE.equals(room.getIsAvailable())) {
            dto.setAvailabilityStatus("unavailable");
        } else {
            // Check if occupied or booked today
            Booking currentBooking = upcomingAndActive.stream()
                    .filter(b -> !today.isBefore(b.getCheckInDate()) && today.isBefore(b.getCheckOutDate()))
                    .findFirst()
                    .orElse(null);

            if (currentBooking != null) {
                if (currentBooking.getStatus() == BookingStatus.CHECKED_IN) {
                    dto.setAvailabilityStatus("occupied");
                } else if (currentBooking.getStatus() == BookingStatus.PENDING) {
                    dto.setAvailabilityStatus("processing");
                } else {
                    dto.setAvailabilityStatus("booked");
                }
            } else {
                // Check if reserved in the future
                boolean hasFuture = upcomingAndActive.stream()
                        .anyMatch(b -> b.getCheckInDate().isAfter(today));
                dto.setAvailabilityStatus(hasFuture ? "reserved" : "available");
            }
        }

        // Calculate next available date
        LocalDate nextAvailable = today;
        for (Booking b : upcomingAndActive) {
            if (b.getCheckOutDate().isAfter(nextAvailable)) {
                nextAvailable = b.getCheckOutDate();
            }
        }
        dto.setNextAvailableDate(nextAvailable);

        return dto;
    }
}
