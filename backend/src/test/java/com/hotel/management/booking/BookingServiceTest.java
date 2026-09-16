package com.hotel.management.booking;

import com.hotel.management.billing.BillRepository;
import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.dto.BookingResponseDto;
import com.hotel.management.booking.dto.CreateBookingRequest;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.food.FoodCategory;
import com.hotel.management.food.FoodItem;
import com.hotel.management.food.FoodItemRepository;
import com.hotel.management.food.FoodService;
import com.hotel.management.food.dto.CreateFoodOrderRequest;
import com.hotel.management.food.dto.CreateOrderItemRequest;
import com.hotel.management.room.Room;
import com.hotel.management.room.RoomRepository;
import com.hotel.management.room.RoomType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BookingServiceTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private FoodService foodService;

    @Autowired
    private BillRepository billRepository;

    @Test
    void testBookingLifecycleAndBillGeneration() {
        // 1. Create a test room
        Room room = roomRepository.save(new Room(
                "TEST-101",
                RoomType.DELUXE,
                new BigDecimal("200.00"),
                2,
                "Test Deluxe Suite",
                true,
                "http://example.com/test.jpg"
        ));

        // 2. Book room for 3 nights
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);

        CreateBookingRequest bookingReq = new CreateBookingRequest(room.getId(), checkIn, checkOut);
        BookingResponseDto booking = bookingService.createBooking("john.doe@example.com", bookingReq);

        assertNotNull(booking);
        assertEquals(3, booking.getNights());
        assertEquals(new BigDecimal("600.00"), booking.getTotalPrice());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        // 3. Prevent duplicate conflicting booking
        CreateBookingRequest collisionReq = new CreateBookingRequest(room.getId(), checkIn.plusDays(1), checkOut.plusDays(1));
        assertThrows(BadRequestException.class, () ->
                bookingService.createBooking("john.doe@example.com", collisionReq)
        );

        // 4. Guest orders food tied to this booking
        FoodItem dish = foodItemRepository.save(new FoodItem(
                "Test Dumpling",
                "Delicious dumpling",
                FoodCategory.STARTER,
                new BigDecimal("25.00"),
                "http://example.com/food.jpg",
                true,
                15,
                true,
                false
        ));

        CreateFoodOrderRequest orderReq = new CreateFoodOrderRequest();
        orderReq.setBookingId(booking.getId());
        orderReq.setItems(List.of(new CreateOrderItemRequest(dish.getId(), 2, "Mild spice")));
        foodService.createOrder("john.doe@example.com", orderReq);

        // 5. Front-desk Check-in
        BookingResponseDto checkedIn = bookingService.checkIn(booking.getId());
        assertEquals(BookingStatus.CHECKED_IN, checkedIn.getStatus());
        assertNotNull(checkedIn.getActualCheckInAt());

        // 6. Front-desk Check-out with automatic Bill Generation
        BillResponseDto bill = bookingService.checkOut(booking.getId());
        assertNotNull(bill);
        assertEquals("GENERATED", bill.getStatus());

        // Room charge: 3 nights * 200 = 600
        assertEquals(new BigDecimal("600.00"), bill.getRoomCharges());
        // Food charge: 2 * 25 = 50
        assertEquals(new BigDecimal("50.00"), bill.getFoodCharges());
        // Subtotal = 650.00
        assertEquals(new BigDecimal("650.00"), bill.getSubtotal());
        // 12% tax = 78.00
        assertEquals(new BigDecimal("78.00"), bill.getTaxAmount());
        // Total = 728.00
        assertEquals(new BigDecimal("728.00"), bill.getTotalAmount());

        // Bill is persisted
        assertTrue(billRepository.existsByBookingId(booking.getId()));
    }
}
