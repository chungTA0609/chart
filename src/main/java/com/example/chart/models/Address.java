package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String country;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    @Column(name = "user_id", nullable = false)
    private Long userId;
}
