package com.hotel.management.billing;

import com.hotel.management.booking.Booking;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false, unique = true)
    private Booking booking;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "room_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal roomCharges = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "food_charges", nullable = false, precision = 10, scale = 2)
    private BigDecimal foodCharges = BigDecimal.ZERO;

    @NotNull
    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate = new BigDecimal("12.00");

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "tax_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BillStatus status = BillStatus.GENERATED;

    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    public Bill() {
    }

    public Bill(Booking booking, BigDecimal roomCharges, BigDecimal foodCharges, BigDecimal taxRate, BigDecimal taxAmount, BigDecimal totalAmount) {
        this.booking = booking;
        this.roomCharges = roomCharges;
        this.foodCharges = foodCharges;
        this.taxRate = taxRate != null ? taxRate : new BigDecimal("12.00");
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.status = BillStatus.GENERATED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BigDecimal getRoomCharges() {
        return roomCharges;
    }

    public void setRoomCharges(BigDecimal roomCharges) {
        this.roomCharges = roomCharges;
    }

    public BigDecimal getFoodCharges() {
        return foodCharges;
    }

    public void setFoodCharges(BigDecimal foodCharges) {
        this.foodCharges = foodCharges;
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

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
