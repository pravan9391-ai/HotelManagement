package com.hotel.management.user.dto;

import com.hotel.management.billing.dto.BillResponseDto;
import com.hotel.management.booking.dto.BookingResponseDto;
import com.hotel.management.user.Role;
import com.hotel.management.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetailDto {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private LocalDateTime createdAt;
    private String currentOccupiedRoom;
    private String currentBookingStatus;
    private String stayDates;
    private List<BookingResponseDto> bookings = new ArrayList<>();
    private List<BillResponseDto> bills = new ArrayList<>();

    public CustomerDetailDto() {
    }

    public static CustomerDetailDto fromUser(User user) {
        if (user == null) return null;
        CustomerDetailDto dto = new CustomerDetailDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrentOccupiedRoom() {
        return currentOccupiedRoom;
    }

    public void setCurrentOccupiedRoom(String currentOccupiedRoom) {
        this.currentOccupiedRoom = currentOccupiedRoom;
    }

    public String getCurrentBookingStatus() {
        return currentBookingStatus;
    }

    public void setCurrentBookingStatus(String currentBookingStatus) {
        this.currentBookingStatus = currentBookingStatus;
    }

    public String getStayDates() {
        return stayDates;
    }

    public void setStayDates(String stayDates) {
        this.stayDates = stayDates;
    }

    public List<BookingResponseDto> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingResponseDto> bookings) {
        this.bookings = bookings;
    }

    public List<BillResponseDto> getBills() {
        return bills;
    }

    public void setBills(List<BillResponseDto> bills) {
        this.bills = bills;
    }
}
