package com.example.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NumberOfEventResponse {
    private int activeUser;
    private int eventCount;
}
