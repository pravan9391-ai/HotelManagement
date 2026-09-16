package com.hotel.management.billing.dto;

import com.hotel.management.billing.Bill;
import com.hotel.management.food.dto.FoodOrderDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillResponseDto {

    private Long id;
    private Long bookingId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String roomNumber;
    private String roomType;
    private BigDecimal roomPricePerNight;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime actualCheckInAt;
    private LocalDateTime actualCheckOutAt;
    private long nights;
    private BigDecimal roomCharges;
    private List<FoodOrderDto> foodOrders = new ArrayList<>();
    private BigDecimal foodCharges;
    private BigDecimal subtotal;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime generatedAt;
    private String notice = "Official Hotel Invoice for Guest Records Only. Generated upon departure checkout. No online payment processing is required.";

    public BillResponseDto() {
    }

    public static BillResponseDto fromEntity(Bill bill, List<FoodOrderDto> orders) {
        if (bill == null) return null;
        BillResponseDto dto = new BillResponseDto();
        dto.setId(bill.getId());
        if (bill.getBooking() != null) {
            dto.setBookingId(bill.getBooking().getId());
            if (bill.getBooking().getUser() != null) {
                dto.setCustomerName(bill.getBooking().getUser().getUsername());
                dto.setCustomerEmail(bill.getBooking().getUser().getEmail());
                dto.setCustomerPhone(bill.getBooking().getUser().getPhoneNumber());
            }
            if (bill.getBooking().getRoom() != null) {
                dto.setRoomNumber(bill.getBooking().getRoom().getRoomNumber());
                dto.setRoomType(bill.getBooking().getRoom().getRoomType() != null ? bill.getBooking().getRoom().getRoomType().name() : "");
                dto.setRoomPricePerNight(bill.getBooking().getRoom().getPricePerNight());
            }
            dto.setCheckInDate(bill.getBooking().getCheckInDate());
            dto.setCheckOutDate(bill.getBooking().getCheckOutDate());
            dto.setActualCheckInAt(bill.getBooking().getActualCheckInAt());
            dto.setActualCheckOutAt(bill.getBooking().getActualCheckOutAt());
            dto.setNights(bill.getBooking().getNights());
        }
        dto.setRoomCharges(bill.getRoomCharges());
        dto.setFoodOrders(orders != null ? orders : new ArrayList<>());
        dto.setFoodCharges(bill.getFoodCharges());
        dto.setSubtotal(bill.getRoomCharges().add(bill.getFoodCharges()));
        dto.setTaxRate(bill.getTaxRate());
        dto.setTaxAmount(bill.getTaxAmount());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setStatus(bill.getStatus() != null ? bill.getStatus().name() : "GENERATED");
        dto.setGeneratedAt(bill.getGeneratedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getRoomPricePerNight() {
        return roomPricePerNight;
    }

    public void setRoomPricePerNight(BigDecimal roomPricePerNight) {
        this.roomPricePerNight = roomPricePerNight;
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

    public BigDecimal getRoomCharges() {
        return roomCharges;
    }

    public void setRoomCharges(BigDecimal roomCharges) {
        this.roomCharges = roomCharges;
    }

    public List<FoodOrderDto> getFoodOrders() {
        return foodOrders;
    }

    public void setFoodOrders(List<FoodOrderDto> foodOrders) {
        this.foodOrders = foodOrders;
    }

    public BigDecimal getFoodCharges() {
        return foodCharges;
    }

    public void setFoodCharges(BigDecimal foodCharges) {
        this.foodCharges = foodCharges;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
