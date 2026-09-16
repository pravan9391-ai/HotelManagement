package com.hotel.management.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.management.auth.dto.LoginRequest;
import com.hotel.management.auth.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAdminLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin@royalstay.com", "Admin@123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("admin@royalstay.com"))
                .andExpect(jsonPath("$.user.role").value("ROLE_ADMIN"));
    }

    @Test
    void testCustomerLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "Customer@123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.user.role").value("ROLE_CUSTOMER"));
    }

    @Test
    void testLoginFailureInvalidPassword() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin@royalstay.com", "WrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterNewCustomer() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "alice_smith",
                "alice.smith@example.com",
                "Password@123",
                "+1-555-9988"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value("alice.smith@example.com"))
                .andExpect(jsonPath("$.user.role").value("ROLE_CUSTOMER"));
    }
}
