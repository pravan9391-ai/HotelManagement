package com.hotel.management.billing;

import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.Booking;
import com.hotel.management.booking.BookingRepository;
import com.hotel.management.config.AppProperties;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.exception.UnauthorizedException;
import com.hotel.management.food.FoodOrder;
import com.hotel.management.food.FoodOrderRepository;
import com.hotel.management.food.FoodOrderStatus;
import com.hotel.management.food.dto.FoodOrderDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final BillRepository billRepository;
    private final BookingRepository bookingRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final AppProperties appProperties;

    public BillingService(BillRepository billRepository,
                          BookingRepository bookingRepository,
                          FoodOrderRepository foodOrderRepository,
                          AppProperties appProperties) {
        this.billRepository = billRepository;
        this.bookingRepository = bookingRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.appProperties = appProperties;
    }

    @Transactional
    public BillResponseDto generateBillForBooking(Booking booking) {
        // Check if bill already exists for this booking
        Bill existingBill = billRepository.findByBooking(booking).orElse(null);
        if (existingBill != null) {
            List<FoodOrderDto> orders = foodOrderRepository.findByBookingAndStatusNot(booking, FoodOrderStatus.CANCELLED)
                    .stream()
                    .map(FoodOrderDto::fromEntity)
                    .collect(Collectors.toList());
            return BillResponseDto.fromEntity(existingBill, orders);
        }

        // Calculate room charges
        long nights = Math.max(booking.getNights(), 1);
        BigDecimal roomRate = booking.getRoom().getPricePerNight();
        BigDecimal roomCharges = roomRate.multiply(BigDecimal.valueOf(nights)).setScale(2, RoundingMode.HALF_UP);

        // Calculate food charges from linked food orders (excluding cancelled)
        List<FoodOrder> foodOrders = foodOrderRepository.findByBookingAndStatusNot(booking, FoodOrderStatus.CANCELLED);
        BigDecimal foodCharges = foodOrders.stream()
                .map(FoodOrder::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate tax
        BigDecimal taxRate = appProperties.getBilling().getTaxRate();
        BigDecimal subtotal = roomCharges.add(foodCharges);
        BigDecimal taxAmount = subtotal.multiply(taxRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = subtotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        Bill bill = new Bill(booking, roomCharges, foodCharges, taxRate, taxAmount, totalAmount);
        Bill saved = billRepository.save(bill);

        List<FoodOrderDto> orderDtos = foodOrders.stream()
                .map(FoodOrderDto::fromEntity)
                .collect(Collectors.toList());

        return BillResponseDto.fromEntity(saved, orderDtos);
    }

    @Transactional(readOnly = true)
    public BillResponseDto getBillForBooking(Long bookingId, String requestingUserEmail, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (!isAdmin && !booking.getUser().getEmail().equals(requestingUserEmail)) {
            throw new UnauthorizedException("You do not have permission to view this bill");
        }

        Bill bill = billRepository.findByBooking(booking)
                .orElseThrow(() -> new ResourceNotFoundException("Bill has not been generated for booking #" + bookingId + " yet (bills are generated automatically upon front-desk checkout)"));

        List<FoodOrderDto> orders = foodOrderRepository.findByBookingAndStatusNot(booking, FoodOrderStatus.CANCELLED)
                .stream()
                .map(FoodOrderDto::fromEntity)
                .collect(Collectors.toList());

        return BillResponseDto.fromEntity(bill, orders);
    }
}
