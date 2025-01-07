package com.example.backend.controller;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save-report")
    public ApiResponse<List<Map<String, String>>> saveReportOfGoogleAnalyticIntoDb(@Valid @RequestBody ReportRequest request) {
        try{
            List<Map<String, String>> response= googleAnalyticService.saveEventIntoDatabase(request);
            return createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(),e.getMessage());
        }
    }

    @GetMapping("/get-all-events")
    public ApiResponse<PageResponse<EventDto>> getEventsOfGoogleAnalytic(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                 @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                                                                 @RequestParam("eventLabel") String eventLabel) {
        try{
            PageResponse<EventDto> response= googleAnalyticService.getEvents(pageNum, pageSize, eventLabel);
            return createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }

    @GetMapping("/get-all-events-by-time")
    public ApiResponse<Map<String, Object>> getEventsOfGoogleAnalyticByStartAndEndDate(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                         @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                                                                         @RequestParam(value = "eventLabel", defaultValue = "eventName") String eventLabel,
                                                                         @RequestParam(value = "startDate", defaultValue = "2024-12-01") String startDate,
                                                                         @RequestParam(value = "endDate", defaultValue = "2024-12-31") String endDate
    ) {
        try{
            Map<String, Object> response= googleAnalyticService.getEventsByStartDateAndEndDate(startDate,endDate,eventLabel,pageNum,pageSize);
            return createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }
}