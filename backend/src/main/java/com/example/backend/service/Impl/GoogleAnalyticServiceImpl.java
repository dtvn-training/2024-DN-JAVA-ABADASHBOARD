package com.example.backend.service.Impl;

import com.example.backend.constant.AppConstant;
import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.dto.response.EventChartResponse;
import com.example.backend.dto.response.EventTableResponse;
import com.example.backend.dto.response.NumberOfEventResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.entity.*;
import com.example.backend.enums.*;
import com.example.backend.enums.MetricType;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.EventMapper;
import com.example.backend.repository.*;
import com.example.backend.service.GoogleAnalyticService;
import com.google.analytics.data.v1beta.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleAnalyticServiceImpl implements GoogleAnalyticService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public PageResponse<EventDto> getEvents(int pageNum, int pageSize, String eventLabel) {
        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            Page<Event> events = eventRepository.findDistinctEventsByEventLabel(eventLabel, pageable);
            return getEventDtoPageResponse(events);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

    @NotNull
    private PageResponse<EventDto> getEventDtoPageResponse(Page<Event> events) {
        List<EventDto> data = events.stream().map(eventMapper::mapToDto).toList();
        PageResponse<EventDto> response = new PageResponse<>();
        response.setData(data);
        response.setCurrentPage(events.getNumber());
        response.setPageSize(events.getSize());
        response.setTotalPages(events.getTotalPages());
        response.setTotalElements(events.getTotalElements());
        return response;
    }

    @NotNull
    private PageResponse<EventTableResponse> getEventTablePageResponse(Page<Event> events, List<EventTableResponse> data) {
        PageResponse<EventTableResponse> response = new PageResponse<>();
        response.setData(data);
        response.setCurrentPage(events.getNumber());
        response.setPageSize(events.getSize());
        response.setTotalPages(events.getTotalPages());
        response.setTotalElements(events.getTotalElements());
        return response;
    }

    @Override
    public Map<String, Object> getEventsByFilter(String startDate,
                                                 String endDate,
                                                 String eventLabel,
                                                 int pageNum,
                                                 int pageSize,
                                                 String mediumName,
                                                 String campaignName) {
        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(AppConstant.TIME_FORMAT_YYYY_MM_DD);
            LocalDate startDateRaw = LocalDate.parse(startDate, dateFormatter);
            LocalDate endDateRaw = LocalDate.parse(endDate, dateFormatter);
            LocalDateTime startDateNew = startDateRaw.atStartOfDay();
            LocalDateTime endDateNew = endDateRaw.atTime(LocalTime.MAX);
            // execute query get data for event table, number of event and event for chart.
            PagedResourcesAssembler<EventTableResponse> pagedResourcesAssembler = new PagedResourcesAssembler<>(null, null);
            Page<EventTableResponse> eventsTableResponse = eventRepository.findEventsByEventLabelAndTimestampBetween(eventLabel, startDateNew, endDateNew, mediumName, campaignName, pageable);
            List<NumberOfEventResponse> numberOfEventResponses = eventRepository.numberOfEventsByEventLabel(startDateNew, endDateNew, mediumName, campaignName);
            List<EventChartResponse> getEventChartResponse = eventRepository.getEventsForChart(startDateNew, endDateNew, mediumName, campaignName);
            Map<String, Object> response = new HashMap<>();
            response.put("eventTable", pagedResourcesAssembler.toModel(eventsTableResponse));
            response.put("numberOfEvent", numberOfEventResponses);
            response.put("chartEvent", getEventChartResponse);
            return response;
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

    @Override
    public PageResponse<EventTableResponse> getEventByMedium(String mediumName, int pageNum, int pageSize, String eventLabel, String startDate, String endDate) {
        try {
            Pageable pageable = PageRequest.of(pageNum, pageSize);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(AppConstant.TIME_FORMAT_YYYY_MM_DD);
            LocalDate startDateRaw = LocalDate.parse(startDate, dateFormatter);
            LocalDate endDateRaw = LocalDate.parse(endDate, dateFormatter);
            LocalDateTime startDateNew = startDateRaw.atStartOfDay();
            LocalDateTime endDateNew = endDateRaw.atTime(LocalTime.MAX);
            // get all event with medium id
            Page<Event> getEvents = eventRepository.getEventsByMediumId(eventLabel, startDateNew, endDateNew, mediumName, pageable);
            List<EventTableResponse> responses = getEvents.stream().collect(Collectors.groupingBy(
                            Event::getEventName,
                            Collectors.summingLong(event -> Long.parseLong(event.getEventValue()))
                    ))
                    .entrySet()
                    .stream()
                    .map(entry -> new EventTableResponse(entry.getKey(), entry.getValue()))
                    .toList();
            return getEventTablePageResponse(getEvents, responses);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }


}
