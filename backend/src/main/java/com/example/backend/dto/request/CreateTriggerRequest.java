package com.example.backend.dto.request;

import lombok.Getter;

@Getter
public class CreateTriggerRequest {
    private String triggerName;
    private String triggerType;
}
