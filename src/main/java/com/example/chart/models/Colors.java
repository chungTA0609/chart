package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "colors")
@Entity
public class Colors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String value;
    private String hex;
}
