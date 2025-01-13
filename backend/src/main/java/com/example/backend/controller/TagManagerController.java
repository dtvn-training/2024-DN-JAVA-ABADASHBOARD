package com.example.backend.controller;

import java.util.List;

import com.example.backend.dto.response.PageResponse;
import com.example.backend.service.GoogleTagManagerService.TagService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.request.ListTagRequestGTM;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.TagResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/google-tag-manager1")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TagManagerController {

    TagService tagService;

    @GetMapping("/list-tag-gtm")
    public ResponseEntity<ApiResponse<List<com.google.api.services.tagmanager.model.Tag>>> listTag(@RequestBody ListTagRequestGTM request) {
        try {
            List<com.google.api.services.tagmanager.model.Tag> tags = tagService.listTagGTM(request); // Fetch from Google Tag Manager
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", tags));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/list-tag")
    public ApiResponse<PageResponse<TagResponse>> getListTag(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "6") int size
    ) {
        try {
            PageResponse<TagResponse> tagResponses = tagService.listTags(page, size);
            return ApiResponse.<PageResponse<TagResponse>>builder()
                    .message("success")
                    .data(tagResponses)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<PageResponse<TagResponse>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }

    @GetMapping("/tags/{id}")
    public ApiResponse<TagResponse> getTagById(@PathVariable("id") Long id) {
        try {
            TagResponse tagResponse = tagService.getTagById(id);
            return ApiResponse.<TagResponse>builder()
                    .message("success")
                    .data(tagResponse)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<TagResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}