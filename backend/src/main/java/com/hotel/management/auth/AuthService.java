package com.hotel.management.auth;

import com.hotel.management.auth.dto.AuthResponse;
import com.hotel.management.auth.dto.LoginRequest;
import com.hotel.management.auth.dto.RefreshTokenRequest;
import com.hotel.management.auth.dto.RegisterRequest;
import com.hotel.management.exception.BadRequestException;
import com.hotel.management.exception.UnauthorizedException;
import com.hotel.management.security.JwtTokenProvider;
import com.hotel.management.user.Role;
import com.hotel.management.user.User;
import com.hotel.management.user.UserRepository;
import com.hotel.management.user.dto.UserSummaryDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with this email already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber(),
                Role.ROLE_CUSTOMER
        );

        User savedUser = userRepository.save(user);

        String accessToken = tokenProvider.generateAccessTokenFromEmail(savedUser.getEmail(), savedUser.getRole().name());
        String refreshToken = tokenProvider.generateRefreshToken(savedUser.getEmail());

        return new AuthResponse(accessToken, refreshToken, UserSummaryDto.fromEntity(savedUser));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(user.getEmail());

        return new AuthResponse(accessToken, refreshToken, UserSummaryDto.fromEntity(user));
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        if (!tokenProvider.validateToken(token)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String email = tokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User associated with refresh token not found"));

        String newAccessToken = tokenProvider.generateAccessTokenFromEmail(user.getEmail(), user.getRole().name());
        String newRefreshToken = tokenProvider.generateRefreshToken(user.getEmail());

        return new AuthResponse(newAccessToken, newRefreshToken, UserSummaryDto.fromEntity(user));
    }
}
