package com.hotel.management.booking.dto;

import jakarta.validation.constraints.NotNull;

public class ReassignRoomRequest {

    @NotNull(message = "New room ID is required")
    private Long newRoomId;

    public ReassignRoomRequest() {
    }

    public ReassignRoomRequest(Long newRoomId) {
        this.newRoomId = newRoomId;
    }

    public Long getNewRoomId() {
        return newRoomId;
    }

    public void setNewRoomId(Long newRoomId) {
        this.newRoomId = newRoomId;
    }
}
