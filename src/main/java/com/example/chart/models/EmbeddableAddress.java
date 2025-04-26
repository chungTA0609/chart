package com.example.chart.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class EmbeddableAddress {
    private String fullName;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phone;
}
