package com.example.backend.service.impl;

import com.example.backend.dto.DailyStatisticDTO;
import com.example.backend.dto.PreviewDataDTO;
import com.example.backend.repository.EventRepository;
import com.example.backend.repository.PurchaseRevenueRepository;
import com.example.backend.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final EventRepository eventRepository;
    private final PurchaseRevenueRepository purchaseRevenueRepository;


    @Override
    public List<PreviewDataDTO<List<DailyStatisticDTO>>> previewByFilter(String startDate, String endDate) {
        List<PreviewDataDTO<List<DailyStatisticDTO>>> previewDataList = new ArrayList<>();

        // Add City Statistics
        previewDataList.add(buildPreviewData(
                "City Statistics",
                Arrays.asList("DateEventOccurred", "City", "Value"),
                eventRepository.findCityStatisticsByDateRange(startDate, endDate)
        ));

        // Add Event Statistics
        previewDataList.add(buildPreviewData(
                "Event Statistics",
                Arrays.asList("Date", "Event Name", "Value"),
                eventRepository.findEventStatisticsByDateRange(startDate, endDate)
        ));

        // Add Purchase Statistics
        previewDataList.add(buildPreviewData(
                "Purchase Statistics",
                Arrays.asList("Day", "Event Name", "Value"),
                eventRepository.findPurchaseStatisticsByDateRange(startDate, endDate)
        ));

        // Add Media Statistics
        previewDataList.add(buildPreviewData(
                "Event Distribution by Media Channels",
                Arrays.asList("Media", "Event Count"),
                eventRepository.findMediaStatisticsByDateRange(startDate, endDate)
        ));

        // Add Daily Event Trends (Event Name)
        previewDataList.add(buildPreviewData(
                "Daily Event Trends",
                Arrays.asList("Day", "Event"),
                eventRepository.findEventsByEventLabelAndTimestamp("eventName", startDate, endDate)
        ));

        // Add
        previewDataList.add(buildPreviewData(
                "Origin of purchases",
                Arrays.asList("Media", "Count","TotalAmount($)"),
                purchaseRevenueRepository.findPurchaseCountAndTotalAmountBySourceNative()
        ));

        return previewDataList;
    }

    private PreviewDataDTO<List<DailyStatisticDTO>> buildPreviewData(
            String header,
            List<String> categories,
            List<Object[]> rawResults
    ) {
        // Map raw results into DailyStatisticDTO objects
        List<DailyStatisticDTO> statistics = rawResults.stream()
                .map(result -> {
                    // Safely extract data from result array
                    String day = result.length > 0 && result[0] != null ? result[0].toString() : null;
                    String title = result.length > 1 && result[1] != null ? result[1].toString() : null;
                    String value = result.length > 2 && result[2] != null ? result[2].toString() : null;

                    return new DailyStatisticDTO(day, title, value);
                })
                .collect(Collectors.toList());

        // Build and return the PreviewDataDTO
        return PreviewDataDTO.<List<DailyStatisticDTO>>builder()
                .header(header)
                .categories(categories)
                .data(statistics)
                .build();
    }
}
