package com.example.backend.service.GoogleAnalyticService.Impl;

import com.example.backend.dto.request.ReportRequest;
import com.example.backend.entity.Campaign;
import com.example.backend.entity.DimensionMaster;
import com.example.backend.entity.MetricMaster;
import com.example.backend.enums.DimensionType;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.MetricType;
import com.example.backend.exception.ApiException;
import com.example.backend.repository.CampaignRepository;
import com.example.backend.repository.DimensionMasterRepository;
import com.example.backend.repository.EventRepository;
import com.example.backend.repository.MetricMasterRepository;
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import com.google.analytics.data.v1beta.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    @Override
    public List<Map<String, String>> reportResponse() {
        try{
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.City.getName()))
                            .addDimensions(Dimension.newBuilder().setName(DimensionType.EventName.getName()))
                            .addMetrics(Metric.newBuilder().setName(MetricType.ActiveUser.getName()))
                            .addMetrics(Metric.newBuilder().setName(MetricType.EventCount.getName()))
                            .addDateRanges(DateRange.newBuilder().setStartDate("2024-11-25").setEndDate("today"))
                            .build();
            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);

            List<Map<String, String>> result = new ArrayList<>();
            for (Row row : response.getRowsList()) {
                Map<String, String> rowData = new HashMap<>();
                rowData.put("city", row.getDimensionValues(0).getValue());
                rowData.put("activeUsers", row.getMetricValues(0).getValue());
                rowData.put("Event name", row.getDimensionValues(1).getValue());
                rowData.put("Event count", row.getMetricValues(1).getValue());
                result.add(rowData);
            }
            return result;
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @Override
    public List<Map<String, String>> getReportByParam(ReportRequest reportRequest) {
        try{
            Campaign findCampaignExist= campaignRepository.findByCampaignId(reportRequest.getCampaignId())
                    .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Campaign is not found"));
            List<Dimension> dimensions= new ArrayList<>();
            for(String dimensionKey : reportRequest.getDimensions()){
                DimensionMaster findDimensionExist= dimensionMasterRepository.findByDimensionKey(dimensionKey)
                        .orElseThrow(()-> new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Dimension is not found"));
                Dimension dimension= Dimension.newBuilder()
                        .setName(findDimensionExist.getDimensionKey())
                        .build();
                dimensions.add(dimension);
            }
            List<Metric> metrics= new ArrayList<>();
            for(String metricKey : reportRequest.getMetrics()){
                MetricMaster findMetricExist= metricMasterRepository.findByMetricKey(metricKey)
                        .orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),"Metric is not found"));
                Metric metric= Metric.newBuilder()
                        .setName(findMetricExist.getMetricKey())
                        .build();
                metrics.add(metric);
            }

            LocalDateTime now = LocalDateTime.now();
            now= now.minusDays(reportRequest.getTime());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = now.format(formatter);


            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
                            .addAllDimensions(dimensions)
                            .addAllMetrics(metrics)
                            .addDateRanges(DateRange.newBuilder().setStartDate(formattedDate).setEndDate("today"))
                            .build();
            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);
            List<Row> rows = response.getRowsList();
            List<Map<String, String>> result = new ArrayList<>();

            for(int i=0;i<rows.size();i++){
                Map<String, String> rowData = new HashMap<>();
                rowData.put(reportRequest.getDimensions().getFirst(),rows.get(i).getDimensionValues(0).getValue());
                rowData.put(reportRequest.getDimensions().get(1),rows.get(i).getDimensionValues(1).getValue());
                rowData.put(reportRequest.getMetrics().getFirst(),rows.get(i).getMetricValues(0).getValue());
                result.add(rowData);
            }
//            for (Row row : response.getRowsList()) {
//                Map<String, String> rowData = new HashMap<>();
//                rowData.put(, row.getDimensionValues(0).getValue());
//                rowData.put(row.getDimensionValues(1).getValue(), row.getMetricValues(0).getValue());
//                rowData.put(reportRequest.getMetric(), row.getMetricValues(0).getValue());
//                result.add(rowData);
//            }

            return result;
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

}
