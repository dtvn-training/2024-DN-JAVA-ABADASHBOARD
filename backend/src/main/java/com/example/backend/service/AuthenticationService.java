package com.example.backend.service;

import com.example.backend.dto.request.AuthenticationRequest;
import com.example.backend.dto.request.LogoutRequest;
import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshAccessToken(RefreshTokenRequest refreshToken);
    String logout (LogoutRequest request);
}
