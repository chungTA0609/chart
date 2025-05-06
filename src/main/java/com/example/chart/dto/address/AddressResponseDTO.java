package com.example.chart.dto.address;

import lombok.Data;

@Data
public class AddressResponseDTO {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phoneNumber;
    private String email;
    private Long userId;
    private boolean isDefault;
    private String fullName;
}