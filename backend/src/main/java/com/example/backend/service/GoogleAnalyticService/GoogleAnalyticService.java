package com.example.backend.service.GoogleAnalyticService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GoogleAnalyticService {
    List<Map<String, String>> reportResponse();
    List<Map<String,String>> getReportByParam(String dimension, String metric);
}
