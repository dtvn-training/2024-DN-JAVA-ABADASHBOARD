package com.example.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class NumberOfEventResponse {
    private String eventTitle;
    private Long totalValue;

    public NumberOfEventResponse(String eventTitle, Long totalValue) {
        this.eventTitle = eventTitle;
        this.totalValue = totalValue;
    }
}
