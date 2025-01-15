package com.example.backend.controller;

import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.request.CreateTriggerRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.TagService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GoogleTagManagerController {
    private final TagService tagService;

    @PostMapping("/create-tag")
    public ApiResponse<TagResponse> createTag(@Valid @RequestBody CreateTagRequest request) {
        try {
            return tagService.CreateTag(request);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @PostMapping("/create-trigger")
    public ResponseEntity<?> createTrigger(@RequestBody CreateTriggerRequest request) {
        try {
            return tagService.createTrigger(request);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @GetMapping("/list-tag")
    public ApiResponse<PageResponse<TagResponse>> getListTag(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "6") int size) {
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
}
