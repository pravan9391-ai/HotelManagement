package com.hotel.management.booking;

import com.hotel.management.billing.BillingService;
import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.dto.BookingResponseDto;
import com.hotel.management.booking.dto.CreateBookingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings", description = "Customer endpoints for room reservations, cancellations, and bill access")
public class BookingController {

    private final BookingService bookingService;
    private final BillingService billingService;

    public BookingController(BookingService bookingService, BillingService billingService) {
        this.bookingService = bookingService;
        this.billingService = billingService;
    }

    @PostMapping
    @Operation(summary = "Create room reservation", description = "Customer books a room for selected check-in and check-out dates")
    public ResponseEntity<BookingResponseDto> createBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateBookingRequest request) {
        BookingResponseDto booking = bookingService.createBooking(userDetails.getUsername(), request);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @GetMapping("/my-bookings")
    @Operation(summary = "Get customer reservations", description = "Returns all reservations made by the authenticated customer")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(bookingService.getUserBookings(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking details", description = "Returns details of a specific reservation")
    public ResponseEntity<BookingResponseDto> getBookingById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(userDetails.getUsername(), id, false));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel booking", description = "Cancels a pending or confirmed booking before check-in")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(userDetails.getUsername(), id, false));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel booking (RESTful alias)", description = "Cancels reservation using HTTP DELETE")
    public ResponseEntity<BookingResponseDto> deleteBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(userDetails.getUsername(), id, false));
    }

    @GetMapping("/{id}/bill")
    @Operation(summary = "Get official bill", description = "Returns generated itemized invoice for records after front-desk checkout")
    public ResponseEntity<BillResponseDto> getBill(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(billingService.getBillForBooking(id, userDetails.getUsername(), false));
    }
}
