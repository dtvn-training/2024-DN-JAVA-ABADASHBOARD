package com.example.backend.service;

import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.request.CreateTriggerRequest;
import com.example.backend.dto.request.ListTagRequestGTM;
import com.example.backend.dto.request.UpdateTagRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TagResponse;
import com.google.api.services.tagmanager.model.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface TagService {
    ApiResponse<TagResponse> CreateTag(CreateTagRequest request);
    ApiResponse<?> updatePushTagOnGTM(UpdateTagRequest request);
    List<Tag> listTagGTM(ListTagRequestGTM requestGTM) throws IOException;
    PageResponse<TagResponse> listTags(int page, int size);
    TagResponse getTagById(Long id);
    ResponseEntity<?> createTrigger(CreateTriggerRequest request) throws IOException;
}