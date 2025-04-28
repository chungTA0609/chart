package com.example.chart.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "dimensions")
public class Dimensions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String height;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String width;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String length;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String diameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_dimension_id", nullable = false)
    private Products product;
}