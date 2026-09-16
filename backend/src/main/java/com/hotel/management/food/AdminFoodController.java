package com.hotel.management.food;

import com.hotel.management.food.dto.CreateFoodItemRequest;
import com.hotel.management.food.dto.FoodItemDto;
import com.hotel.management.food.dto.FoodOrderDto;
import com.hotel.management.food.dto.UpdateFoodOrderStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Food & Dining", description = "Admin-only endpoints for managing food menu and kitchen order statuses")
public class AdminFoodController {

    private final FoodService foodService;

    public AdminFoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/food-items")
    @Operation(summary = "Add food menu item", description = "Add a new dish to the hotel dining menu (Admin only)")
    public ResponseEntity<FoodItemDto> createFoodItem(@Valid @RequestBody CreateFoodItemRequest request) {
        FoodItemDto created = foodService.createFoodItem(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/food-items/{id}")
    @Operation(summary = "Update food menu item", description = "Update price, description, or availability of a dish (Admin only)")
    public ResponseEntity<FoodItemDto> updateFoodItem(@PathVariable Long id, @Valid @RequestBody CreateFoodItemRequest request) {
        FoodItemDto updated = foodService.updateFoodItem(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/food-items/{id}")
    @Operation(summary = "Delete food menu item", description = "Remove a dish from the menu (Admin only)")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        foodService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/food-orders")
    @Operation(summary = "View all food orders", description = "Get all guest food orders across the hotel (Admin only)")
    public ResponseEntity<List<FoodOrderDto>> getAllFoodOrders() {
        return ResponseEntity.ok(foodService.getAllOrders());
    }

    @PutMapping("/food-orders/{id}/status")
    @Operation(summary = "Update food order status", description = "Kitchen updates order status: PREPARING, READY, DELIVERED, CANCELLED (Admin only)")
    public ResponseEntity<FoodOrderDto> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFoodOrderStatusRequest request) {
        return ResponseEntity.ok(foodService.updateOrderStatus(id, request.getStatus()));
    }
}
