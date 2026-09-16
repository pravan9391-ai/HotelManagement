package com.hotel.management.food;

import com.hotel.management.booking.Booking;
import com.hotel.management.booking.BookingRepository;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.ResourceNotFoundException;
import com.hotel.management.exception.UnauthorizedException;
import com.hotel.management.food.dto.*;
import com.hotel.management.user.Role;
import com.hotel.management.user.User;
import com.hotel.management.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodItemRepository foodItemRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public FoodService(FoodItemRepository foodItemRepository,
                       FoodOrderRepository foodOrderRepository,
                       UserRepository userRepository,
                       BookingRepository bookingRepository) {
        this.foodItemRepository = foodItemRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<FoodItemDto> getAllFoodItems(FoodCategory category) {
        List<FoodItem> items;
        if (category != null) {
            items = foodItemRepository.findByCategoryAndAvailableTrue(category);
        } else {
            items = foodItemRepository.findByAvailableTrue();
        }
        return items.stream().map(FoodItemDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FoodItemDto getFoodItemById(Long id) {
        FoodItem item = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with ID: " + id));
        return FoodItemDto.fromEntity(item);
    }

    @Transactional
    public FoodItemDto createFoodItem(CreateFoodItemRequest request) {
        FoodItem item = new FoodItem(
                request.getName(),
                request.getDescription(),
                request.getCategory(),
                request.getPrice(),
                request.getImageUrl(),
                request.getAvailable(),
                request.getPreparationTime(),
                request.getIsVegetarian(),
                request.getIsVegan()
        );
        FoodItem saved = foodItemRepository.save(item);
        return FoodItemDto.fromEntity(saved);
    }

    @Transactional
    public FoodItemDto updateFoodItem(Long id, CreateFoodItemRequest request) {
        FoodItem item = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with ID: " + id));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        item.setAvailable(request.getAvailable());
        item.setPreparationTime(request.getPreparationTime());
        item.setIsVegetarian(request.getIsVegetarian());
        item.setIsVegan(request.getIsVegan());

        FoodItem updated = foodItemRepository.save(item);
        return FoodItemDto.fromEntity(updated);
    }

    @Transactional
    public void deleteFoodItem(Long id) {
        FoodItem item = foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with ID: " + id));
        foodItemRepository.delete(item);
    }

    @Transactional
    public FoodOrderDto createOrder(String userEmail, CreateFoodOrderRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + userEmail));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one food item");
        }

        Booking booking = null;
        if (request.getBookingId() != null) {
            booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + request.getBookingId()));

            if (!booking.getUser().getId().equals(user.getId()) && user.getRole() != Role.ROLE_ADMIN) {
                throw new BadRequestException("You can only link food orders to your own active bookings");
            }
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        FoodOrder order = new FoodOrder(user, booking, FoodOrderStatus.ORDER_CONFIRMED, BigDecimal.ZERO, request.getSpecialInstructions());

        for (CreateOrderItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() == null || itemReq.getQuantity() <= 0) {
                throw new BadRequestException("Quantity must be greater than zero");
            }

            FoodItem foodItem = foodItemRepository.findById(itemReq.getFoodItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food item not found with ID: " + itemReq.getFoodItemId()));

            if (!Boolean.TRUE.equals(foodItem.getAvailable())) {
                throw new BadRequestException("Food item '" + foodItem.getName() + "' is currently unavailable");
            }

            BigDecimal itemTotal = foodItem.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            totalPrice = totalPrice.add(itemTotal);

            OrderItem orderItem = new OrderItem(order, foodItem, itemReq.getQuantity(), foodItem.getPrice(), itemReq.getNotes());
            order.addItem(orderItem);
        }

        order.setTotalPrice(totalPrice);
        FoodOrder saved = foodOrderRepository.save(order);
        return FoodOrderDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<FoodOrderDto> getUserOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UnauthorizedException("User not found: " + userEmail));

        return foodOrderRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(FoodOrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FoodOrderDto getOrderById(String userEmail, Long orderId, boolean isAdmin) {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Food order not found with ID: " + orderId));

        if (!isAdmin && !order.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You do not have permission to view this order");
        }

        return FoodOrderDto.fromEntity(order);
    }

    @Transactional
    public FoodOrderDto cancelOrder(String userEmail, Long orderId) {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Food order not found with ID: " + orderId));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new UnauthorizedException("You can only cancel your own food orders");
        }

        if (order.getStatus() != FoodOrderStatus.ORDER_CONFIRMED) {
            throw new BadRequestException("Cannot cancel order with status: " + order.getStatus());
        }

        order.setStatus(FoodOrderStatus.CANCELLED);
        FoodOrder saved = foodOrderRepository.save(order);
        return FoodOrderDto.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public List<FoodOrderDto> getAllOrders() {
        return foodOrderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(FoodOrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public FoodOrderDto updateOrderStatus(Long orderId, FoodOrderStatus newStatus) {
        FoodOrder order = foodOrderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Food order not found with ID: " + orderId));

        order.setStatus(newStatus);
        FoodOrder saved = foodOrderRepository.save(order);
        return FoodOrderDto.fromEntity(saved);
    }
}
