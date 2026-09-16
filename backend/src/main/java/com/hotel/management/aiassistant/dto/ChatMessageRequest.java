package com.hotel.management.aiassistant.dto;

import jakarta.validation.constraints.NotBlank;

public class ChatMessageRequest {

    @NotBlank(message = "Message cannot be empty")
    private String message;

    public ChatMessageRequest() {
    }

    public ChatMessageRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
