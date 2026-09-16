package com.hotel.management.food.dto;

import com.hotel.management.food.FoodOrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateFoodOrderStatusRequest {

    @NotNull(message = "Status is required")
    private FoodOrderStatus status;

    public UpdateFoodOrderStatusRequest() {
    }

    public UpdateFoodOrderStatusRequest(FoodOrderStatus status) {
        this.status = status;
    }

    public FoodOrderStatus getStatus() {
        return status;
    }

    public void setStatus(FoodOrderStatus status) {
        this.status = status;
    }
}
