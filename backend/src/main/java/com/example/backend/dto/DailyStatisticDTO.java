package com.example.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatisticDTO {
    private String Day;
    private String value;
}

