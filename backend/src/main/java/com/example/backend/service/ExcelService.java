package com.example.backend.service;

import com.example.backend.dto.DailyStatisticDTO;
import com.example.backend.dto.response.PageResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ExcelService {
    byte[] generateExcel() throws IOException;
    PageResponse<Map<String, List<DailyStatisticDTO>>> previewByFilter(String startDate, String endDate,
                                                                       int pageNum, int pageSize);
}