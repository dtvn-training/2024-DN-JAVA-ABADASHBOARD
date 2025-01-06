package com.example.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum GoogleTagEnumKeyWord {
    CONTAINER_NAME("abalightacademy.com"),
    TRACKING_ID("G-1DXQ4J3ERZ");

    GoogleTagEnumKeyWord(String name){
        this.name=name;
    }
    private final String name;
}
