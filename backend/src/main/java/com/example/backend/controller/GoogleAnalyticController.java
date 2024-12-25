package com.example.backend.controller;

import com.example.backend.dto.response.ApiResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google-analytic")
public class GoogleAnalyticController {
    private final GoogleAnalyticService googleAnalyticService;

    private <T> ApiResponse<T> createResponse(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    @GetMapping("/run-report")
    public ApiResponse<List<Map<String, String>>> getReportOfGoogleAnalytic() {
        try{
            List<Map<String, String>> response= googleAnalyticService.reportResponse();
            return createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(),e.getMessage());
        }
    }

    @GetMapping("/run-report/param")
    public ApiResponse<List<Map<String, String>>> getReportOfGoogleAnalyticByParams(@RequestParam("dimension") String dimension, @RequestParam("metric") String metric) {
        try{
            List<Map<String, String>> response= googleAnalyticService.getReportByParam(dimension, metric);
            return createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(),e.getMessage());
        }
    }
}
