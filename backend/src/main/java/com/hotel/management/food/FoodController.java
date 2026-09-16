package com.hotel.management.food;

import com.hotel.management.food.dto.CreateFoodOrderRequest;
import com.hotel.management.food.dto.FoodItemDto;
import com.hotel.management.food.dto.FoodOrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@Tag(name = "Food & Dining", description = "Endpoints for browsing menu and placing room food orders")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/items")
    @Operation(summary = "Browse food menu", description = "Get available food items, optionally filtered by category")
    public ResponseEntity<List<FoodItemDto>> getFoodItems(@RequestParam(required = false) FoodCategory category) {
        return ResponseEntity.ok(foodService.getAllFoodItems(category));
    }

    @GetMapping("/items/{id}")
    @Operation(summary = "Get food item details", description = "Returns single food item information by ID")
    public ResponseEntity<FoodItemDto> getFoodItemById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getFoodItemById(id));
    }

    @PostMapping("/orders")
    @Operation(summary = "Place a food order", description = "Customer places an order, optionally linked to their active room booking")
    public ResponseEntity<FoodOrderDto> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CreateFoodOrderRequest request) {
        FoodOrderDto order = foodService.createOrder(userDetails.getUsername(), request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/orders/my-orders")
    @Operation(summary = "Get customer's food orders", description = "Returns all food orders placed by authenticated customer")
    public ResponseEntity<List<FoodOrderDto>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(foodService.getUserOrders(userDetails.getUsername()));
    }

    @GetMapping("/orders/{id}")
    @Operation(summary = "Get food order details", description = "Returns details of a specific food order")
    public ResponseEntity<FoodOrderDto> getOrderById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(foodService.getOrderById(userDetails.getUsername(), id, false));
    }

    @PostMapping("/orders/{id}/cancel")
    @Operation(summary = "Cancel food order", description = "Cancel order if it is still in ORDER_CONFIRMED status")
    public ResponseEntity<FoodOrderDto> cancelOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(foodService.cancelOrder(userDetails.getUsername(), id));
    }
}
