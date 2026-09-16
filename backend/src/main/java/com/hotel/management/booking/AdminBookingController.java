package com.hotel.management.booking;

import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.dto.BookingResponseDto;
import com.hotel.management.booking.dto.ReassignRoomRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Bookings", description = "Admin-only front-desk endpoints for managing reservations, room reassignments, check-in, and check-out")
public class AdminBookingController {

    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    @Operation(summary = "View all bookings", description = "List all reservations across the hotel, optionally filtered by status (Admin only)")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(@RequestParam(required = false) BookingStatus status) {
        return ResponseEntity.ok(bookingService.getAllBookings(status));
    }

    @PutMapping("/{id}/reassign-room")
    @Operation(summary = "Reassign guest room", description = "Move a reservation to another available room with date validation (Admin only)")
    public ResponseEntity<BookingResponseDto> reassignRoom(
            @PathVariable Long id,
            @Valid @RequestBody ReassignRoomRequest request) {
        return ResponseEntity.ok(bookingService.reassignRoom(id, request.getNewRoomId()));
    }

    @PostMapping("/{id}/check-in")
    @Operation(summary = "Front-desk Check-In", description = "Record guest arrival, set actual check-in timestamp, mark status CHECKED_IN (Admin only)")
    public ResponseEntity<BookingResponseDto> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkIn(id));
    }

    @PostMapping("/{id}/check-out")
    @Operation(summary = "Front-desk Check-Out & Bill Generation", description = "Record departure, mark status CHECKED_OUT, free room, and automatically compute & generate official bill (Admin only)")
    public ResponseEntity<BillResponseDto> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkOut(id));
    }
}
