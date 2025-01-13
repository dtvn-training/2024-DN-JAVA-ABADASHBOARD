package com.example.backend.service.impl;

import com.example.backend.dto.request.AuthenticationRequest;
import com.example.backend.dto.request.LogoutRequest;
import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.response.AuthenticationResponse;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.AuthenticationService;
import com.example.backend.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!request.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Override
    public AuthenticationResponse refreshAccessToken(RefreshTokenRequest request) {
        return AuthenticationResponse.builder()
                .token(jwtUtil.refreshAccessToken(request.getRefreshToken()))
                .authenticated(true)
                .build();
    }

    @Override
    public String logout(LogoutRequest request) {
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        if (accessToken == null || accessToken.isEmpty()) {
            return "Access token is missing or invalid.";
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            return "Refresh token is missing or invalid.";
        }

        try {
            jwtUtil.adjustTokenExpiry(accessToken, 0);
            jwtUtil.adjustTokenExpiry(refreshToken, 0);
        } catch (JwtException e) {
            return "Failed to invalidate tokens: " + e.getMessage();
        }

        return "Logout successful. Tokens have been invalidated.";
    }
}
