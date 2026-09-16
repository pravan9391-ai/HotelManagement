package com.hotel.management.room.dto;

import java.time.LocalDate;

public class BookedDateRangeDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;

    public BookedDateRangeDto() {
    }

    public BookedDateRangeDto(LocalDate checkInDate, LocalDate checkOutDate, String status) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
