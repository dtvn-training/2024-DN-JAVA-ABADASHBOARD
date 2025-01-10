package com.example.backend.service;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.dto.response.EventTableResponse;
import com.example.backend.dto.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GoogleAnalyticService {
    PageResponse<EventDto> getEvents(int pageNum, int pageSize, String eventLabel);
    Map<String, Object> getEventsByFilter(String startDate, String endDate,
                                                       String eventLabel,
                                                       int pageNum, int pageSize,
                                                       String mediumName,
                                                       String campaignName);
    PageResponse<EventTableResponse> getEventByMedium(String mediumName, int pageNum, int pageSize, String eventLabel, String startDate, String endDate);
}
