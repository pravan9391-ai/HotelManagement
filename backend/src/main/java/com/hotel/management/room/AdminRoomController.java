package com.hotel.management.room;

import com.hotel.management.room.dto.CreateRoomRequest;
import com.hotel.management.room.dto.RoomDto;
import com.hotel.management.room.dto.UpdateRoomRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/rooms")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Rooms", description = "Admin-only endpoints for managing room inventory")
public class AdminRoomController {

    private final RoomService roomService;

    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    @Operation(summary = "Create a new room", description = "Adds a new room to inventory (Admin only)")
    public ResponseEntity<RoomDto> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        RoomDto created = roomService.createRoom(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room details", description = "Updates room price, type, capacity, description, or availability (Admin only)")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable Long id, @Valid @RequestBody UpdateRoomRequest request) {
        RoomDto updated = roomService.updateRoom(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a room", description = "Deletes a room by ID (Admin only)")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}
