package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class EventChartResponse {
    private LocalDateTime time;
    private String eventTitle;
    private Long eventValue;

    public EventChartResponse(LocalDateTime time, String eventTitle, Long eventValue) {
        this.time = time;
        this.eventTitle = eventTitle;
        this.eventValue = eventValue;
    }
}