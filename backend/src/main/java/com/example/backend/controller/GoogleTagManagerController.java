package com.example.backend.controller;

import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.service.GoogleTagManagerService.TagService;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Trigger;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleTagManagerController {
    private final TagService tagService;

    @PostMapping("/create-tag")
    public ApiResponse<TagResponse> createTag(@Valid @RequestBody CreateTagRequest request) {
//        return null;
        return tagService.CreateTag(request);
    }
}
