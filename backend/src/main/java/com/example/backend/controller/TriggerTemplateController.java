package com.example.backend.controller;

import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TriggerTemplateResponse;
import com.example.backend.service.TriggerTemplateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/google-trigger-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TriggerTemplateController {

    private final TriggerTemplateService triggerTemplateService;

    @GetMapping("/list-trigger")
    public ApiResponse<PageResponse<TriggerTemplateResponse>> listTriggerTemplates(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            PageResponse<TriggerTemplateResponse> triggerTemplateResponse = triggerTemplateService.listTriggerTemplates(page,size);
            return ApiResponse.<PageResponse<TriggerTemplateResponse>>builder()
                    .message("success")
                    .data(triggerTemplateResponse)
                    .build();
        } catch (Exception e) {
            return ApiResponse.<PageResponse<TriggerTemplateResponse>>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .data(null)
                    .build();
        }

    }
}