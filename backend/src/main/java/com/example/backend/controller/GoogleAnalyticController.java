package com.example.backend.controller;

import com.example.backend.comon.CreateApiResponse;
import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.EventTableResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.GoogleAnalyticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/google-analytic")
public class GoogleAnalyticController {
    private final GoogleAnalyticService googleAnalyticService;

    @GetMapping("/get-all-events")
    public ApiResponse<PageResponse<EventDto>> getEventsOfGoogleAnalytic(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                         @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                                                                         @RequestParam("eventLabel") String eventLabel) {
        try {
            PageResponse<EventDto> response = googleAnalyticService.getEvents(pageNum, pageSize, eventLabel);
            return CreateApiResponse.createResponse(response);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), e.getMessage());
        }
    }

    @GetMapping("/get-all-events-by-filter")
    public ApiResponse<Map<String, Object>> getEventsOfGoogleAnalyticByFilter(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                              @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                                                                              @RequestParam(value = "eventLabel", defaultValue = "eventName") String eventLabel,
                                                                              @RequestParam(value = "startDate", defaultValue = "2024-12-01") String startDate,
                                                                              @RequestParam(value = "endDate", defaultValue = "2024-12-31") String endDate,
                                                                              @RequestParam(value = "mediumName", defaultValue = "") String mediumName,
                                                                              @RequestParam(value = "campaignName", defaultValue = "") String campaignName) {
        try {
            Map<String, Object> response = googleAnalyticService.getEventsByFilter(startDate, endDate, eventLabel, pageNum, pageSize, mediumName, campaignName);
            return CreateApiResponse.createResponse(response);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), e.getMessage());
        }
    }

    @GetMapping("/get-events-by-medium")
    public ApiResponse<PageResponse<EventTableResponse>> getEventsOfGoogleAnalyticByMedium(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                                           @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                                                                                           @RequestParam(value = "eventLabel", defaultValue = "eventName") String eventLabel,
                                                                                           @RequestParam(value = "startDate", defaultValue = "2024-12-01") String startDate,
                                                                                           @RequestParam(value = "endDate", defaultValue = "2024-12-31") String endDate,
                                                                                           @RequestParam(value = "mediumName", defaultValue = "organic") String mediumName) {
        try {
            PageResponse<EventTableResponse> response = googleAnalyticService.getEventByMedium(mediumName, pageNum, pageSize, eventLabel, startDate, endDate);
            return CreateApiResponse.createResponse(response);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), e.getMessage());
        }
    }
}
