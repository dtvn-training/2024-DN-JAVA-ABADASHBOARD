package com.example.backend.controller;

import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.service.GoogleTagManagerService.GoogleTagManagerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleTagManagerController {
    GoogleTagManagerService googleTagManagerService;

    @PostMapping("/create-tag")
    public ApiResponse<?> createTag(@Valid @RequestBody CreateTagRequest request) {
        return googleTagManagerService.CreateTag(request.getTagName(), request.getTagType());
    }
}
