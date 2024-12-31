package com.example.backend.service.GoogleAnalyticService;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GoogleAnalyticService {
    List<Map<String, String>> reportResponse();
    List<Map<String, String>> saveEventIntoDatabase(ReportRequest request);
    List<EventDto> getEvents(int pageNum,int pageSize, String eventLabel);

}
