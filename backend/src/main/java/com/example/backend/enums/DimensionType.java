package com.example.backend.enums;

import lombok.Getter;

@Getter
public enum DimensionType {
    EventName("eventName"),
    City("city");

    DimensionType(String name){
        this.name=name;
    }

    private final String name;

}
