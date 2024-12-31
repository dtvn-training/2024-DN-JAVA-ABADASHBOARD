package com.example.backend.service.GoogleAnalyticService.Impl;

import com.example.backend.dto.EventDto;
import com.example.backend.dto.request.ReportRequest;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.entity.Campaign;
import com.example.backend.entity.DimensionMaster;
import com.example.backend.entity.Event;
import com.example.backend.entity.MetricMaster;
import com.example.backend.enums.DeletedFlag;
import com.example.backend.enums.DimensionType;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.MetricType;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.EventMapper;
import com.example.backend.repository.CampaignRepository;
import com.example.backend.repository.DimensionMasterRepository;
import com.example.backend.repository.EventRepository;
import com.example.backend.repository.MetricMasterRepository;
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import com.google.analytics.data.v1beta.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GoogleAnalyticServiceImpl implements GoogleAnalyticService {
    private final BetaAnalyticsDataClient analyticsData;
    @Value("${google-analytic.properties_id}")
    private String propertyId;
    private final DimensionMasterRepository dimensionMasterRepository;
    private final MetricMasterRepository metricMasterRepository;
    private final CampaignRepository campaignRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;


    @Override
    public List<Map<String, String>> reportResponse() {
        try{
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
//                            .addDimensions(Dimension.newBuilder().setName(DimensionType.City.getName()))
                            .addDimensions(Dimension.newBuilder().setName("month"))
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.EventName.getName()))
//                            .addMetrics(Metric.newBuilder().setName(MetricType.ActiveUser.getName()))
                            .addMetrics(Metric.newBuilder().setName(MetricType.EventCount.getName()))
                            .addDateRanges(DateRange.newBuilder().setStartDate("2024-11-25").setEndDate("today"))
                            .build();
            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);

            List<Map<String, String>> result = new ArrayList<>();
            for (Row row : response.getRowsList()) {
                Map<String, String> rowData = new HashMap<>();
                rowData.put("date", row.getDimensionValues(0).getValue());
//                rowData.put("activeUsers", row.getMetricValues(0).getValue());
                rowData.put("Event name", row.getDimensionValues(1).getValue());
                rowData.put("Event count", row.getMetricValues(0).getValue());
                result.add(rowData);
            }
            return result;
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> saveEventIntoDatabase(ReportRequest reportRequest) {
        try{
            Campaign findCampaignExist= campaignRepository.findByCampaignId(reportRequest.getCampaignId())
                    .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Campaign is not found"));
            DimensionMaster findDimensionExist= dimensionMasterRepository.findByDimensionKey(reportRequest.getDimension())
                    .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Dimension is not found"));
            MetricMaster findMetricExist= metricMasterRepository.findByMetricKey(reportRequest.getMetric())
                    .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Metric is not found"));

            if(!findDimensionExist.getMetricMasters().contains(findMetricExist)){
                throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Metric is not valid");
            }
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
                            .addDimensions(Dimension.newBuilder().setName(reportRequest.getDimension()))
                            .addMetrics(Metric.newBuilder().setName(reportRequest.getMetric()))
                            .addDateRanges(DateRange.newBuilder().setStartDate(reportRequest.getStartDate()).setEndDate(reportRequest.getEndDate()))
                            .build();
            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);
            List<Row> rows = response.getRowsList();
            List<Map<String, String>> result = new ArrayList<>();
            List<Event> saveEvent= new ArrayList<>();
            for (Row row : rows) {
                Map<String, String> rowData = new HashMap<>();
                rowData.put(reportRequest.getDimension(), row.getDimensionValues(0).getValue());
                rowData.put(reportRequest.getMetric(), row.getMetricValues(0).getValue());
                Event event= Event.builder()
                        .campaign(findCampaignExist)
                        .eventName(row.getDimensionValues(0).getValue())
                        .eventLabel(findDimensionExist.getDimensionKey())
                        .eventValue(row.getMetricValues(0).getValue())
                        .timestamp(LocalDateTime.now())
                        .build();
                event.setDeletedFlag(DeletedFlag.ACTIVE);
                saveEvent.add(event);
                result.add(rowData);
            }
            eventRepository.saveAll(saveEvent);
            return result;
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }
    @Override
    public PageResponse<EventDto> getEvents(int pageNum, int pageSize, String eventLabel) {
        try{
            Pageable pageable= PageRequest.of(pageNum,pageSize);
            Page<Event> events= eventRepository.findDistinctEventsByEventLabel(eventLabel,pageable);
            return getEventDtoPageResponse(events);
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

    @NotNull
    private PageResponse<EventDto> getEventDtoPageResponse(Page<Event> events) {
        List<EventDto> data = events.stream().map(eventMapper::mapToDto).toList();
        PageResponse<EventDto> response= new PageResponse<>();
        response.setData(data);
        response.setCurrentPage(events.getNumber());
        response.setPageSize(events.getSize());
        response.setTotalPages(events.getTotalPages());
        response.setTotalElements(events.getTotalElements());
        return response;
    }

    @Override
    public PageResponse<EventDto> getEventsByStartDateAndEndDate(String startDate,
                                                                 String endDate,
                                                                 String eventLabel,
                                                                 int pageNum,
                                                                 int pageSize) {
        try{
            Pageable pageable= PageRequest.of(pageNum,pageSize);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateRaw = LocalDate.parse(startDate, dateFormatter);
            LocalDate endDateRaw = LocalDate.parse(endDate, dateFormatter);
            LocalDateTime startDateNew= startDateRaw.atStartOfDay();
            LocalDateTime endDateNew= endDateRaw.atTime(LocalTime.MAX);
            Page<Event> events= eventRepository.findDistinctEventsByEventLabelAndStartDateAndEndDate(eventLabel,startDateNew,endDateNew,pageable);
            return getEventDtoPageResponse(events);
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

}
