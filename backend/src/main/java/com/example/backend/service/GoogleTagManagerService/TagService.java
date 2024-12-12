package com.example.backend.service.GoogleTagManagerService;

import com.example.backend.dto.TagDto;
import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.Tag;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    ApiResponse<TagResponse> CreateTag(CreateTagRequest request);
    ApiResponse<?> updatePushTagOnGTM(TagDto tagDto);
}
