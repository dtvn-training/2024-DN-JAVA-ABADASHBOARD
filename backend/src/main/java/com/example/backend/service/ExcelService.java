package com.example.backend.service;

import com.example.backend.dto.DailyStatisticDTO;
import com.example.backend.dto.PreviewDataDTO;
import com.example.backend.dto.response.PageResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface ExcelService {
    List<PreviewDataDTO<List<DailyStatisticDTO>>> previewByFilter(String startDate, String endDate);
}