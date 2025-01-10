package com.example.backend.controller;

import com.example.backend.dto.response.ApiResponse;
import com.example.backend.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;

    @GetMapping("/download")
    public ApiResponse<byte[]> downloadExcel() {
        try {

            byte[] excelContent = excelService.generateExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=data.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            return ApiResponse.<byte[]>builder()
                    .code(HttpStatus.OK.value())
                    .message("File generated successfully")
                    .data(excelContent)
                    .build();

        } catch (IOException e) {
            return ApiResponse.<byte[]>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Failed to generate Excel file: " + e.getMessage())
                    .build();
        }
    }
}
