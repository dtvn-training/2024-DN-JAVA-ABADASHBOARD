package com.example.backend.enums;

import lombok.Getter;

@Getter
public enum MetricType {
    ActiveUser("activeUsers"),
    EventCount("eventCount");

    MetricType(String name){
        this.name=name;
    }

    private final String name;
}
