package com.hotel.management.user.dto;

import com.hotel.management.user.Role;
import com.hotel.management.user.User;

import java.time.LocalDateTime;

public class UserSummaryDto {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Role role;
    private LocalDateTime createdAt;

    public UserSummaryDto() {
    }

    public UserSummaryDto(Long id, String username, String email, String phoneNumber, Role role, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.createdAt = createdAt;
    }

    public static UserSummaryDto fromEntity(User user) {
        if (user == null) return null;
        return new UserSummaryDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
