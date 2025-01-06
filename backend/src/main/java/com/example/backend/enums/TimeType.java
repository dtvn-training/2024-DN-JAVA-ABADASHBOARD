package com.example.backend.enums;

import lombok.Getter;

@Getter
public enum TimeType {
    LAST_WEEKLY("last week"),
    THIS_MONTH("this month"),
    LAST_MONTH("last month"),
    YEAR("year");

    TimeType(String name){
        this.name=name;
    }

    private final String name;

    public static TimeType findByName(String name) {
        for (TimeType timeType : TimeType.values()) {
            if (timeType.name.equals(name)) {
                return timeType;
            }
        }
        throw new IllegalArgumentException("No MetricType with name " + name + " found.");
    }
}
