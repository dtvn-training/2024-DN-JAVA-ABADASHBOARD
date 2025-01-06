package com.example.backend.dto.request;

import lombok.Getter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
public class ListEventByDayRequest {
    @Nullable
    private String type;
    @Nullable
    private LocalDateTime startDate;
    @Nullable
    private LocalDateTime endDate;
}
