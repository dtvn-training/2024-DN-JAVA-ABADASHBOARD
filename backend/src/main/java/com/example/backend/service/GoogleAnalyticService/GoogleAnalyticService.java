package com.example.backend.service.GoogleAnalyticService;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.dto.response.EventTableResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.entity.Event;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GoogleAnalyticService {
    List<Map<String, String>> reportResponse();
    List<Map<String, String>> saveEventIntoDatabase(ReportRequest request);
    PageResponse<EventDto> getEvents(int pageNum, int pageSize, String eventLabel);
    Map<String, Object> getEventsByStartDateAndEndDate(String startDate, String endDate, String eventLabel,int pageNum, int pageSize);
    PageResponse<EventTableResponse> getEventByMedium(String mediumName, int pageNum, int pageSize, String eventLabel, String startDate, String endDate);
    List<Event> getReportFromGA4MapToEvent(String dimension, String metric, String campaignName);
}
