package com.hotel.management.booking;

import com.hotel.management.billing.BillRepository;
import com.hotel.management.billing.BillingService;
import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.dto.BookingResponseDto;
import com.hotel.management.booking.dto.CreateBookingRequest;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.exception.UnauthorizedException;
import com.hotel.management.room.Room;
import com.hotel.management.room.RoomRepository;
import com.hotel.management.user.Role;
import com.hotel.management.user.User;
import com.hotel.management.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final BillRepository billRepository;
    private final BillingService billingService;

    public BookingService(BookingRepository bookingRepository,
                          RoomRepository roomRepository,
                          UserRepository userRepository,
                          BillRepository billRepository,
                          BillingService billingService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.billRepository = billRepository;
        this.billingService = billingService;
    }

    @Transactional
    public BookingResponseDto createBooking(String userEmail, CreateBookingRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + userEmail));

        LocalDate checkInDate = request.getCheckInDate();
        LocalDate checkOutDate = request.getCheckOutDate();
        LocalDate today = LocalDate.now();

        if (checkOutDate == null || checkInDate == null) {
            throw new BadRequestException("Check-in and check-out dates are required");
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new BadRequestException("Check-out date must be strictly after check-in date");
        }

        if (checkInDate.isBefore(today)) {
            throw new BadRequestException("Check-in date cannot be in the past");
        }

        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights > 365) {
            throw new BadRequestException("Stay duration cannot exceed 365 days");
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + request.getRoomId()));

        if (!Boolean.TRUE.equals(room.getIsAvailable())) {
            throw new BadRequestException("Room " + room.getRoomNumber() + " is currently unavailable for booking");
        }

        boolean hasCollision = bookingRepository.existsConflictingBooking(room, checkInDate, checkOutDate);
        if (hasCollision) {
            throw new BadRequestException("Room " + room.getRoomNumber() + " is already booked for the selected dates");
        }

        BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking(
                user,
                room,
                checkInDate,
                checkOutDate,
                totalPrice,
                BookingStatus.CONFIRMED
        );

        Booking saved = bookingRepository.save(booking);
        return BookingResponseDto.fromEntity(saved, false);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + userEmail));

        return bookingRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(b -> BookingResponseDto.fromEntity(b, billRepository.existsByBookingId(b.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(String userEmail, Long bookingId, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (!isAdmin && !booking.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have permission to view this booking");
        }

        return BookingResponseDto.fromEntity(booking, billRepository.existsByBookingId(booking.getId()));
    }

    @Transactional
    public BookingResponseDto cancelBooking(String userEmail, Long bookingId, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (!isAdmin && !booking.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have permission to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_IN || booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BadRequestException("Cannot cancel booking with status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);
        return BookingResponseDto.fromEntity(saved, false);
    }

    @Transactional(readOnly = true)
    public List<BookingResponseDto> getAllBookings(BookingStatus statusFilter) {
        List<Booking> bookings;
        if (statusFilter != null) {
            bookings = bookingRepository.findByStatusOrderByCreatedAtDesc(statusFilter);
        } else {
            bookings = bookingRepository.findAllByOrderByCreatedAtDesc();
        }

        return bookings.stream()
                .map(b -> BookingResponseDto.fromEntity(b, billRepository.existsByBookingId(b.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseDto reassignRoom(Long bookingId, Long newRoomId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BadRequestException("Cannot reassign room for a booking that is " + booking.getStatus());
        }

        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() -> new ResourceNotFoundException("Target room not found with ID: " + newRoomId));

        if (!Boolean.TRUE.equals(newRoom.getIsAvailable())) {
            throw new BadRequestException("Target room " + newRoom.getRoomNumber() + " is marked unavailable");
        }

        boolean hasCollision = bookingRepository.existsConflictingBookingExcluding(
                newRoom,
                booking.getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()
        );

        if (hasCollision) {
            throw new BadRequestException("Room " + newRoom.getRoomNumber() + " has conflicting bookings for dates " +
                    booking.getCheckInDate() + " to " + booking.getCheckOutDate());
        }

        booking.setRoom(newRoom);
        long nights = Math.max(booking.getNights(), 1);
        booking.setTotalPrice(newRoom.getPricePerNight().multiply(BigDecimal.valueOf(nights)));

        Booking saved = bookingRepository.save(booking);
        return BookingResponseDto.fromEntity(saved, billRepository.existsByBookingId(saved.getId()));
    }

    @Transactional
    public BookingResponseDto checkIn(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot check in a cancelled booking");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            throw new BadRequestException("Guest is already checked in");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BadRequestException("Cannot check in a booking that has already checked out");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        booking.setActualCheckInAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);
        return BookingResponseDto.fromEntity(saved, false);
    }

    @Transactional
    public BillResponseDto checkOut(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot check out a cancelled booking");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            // If already checked out, return existing generated bill
            return billingService.generateBillForBooking(booking);
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        booking.setActualCheckOutAt(LocalDateTime.now());
        Booking saved = bookingRepository.save(booking);

        // Transactional automatic bill generation
        return billingService.generateBillForBooking(saved);
    }
}
