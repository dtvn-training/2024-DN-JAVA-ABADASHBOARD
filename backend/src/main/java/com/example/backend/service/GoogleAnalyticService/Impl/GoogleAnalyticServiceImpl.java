package com.example.backend.service.GoogleAnalyticService.Impl;

import com.example.backend.enums.DimensionType;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.MetricType;
import com.example.backend.exception.ApiException;
import com.example.backend.service.GoogleAnalyticService.GoogleAnalyticService;
import com.google.analytics.data.v1beta.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleAnalyticServiceImpl implements GoogleAnalyticService {
    private final BetaAnalyticsDataClient analyticsData;
    @Value("${google-analytic.properties_id}")
    private String propertyId;
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

}
