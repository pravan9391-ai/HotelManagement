package com.hotel.management.user;

import com.hotel.management.user.dto.CreateAdminUserRequest;
import com.hotel.management.user.dto.CustomerDetailDto;
import com.hotel.management.user.dto.UserSummaryDto;
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
@Tag(name = "Admin Users & Customers", description = "Admin-only endpoints for managing users and viewing customer activity")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @Operation(summary = "Create user/admin", description = "Allows an administrator to create new staff/admin or customer accounts (Admin only)")
    public ResponseEntity<UserSummaryDto> createUser(@Valid @RequestBody CreateAdminUserRequest request) {
        UserSummaryDto created = userService.createAdminUser(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/customers")
    @Operation(summary = "List all customers", description = "Returns all hotel customers with their occupied room, stay dates, and billing status (Admin only)")
    public ResponseEntity<List<CustomerDetailDto>> getAllCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    @GetMapping("/customers/{id}")
    @Operation(summary = "Get customer details", description = "Returns full activity profile for a customer including all reservations and generated bills (Admin only)")
    public ResponseEntity<CustomerDetailDto> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getCustomerById(id));
    }
}
