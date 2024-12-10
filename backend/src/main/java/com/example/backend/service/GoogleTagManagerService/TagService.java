package com.example.backend.service.GoogleTagManagerService;

import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    ApiResponse<?> CreateTag(CreateTagRequest request);
}
