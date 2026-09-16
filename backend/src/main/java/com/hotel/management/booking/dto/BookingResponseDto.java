package com.hotel.management.booking.dto;

import com.hotel.management.booking.Booking;
import com.hotel.management.booking.BookingStatus;
import com.hotel.management.room.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponseDto {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Long roomId;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal roomPricePerNight;
    private Integer roomCapacity;
    private String roomImageUrl;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime actualCheckInAt;
    private LocalDateTime actualCheckOutAt;
    private long nights;
    private BigDecimal totalPrice;
    private BookingStatus status;
    private boolean hasBill;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookingResponseDto() {
    }

    public static BookingResponseDto fromEntity(Booking booking) {
        return fromEntity(booking, false);
    }

    public static BookingResponseDto fromEntity(Booking booking, boolean hasBill) {
        if (booking == null) return null;
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());

        if (booking.getUser() != null) {
            dto.setUserId(booking.getUser().getId());
            dto.setUserName(booking.getUser().getUsername());
            dto.setUserEmail(booking.getUser().getEmail());
            dto.setUserPhone(booking.getUser().getPhoneNumber());
        }

        if (booking.getRoom() != null) {
            dto.setRoomId(booking.getRoom().getId());
            dto.setRoomNumber(booking.getRoom().getRoomNumber());
            dto.setRoomType(booking.getRoom().getRoomType());
            dto.setRoomPricePerNight(booking.getRoom().getPricePerNight());
            dto.setRoomCapacity(booking.getRoom().getCapacity());
            dto.setRoomImageUrl(booking.getRoom().getImageUrl());
        }

        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setActualCheckInAt(booking.getActualCheckInAt());
        dto.setActualCheckOutAt(booking.getActualCheckOutAt());
        dto.setNights(booking.getNights());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        dto.setHasBill(hasBill);
        dto.setCancelledAt(booking.getCancelledAt());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(BigDecimal roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
    }

    public Integer getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getRoomImageUrl() {
        return roomImageUrl;
    }

    public void setRoomImageUrl(String roomImageUrl) {
        this.roomImageUrl = roomImageUrl;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalDateTime getActualCheckInAt() {
        return actualCheckInAt;
    }

    public void setActualCheckInAt(LocalDateTime actualCheckInAt) {
        this.actualCheckInAt = actualCheckInAt;
    }

    public LocalDateTime getActualCheckOutAt() {
        return actualCheckOutAt;
    }

    public void setActualCheckOutAt(LocalDateTime actualCheckOutAt) {
        this.actualCheckOutAt = actualCheckOutAt;
    }

    public long getNights() {
        return nights;
    }

    public void setNights(long nights) {
        this.nights = nights;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public boolean isHasBill() {
        return hasBill;
    }

    public void setHasBill(boolean hasBill) {
        this.hasBill = hasBill;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
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
