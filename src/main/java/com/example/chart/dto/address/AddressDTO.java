package com.example.chart.dto.address;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private Long userId;
    private boolean isDefault;
}