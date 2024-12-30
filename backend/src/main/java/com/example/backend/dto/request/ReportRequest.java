package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ReportRequest {
    @NotNull(message = "Dimension is required")
    private List<String> dimensions;
    @NotNull(message = "Metric is required")
    private List<String> metrics;
    @NotNull(message = "medium is required")
    private String medium;
    @NotNull(message = "time is required")
    private int time;
    @NotNull(message = "campaignId is required")
    private long campaignId;
}
