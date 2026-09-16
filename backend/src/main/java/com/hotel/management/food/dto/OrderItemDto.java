package com.hotel.management.food.dto;

import com.hotel.management.food.OrderItem;

import java.math.BigDecimal;

public class OrderItemDto {
    private Long id;
    private Long foodItemId;
    private String foodItemName;
    private String foodItemCategory;
    private String foodItemImageUrl;
    private Integer quantity;
    private BigDecimal priceAtOrder;
    private BigDecimal subtotal;
    private String notes;

    public OrderItemDto() {
    }

    public static OrderItemDto fromEntity(OrderItem item) {
        if (item == null) return null;
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        if (item.getFoodItem() != null) {
            dto.setFoodItemId(item.getFoodItem().getId());
            dto.setFoodItemName(item.getFoodItem().getName());
            dto.setFoodItemCategory(item.getFoodItem().getCategory() != null ? item.getFoodItem().getCategory().name() : null);
            dto.setFoodItemImageUrl(item.getFoodItem().getImageUrl());
        }
        dto.setQuantity(item.getQuantity());
        dto.setPriceAtOrder(item.getPriceAtOrder());
        dto.setSubtotal(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        dto.setNotes(item.getNotes());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public String getFoodItemName() {
        return foodItemName;
    }

    public void setFoodItemName(String foodItemName) {
        this.foodItemName = foodItemName;
    }

    public String getFoodItemCategory() {
        return foodItemCategory;
    }

    public void setFoodItemCategory(String foodItemCategory) {
        this.foodItemCategory = foodItemCategory;
    }

    public String getFoodItemImageUrl() {
        return foodItemImageUrl;
    }

    public void setFoodItemImageUrl(String foodItemImageUrl) {
        this.foodItemImageUrl = foodItemImageUrl;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(BigDecimal priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
