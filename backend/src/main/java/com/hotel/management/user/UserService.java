package com.hotel.management.user;

import com.hotel.management.billing.Bill;
import com.hotel.management.billing.BillRepository;
import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.Booking;
import com.hotel.management.booking.BookingRepository;
import com.hotel.management.booking.BookingStatus;
import com.hotel.management.booking.dto.BookingResponseDto;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.food.FoodOrderRepository;
import com.hotel.management.food.FoodOrderStatus;
import com.hotel.management.food.dto.FoodOrderDto;
import com.hotel.management.user.dto.CreateAdminUserRequest;
import com.hotel.management.user.dto.CustomerDetailDto;
import com.hotel.management.user.dto.UserSummaryDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BillRepository billRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       BookingRepository bookingRepository,
                       BillRepository billRepository,
                       FoodOrderRepository foodOrderRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.billRepository = billRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserSummaryDto getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        return UserSummaryDto.fromEntity(user);
    }

    @Transactional
    public UserSummaryDto createAdminUser(CreateAdminUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber(),
                request.getRole() != null ? request.getRole() : Role.ROLE_ADMIN
        );

        User saved = userRepository.save(user);
        return UserSummaryDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomerDetailDto> getAllCustomers() {
        List<User> customers = userRepository.findByRole(Role.ROLE_CUSTOMER);
        return customers.stream().map(this::buildCustomerSummary).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDetailDto getCustomerById(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        return buildCustomerSummary(customer);
    }

    private CustomerDetailDto buildCustomerSummary(User customer) {
        CustomerDetailDto dto = CustomerDetailDto.fromUser(customer);

        List<Booking> customerBookings = bookingRepository.findByUserOrderByCreatedAtDesc(customer);

        List<BookingResponseDto> bookingDtos = customerBookings.stream()
                .map(b -> BookingResponseDto.fromEntity(b, billRepository.existsByBookingId(b.getId())))
                .collect(Collectors.toList());
        dto.setBookings(bookingDtos);

        // Find current stay / active booking
        Booking activeStay = customerBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CHECKED_IN || b.getStatus() == BookingStatus.CONFIRMED)
                .findFirst()
                .orElse(null);

        if (activeStay != null) {
            dto.setCurrentOccupiedRoom(activeStay.getRoom().getRoomNumber() + " (" + activeStay.getRoom().getRoomType() + ")");
            dto.setCurrentBookingStatus(activeStay.getStatus().name());
            dto.setStayDates(activeStay.getCheckInDate() + " to " + activeStay.getCheckOutDate());
        } else if (!customerBookings.isEmpty()) {
            Booking latest = customerBookings.get(0);
            dto.setCurrentOccupiedRoom(latest.getRoom().getRoomNumber());
            dto.setCurrentBookingStatus(latest.getStatus().name());
            dto.setStayDates(latest.getCheckInDate() + " to " + latest.getCheckOutDate());
        } else {
            dto.setCurrentOccupiedRoom("None");
            dto.setCurrentBookingStatus("No Reservations");
            dto.setStayDates("N/A");
        }

        // Fetch bills
        List<BillResponseDto> billDtos = new ArrayList<>();
        for (Booking b : customerBookings) {
            billRepository.findByBooking(b).ifPresent(bill -> {
                List<FoodOrderDto> orders = foodOrderRepository.findByBookingAndStatusNot(b, FoodOrderStatus.CANCELLED)
                        .stream()
                        .map(FoodOrderDto::fromEntity)
                        .collect(Collectors.toList());
                billDtos.add(BillResponseDto.fromEntity(bill, orders));
            });
        }
        dto.setBills(billDtos);

        return dto;
    }
}
