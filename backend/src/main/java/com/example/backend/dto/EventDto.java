package com.example.backend.dto;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long eventId;
    private String eventName;
    private String eventLabel;
    private String eventValue;
}
