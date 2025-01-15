package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class EventTableResponse {
    private String eventName;
    private Long totalValue;

    public EventTableResponse(String eventName, Long totalValue) {
        this.eventName = eventName;
        this.totalValue = totalValue;
    }
}
