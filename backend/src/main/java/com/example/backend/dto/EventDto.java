package com.example.backend.dto;

import com.example.backend.enums.DeletedFlag;
import lombok.*;

import java.time.LocalDateTime;

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
