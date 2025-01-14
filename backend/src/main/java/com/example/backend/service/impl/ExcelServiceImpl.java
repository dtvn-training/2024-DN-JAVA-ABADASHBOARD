package com.example.backend.service.impl;

import com.example.backend.constant.AppConstant;
import com.example.backend.dto.DailyStatisticDTO;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.repository.EventRepository;
import com.example.backend.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final EventRepository eventRepository;

    @Override
    public byte[] generateExcel() throws IOException {
        StringBuilder csvContent = new StringBuilder();
        String[] headers = { "ID", "Name", "Email" };
        csvContent.append(String.join(",", headers)).append("\n");

        String[][] data = {
                { "1", "John Doe", "john.doe@example.com" },
                { "2", "Jane Smith", "jane.smith@example.com" }
        };

        for (String[] rowData : data) {
            csvContent.append(String.join(",", rowData)).append("\n");
        }

        return csvContent.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public PageResponse<Map<String, List<DailyStatisticDTO>>> previewByFilter(String startDate,
                                                                              String endDate,
                                                                              int pageNum,
                                                                              int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(AppConstant.TIME_FORMAT);
        LocalDate startDateRaw = LocalDate.parse(startDate, dateFormatter);
        LocalDate endDateRaw = LocalDate.parse(endDate, dateFormatter);
        LocalDateTime startDateNew = startDateRaw.atStartOfDay();
        LocalDateTime endDateNew = endDateRaw.atTime(LocalTime.MAX);

        List<String> eventLabels = eventRepository.findDistinctEventLabels();

        Map<String, List<DailyStatisticDTO>> allResults = new HashMap<>();
        long totalElements = 0;
        int totalPages = 0;

        for (String label : eventLabels) {
            Page<Object[]> rawResults = eventRepository.findEventsByEventLabelAndTimestampPaginated(
                    label, startDateNew, endDateNew, pageable);

            List<DailyStatisticDTO> getEventByDate = rawResults.getContent().stream()
                    .map(result -> new DailyStatisticDTO(
                            String.valueOf(((BigDecimal) result[0]).intValue()),
                            ((BigDecimal) result[1]).toString()
                    ))
                    .toList();

            allResults.put(label, getEventByDate);

            if (totalElements == 0 && totalPages == 0) {
                totalElements = rawResults.getTotalElements();
                totalPages = rawResults.getTotalPages();
            }
        }

        return PageResponse.<Map<String, List<DailyStatisticDTO>>>builder()
                .currentPage(pageNum)
                .totalPages(totalPages)
                .pageSize(pageSize)
                .totalElements(totalElements)
                .data(Collections.singletonList(allResults))
                .build();
    }



}
