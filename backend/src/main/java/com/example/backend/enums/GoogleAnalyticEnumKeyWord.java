package com.example.backend.enums;

import lombok.Getter;

@Getter
public enum GoogleAnalyticEnumKeyWord {
    PROPERTIES_DEFAULT("abalightacademy.com"),
    TRACKING_ID("G-1DXQ4J3ERZ");

    GoogleAnalyticEnumKeyWord(String name){
        this.name=name;
    }
    private final String name;
}
