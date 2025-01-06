package com.example.backend.enums;

import lombok.Getter;

@Getter
public enum MetricType {
    ActiveUser("activeUsers"),
    EventCount("eventCount"),
    EventCountPerUser("eventCountPerUser"),
    NewUsers("newUsers"),
    ItemsViewed("itemsViewed"),
    ScreenPageViews("screenPageViews"),
    Sessions("sessions"),
    PurchaseRevenue("purchaseRevenue"),;

    MetricType(String name){
        this.name=name;
    }

    private final String name;

    public static MetricType findByName(String name) {
        for (MetricType metricType : MetricType.values()) {
            if (metricType.name.equals(name)) {
                return metricType;
            }
        }
        throw new IllegalArgumentException("No MetricType with name " + name + " found.");
    }
}
