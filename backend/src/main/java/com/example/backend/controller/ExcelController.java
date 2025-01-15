package com.example.backend.controller;

import com.example.backend.dto.DailyStatisticDTO;
import com.example.backend.dto.PreviewDataDTO;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/preview")
    public ApiResponse<List<PreviewDataDTO<List<DailyStatisticDTO>>>> getDataPreviewByFilter(
            @RequestParam(value = "startDate", defaultValue = "2024-12-01") String startDate,
            @RequestParam(value = "endDate", defaultValue = "2024-12-31") String endDate
    ) {
        try {
            List<PreviewDataDTO<List<DailyStatisticDTO>>> response = excelService.previewByFilter(
                    startDate, endDate);

            return ApiResponse.<List<PreviewDataDTO<List<DailyStatisticDTO>>>>builder()
                    .code(HttpStatus.OK.value())
                    .message("success")
                    .data(response)
                    .build();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), e.getMessage());
        }
    }

}
