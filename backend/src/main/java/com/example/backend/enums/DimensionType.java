package com.example.backend.enums;

import lombok.Getter;

import java.util.Date;

@Getter
public enum DimensionType {
    EventName("eventName"),
    City("city"),
    Source("source"),
    PageTitle("pageTitle"),
    PagePath("pagePath"),
    Medium("medium"),
    Date("date");

    DimensionType(String name){
        this.name=name;
    }

    private final String name;

    public static DimensionType findByName(String name) {
        for (DimensionType dimension : DimensionType.values()) {
            if (dimension.name.equals(name)) {
                return dimension;
            }
        }
        throw new IllegalArgumentException("No DimensionType with name " + name + " found.");
    }

}
