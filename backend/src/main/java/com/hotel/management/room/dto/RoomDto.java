package com.hotel.management.room.dto;

import com.hotel.management.room.Room;
import com.hotel.management.room.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomDto {
    private Long id;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private String description;
    private Boolean isAvailable;
    private String imageUrl;
    private LocalDateTime createdAt;
    private String availabilityStatus; // "available", "processing", "booked", "reserved"
    private List<BookedDateRangeDto> bookedDates = new ArrayList<>();
    private LocalDate nextAvailableDate;

    public RoomDto() {
    }

    public static RoomDto fromEntity(Room room) {
        if (room == null) return null;
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomType(room.getRoomType());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setCapacity(room.getCapacity());
        dto.setDescription(room.getDescription());
        dto.setIsAvailable(room.getIsAvailable());
        dto.setImageUrl(room.getImageUrl());
        dto.setCreatedAt(room.getCreatedAt());
        dto.setAvailabilityStatus(Boolean.TRUE.equals(room.getIsAvailable()) ? "available" : "unavailable");
        dto.setNextAvailableDate(LocalDate.now());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public List<BookedDateRangeDto> getBookedDates() {
        return bookedDates;
    }

    public void setBookedDates(List<BookedDateRangeDto> bookedDates) {
        this.bookedDates = bookedDates;
    }

    public LocalDate getNextAvailableDate() {
        return nextAvailableDate;
    }

    public void setNextAvailableDate(LocalDate nextAvailableDate) {
        this.nextAvailableDate = nextAvailableDate;
    }
}
