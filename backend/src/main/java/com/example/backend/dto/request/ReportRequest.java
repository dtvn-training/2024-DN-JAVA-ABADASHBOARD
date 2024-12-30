package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.List;

@Getter
public class ReportRequest {
    @NotNull(message = "Dimension is required")
    private String dimension;
    @NotNull(message = "Metric is required")
    private String metric;
    @Nullable
    private String medium;
    @NotNull(message = "time is required")
    private int time;
    @NotNull(message = "start date is required")
    private String startDate;
    @NotNull(message = "end date is required")
    private String endDate;
    @NotNull(message = "campaignId is required")
    private long campaignId;
}
