package com.example.chart.models;

public enum ShippingMethod {
    STANDARD("Standard Shipping", 5.00),
    EXPRESS("Express Shipping", 10.00),
    NEXT_DAY("Next Day Delivery", 15.00),
    PICKUP("Store Pickup", 0.00);

    private final String displayName;
    private final double cost;

    ShippingMethod(String displayName, double cost) {
        this.displayName = displayName;
        this.cost = cost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getCost() {
        return cost;
    }
} 