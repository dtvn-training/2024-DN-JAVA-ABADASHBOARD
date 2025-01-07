package com.example.backend.service.GoogleAnalyticService.Impl;

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
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import com.google.analytics.data.v1beta.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
    private final BetaAnalyticsDataClient analyticsData;
    @Value("${google-analytic.properties_id}")
    private String propertyId;
    private final DimensionMasterRepository dimensionMasterRepository;
    private final MetricMasterRepository metricMasterRepository;
    private final CampaignRepository campaignRepository;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final PurchaseRevenueRepository purchaseRevenueRepository;
    private final MediumRepository mediumRepository;

    @Override
    public List<Map<String, String>> reportResponse() {
        try{
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
//                            .addDimensions(Dimension.newBuilder().setName(DimensionType.City.getName()))
                            .addDimensions(Dimension.newBuilder().setName("date"))
                            .addDimensions(Dimension.newBuilder().setName("sessionMedium"))
                            .addDimensions(Dimension.newBuilder().setName("eventName"))
//                            .addMetrics(Metric.newBuilder().setName(MetricType.EventCount.getName()))
                            .addMetrics(Metric.newBuilder().setName("eventCount"))
                            .addDateRanges(DateRange.newBuilder().setStartDate("2024-11-25").setEndDate("today"))
                            .build();
            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);

            List<Map<String, String>> result = new ArrayList<>();
            for (Row row : response.getRowsList()) {
                Map<String, String> rowData = new HashMap<>();
                String rawDate = row.getDimensionValues(0).getValue(); // Giá trị thô "date"
                String formattedDate = LocalDate.parse(rawDate, DateTimeFormatter.BASIC_ISO_DATE).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                rowData.put("date", formattedDate);
//                rowData.put("activeUsers", row.getMetricValues(0).getValue());
                rowData.put("sessionMedium", row.getDimensionValues(1).getValue());
                rowData.put("event name", row.getDimensionValues(2).getValue());
//                rowData.put("event count", row.getMetricValues(0).getValue());
                rowData.put("purchase Revenue", row.getMetricValues(0).getValue());
                Optional<Medium> checkMediumExist= mediumRepository.findByMediumName(row.getDimensionValues(1).getValue());
                if(checkMediumExist.isEmpty()) {
                    Medium medium= Medium.builder()
                            .mediumName(row.getDimensionValues(1).getValue())
                            .build();
                    mediumRepository.save(medium);
                }
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

            if(!findDimensionExist.getMetricMasters().contains(findMetricExist)) {
                throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), "Metric is not valid");
            }
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.Date.getName()))
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.SessionMedium.getName()))
                            .addDimensions(Dimension.newBuilder().setName(reportRequest.getDimension()))
                            .addMetrics(Metric.newBuilder().setName(reportRequest.getMetric()))
                            .addDateRanges(DateRange.newBuilder().setStartDate(reportRequest.getStartDate()).setEndDate(reportRequest.getEndDate()))
                            .build();
            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);
            List<Row> rows = response.getRowsList();
            List<Map<String, String>> result = new ArrayList<>();
            List<Event> saveEvent= new ArrayList<>();
            List<PurchaseRevenue> savePurchaseRevenue= new ArrayList<>();
            for (Row row : rows) {
                Map<String, String> rowData = new HashMap<>();
                String rawDate = row.getDimensionValues(0).getValue(); // Giá trị thô "date"
                LocalDate date= LocalDate.parse(rawDate, DateTimeFormatter.BASIC_ISO_DATE);
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long minSecond = date.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC);
                long maxSecond = date.plusDays(1).atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC);
                long randomSecond = ThreadLocalRandom.current().nextLong(minSecond, maxSecond);
                LocalDateTime dateTime = LocalDateTime.ofEpochSecond(randomSecond, 0, java.time.ZoneOffset.UTC);
                rowData.put(DimensionType.Date.getName(), formattedDate);
                rowData.put(reportRequest.getDimension(), row.getDimensionValues(2).getValue());
                rowData.put(reportRequest.getMetric(), row.getMetricValues(0).getValue());

                Event event= Event.builder()
                        .campaign(findCampaignExist)
                        .eventName(row.getDimensionValues(2).getValue())
                        .eventLabel(findDimensionExist.getDimensionKey())
                        .eventValue(row.getMetricValues(0).getValue())
                        .timestamp(dateTime)
                        .build();

                Optional<Medium> checkMediumExist= mediumRepository.findByMediumName(row.getDimensionValues(1).getValue());
                if(checkMediumExist.isPresent()) {
                    event.setMedium(checkMediumExist.get());
                }else{
                    Medium medium= Medium.builder()
                            .mediumName(row.getDimensionValues(1).getValue())
                            .build();
                    mediumRepository.save(medium);
                    event.setMedium(medium);
                }
                event.setDeletedFlag(DeletedFlag.ACTIVE);
                if(row.getDimensionValues(2).getValue().equals("purchase")){
                    RunReportRequest requestPurchase =
                            RunReportRequest.newBuilder()
                                    .setProperty("properties/" + propertyId)
                                    .addDimensions(Dimension.newBuilder().setName(DimensionType.Date.getName()))
                                    .addDimensions(Dimension.newBuilder().setName(DimensionType.Source.getName()))
                                    .addDimensions(Dimension.newBuilder().setName(DimensionType.EventName.getName()))
                                    .addMetrics(Metric.newBuilder().setName(MetricType.PurchaseRevenue.getName()))
                                    .addDateRanges(DateRange.newBuilder().setStartDate(formattedDate).setEndDate(formattedDate))
                                    .build();
                    // Make the request.
                    RunReportResponse responsePurchase = analyticsData.runReport(requestPurchase);
                    List<Row> rowsPurchase = responsePurchase.getRowsList();
                    for(Row rowPurchase: rowsPurchase) {
                        double amount = Double.parseDouble(rowPurchase.getMetricValues(0).getValue());
                        PurchaseRevenue purchaseRevenue= PurchaseRevenue.builder()
                                .event(event)
                                .amount(BigDecimal.valueOf(amount))
                                .source(rowPurchase.getDimensionValues(1).getValue())
                                .build();
                        savePurchaseRevenue.add(purchaseRevenue);
                    }

                }

                saveEvent.add(event);
                result.add(rowData);
            }
            eventRepository.saveAll(saveEvent);
            purchaseRevenueRepository.saveAll(savePurchaseRevenue);
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
    @NotNull
    private PageResponse<EventTableResponse> getEventTablePageResponse(Page<Event> events, List<EventTableResponse> data) {
        PageResponse<EventTableResponse> response= new PageResponse<>();
        response.setData(data);
        response.setCurrentPage(events.getNumber());
        response.setPageSize(events.getSize());
        response.setTotalPages(events.getTotalPages());
        response.setTotalElements(events.getTotalElements());
        return response;
    }

    @Override
    public Map<String, Object> getEventsByStartDateAndEndDate(String startDate,
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
            PagedResourcesAssembler<EventTableResponse> pagedResourcesAssembler= new PagedResourcesAssembler<>(null,null);
            Page<EventTableResponse> eventsTableResponse= eventRepository.findEventsByEventLabelAndTimestampBetween(eventLabel,startDateNew,endDateNew,pageable);
            List<NumberOfEventResponse> numberOfEventResponses= eventRepository.numberOfEventsByEventLabel(startDateNew,endDateNew);
            List<EventChartResponse> getEventChartResponse= eventRepository.getEventsForChart(startDateNew,endDateNew);
            Map<String, Object> response= new HashMap<>();
            response.put("eventTable", pagedResourcesAssembler.toModel(eventsTableResponse));
            response.put("numberOfEvent",numberOfEventResponses);
            response.put("chartEvent", getEventChartResponse);
            return response;
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

    @Override
    public PageResponse<EventTableResponse> getEventByMedium(String mediumName, int pageNum, int pageSize, String eventLabel, String startDate, String endDate) {
        try{
            Pageable pageable= PageRequest.of(pageNum,pageSize);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDateRaw = LocalDate.parse(startDate, dateFormatter);
            LocalDate endDateRaw = LocalDate.parse(endDate, dateFormatter);
            LocalDateTime startDateNew= startDateRaw.atStartOfDay();
            LocalDateTime endDateNew= endDateRaw.atTime(LocalTime.MAX);
            Page<Event> getEvents= eventRepository.getEventsByMediumId(eventLabel,startDateNew,endDateNew,mediumName,pageable);
            List<EventTableResponse> responses= getEvents.stream().collect(Collectors.groupingBy(
                    Event::getEventName,
                            Collectors.summingLong(event-> Long.parseLong(event.getEventValue()))
                    ))
                    .entrySet()
                    .stream()
                    .map(entry-> new EventTableResponse(entry.getKey(), entry.getValue()))
                    .toList();
            return getEventTablePageResponse(getEvents,responses);
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

    @Override
    public List<Event> getReportFromGA4MapToEvent(String dimension, String metric, String campaignName) {
        try{
            Campaign findCampaignExist= campaignRepository.findByCampaignName(campaignName)
                    .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Campaign not found"));
            DimensionMaster findDimensionExist= dimensionMasterRepository.findByDimensionKey(dimension)
                    .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Dimension is not found"));
            MetricMaster findMetricExist= metricMasterRepository.findByMetricKey(metric)
                    .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Metric is not found"));

            if(!findDimensionExist.getMetricMasters().contains(findMetricExist)) {
                throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), "Metric is not valid");
            }
            LocalDateTime endDate= LocalDateTime.now();
            LocalDateTime startDate= endDate.minusDays(3);
            String startDateNew= startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String endDateNew= endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.Date.getName()))
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.SessionMedium.getName()))
                            .addDimensions(Dimension.newBuilder().setName(dimension))
                            .addMetrics(Metric.newBuilder().setName(metric))
                            .addDateRanges(DateRange.newBuilder().setStartDate(startDateNew).setEndDate(endDateNew))
                            .build();
            RunReportResponse response = analyticsData.runReport(request);
            List<Row> rows = response.getRowsList();
            List<Event> events= new ArrayList<>();
            for (Row row : rows) {
                String rawDate = row.getDimensionValues(0).getValue(); // Giá trị thô "date"
                LocalDate date= LocalDate.parse(rawDate, DateTimeFormatter.BASIC_ISO_DATE);
                String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                long minSecond = date.atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC);
                long maxSecond = date.plusDays(1).atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC);
                long randomSecond = ThreadLocalRandom.current().nextLong(minSecond, maxSecond);
                LocalDateTime dateTime = LocalDateTime.ofEpochSecond(randomSecond, 0, java.time.ZoneOffset.UTC);
                Event event= Event.builder()
                        .campaign(findCampaignExist)
                        .eventName(row.getDimensionValues(2).getValue())
                        .eventLabel(findDimensionExist.getDimensionKey())
                        .eventValue(row.getMetricValues(0).getValue())
                        .timestamp(dateTime)
                        .build();
                Optional<Medium> checkMediumExist= mediumRepository.findByMediumName(row.getDimensionValues(1).getValue());
                if(checkMediumExist.isPresent()) {
                    event.setMedium(checkMediumExist.get());
                }else{
                    Medium medium= Medium.builder()
                            .mediumName(row.getDimensionValues(1).getValue())
                            .build();
                    mediumRepository.save(medium);
                    event.setMedium(medium);
                }
                event.setDeletedFlag(DeletedFlag.ACTIVE);
                if(row.getDimensionValues(2).getValue().equals("purchase")){
                    RunReportRequest requestPurchase =
                            RunReportRequest.newBuilder()
                                    .setProperty("properties/" + propertyId)
                                    .addDimensions(Dimension.newBuilder().setName(DimensionType.Date.getName()))
                                    .addDimensions(Dimension.newBuilder().setName(DimensionType.Source.getName()))
                                    .addDimensions(Dimension.newBuilder().setName(DimensionType.EventName.getName()))
                                    .addMetrics(Metric.newBuilder().setName(MetricType.PurchaseRevenue.getName()))
                                    .addDateRanges(DateRange.newBuilder().setStartDate(formattedDate).setEndDate(formattedDate))
                                    .build();
                    // Make the request.
                    RunReportResponse responsePurchase = analyticsData.runReport(requestPurchase);
                    List<Row> rowsPurchase = responsePurchase.getRowsList();
                    List<PurchaseRevenue> purchaseRevenues= new ArrayList<>();
                    for(Row rowPurchase: rowsPurchase) {
                        double amount = Double.parseDouble(rowPurchase.getMetricValues(0).getValue());
                        PurchaseRevenue purchaseRevenue= PurchaseRevenue.builder()
                                .event(event)
                                .amount(BigDecimal.valueOf(amount))
                                .source(rowPurchase.getDimensionValues(1).getValue())
                                .build();
                        purchaseRevenues.add(purchaseRevenue);
                    }
                    event.setPurchaseRevenues(purchaseRevenues);
                }
                events.add(event);
            }
            return events;
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(), e.getMessage());
        }
    }

}
