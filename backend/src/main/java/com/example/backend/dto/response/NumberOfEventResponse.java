package com.example.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class NumberOfEventResponse {
    private String eventLabel;
    private int totalValue;

    public NumberOfEventResponse(String eventLabel, int totalValue) {
        this.eventLabel = eventLabel;
        this.totalValue = totalValue;
    }
}
