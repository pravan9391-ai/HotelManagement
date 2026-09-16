package com.hotel.management.aiassistant;

import com.hotel.management.aiassistant.dto.ChatMessageRequest;
import com.hotel.management.aiassistant.dto.ChatResponse;
import com.hotel.management.user.User;
import com.hotel.management.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Concierge", description = "Endpoints for interacting with the Royal Stay Google Gemini AI assistant")
public class AiChatController {

    private final GeminiService geminiService;
    private final UserRepository userRepository;

    public AiChatController(GeminiService geminiService, UserRepository userRepository) {
        this.geminiService = geminiService;
        this.userRepository = userRepository;
    }

    @PostMapping("/chat")
    @Operation(summary = "Ask AI Concierge", description = "Sends query to Royal Stay AI assistant with live room and menu context")
    public ResponseEntity<ChatResponse> chat(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ChatMessageRequest request) {

        User user = null;
        if (userDetails != null) {
            user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        }

        String reply = geminiService.generateResponse(request.getMessage(), user);
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
