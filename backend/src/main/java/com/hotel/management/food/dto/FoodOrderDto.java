package com.hotel.management.food.dto;

import com.hotel.management.food.FoodOrder;
import com.hotel.management.food.FoodOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FoodOrderDto {
    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long bookingId;
    private String roomNumber;
    private FoodOrderStatus status;
    private BigDecimal totalPrice;
    private String specialInstructions;
    private List<OrderItemDto> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FoodOrderDto() {
    }

    public static FoodOrderDto fromEntity(FoodOrder order) {
        if (order == null) return null;
        FoodOrderDto dto = new FoodOrderDto();
        dto.setId(order.getId());
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
            dto.setUserName(order.getUser().getUsername());
            dto.setUserEmail(order.getUser().getEmail());
        }
        if (order.getBooking() != null) {
            dto.setBookingId(order.getBooking().getId());
            if (order.getBooking().getRoom() != null) {
                dto.setRoomNumber(order.getBooking().getRoom().getRoomNumber());
            }
        }
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setSpecialInstructions(order.getSpecialInstructions());
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream()
                    .map(OrderItemDto::fromEntity)
                    .collect(Collectors.toList()));
        }
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public FoodOrderStatus getStatus() {
        return status;
    }

    public void setStatus(FoodOrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
