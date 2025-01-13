package com.example.backend.controller;

import com.example.backend.dto.request.AuthenticationRequest;
import com.example.backend.dto.request.LogoutRequest;
import com.example.backend.dto.request.RefreshTokenRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.AuthenticationResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/authentication")
    public ApiResponse<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ApiResponse.<AuthenticationResponse>builder()
                    .message("success")
                    .data(response)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(ErrorCode.BAD_REQUEST.getStatusCode().value())
                    .message(e.getMessage())
                    .build();

        }
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@Valid @RequestBody LogoutRequest request) {
        try {
            String response = authenticationService.logout(request);
            return ApiResponse.<String>builder()
                    .message(response)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .code(ErrorCode.BAD_REQUEST.getStatusCode().value())
                    .message(e.getMessage())
                    .build();
        }
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try{
            AuthenticationResponse newAccessToken = authenticationService.refreshAccessToken(request);
            return ApiResponse.<AuthenticationResponse>builder()
                    .message("Token refreshed successfully")
                    .data(newAccessToken)
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(ErrorCode.UNAUTHORIZED.getStatusCode().value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }


    }
}
