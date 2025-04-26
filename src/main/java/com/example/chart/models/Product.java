package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_price", columnList = "price"),
        @Index(name = "idx_product_brand", columnList = "brand"),
        @Index(name = "idx_product_rating", columnList = "rating")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ElementCollection
    private List<String> images;

    @ManyToMany
    private List<Category> categories;

    private BigDecimal price;
    private Integer stockQuantity;
    private String brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

    private Double rating;
    private Integer popularity;
    private LocalDateTime createdDate;

    @Version
    private Long version; // For optimistic locking

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        popularity = 0;
    }
}
