package com.example.backend.controller;

import java.util.List;

import com.example.backend.service.GoogleTagManagerService.Impl.TagServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.TagResponse;
import com.google.api.services.tagmanager.model.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TagManagerController {

    private final TagServiceImpl tagService;

    @GetMapping("/list-tag-gtm")
    public ResponseEntity<ApiResponse<List<Tag>>> listTag(@RequestBody ListTagRequestGTM request) {
        try {
            List<Tag> tags = tagService.listTagGTM(request);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", tags));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }

    @GetMapping("/list-tag")
    public ApiResponse<List<TagResponse>> getListTag() {
        try {
            List<TagResponse> tagResponses = tagService.listTags();
            return ApiResponse.<List<TagResponse>>builder()
                    .data(tagResponses)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<TagResponse>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }
    }
}
