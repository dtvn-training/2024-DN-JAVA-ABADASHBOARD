package com.example.backend.service.GoogleTagManagerService;

import com.example.backend.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface GoogleTagManagerService {
    ApiResponse<?> CreateTag(String tagName, String tagType);
}
