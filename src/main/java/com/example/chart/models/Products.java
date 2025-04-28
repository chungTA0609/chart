package com.example.chart.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(indexes = {
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_price", columnList = "price"),
        @Index(name = "idx_product_brand", columnList = "brand"),
        @Index(name = "idx_product_rating", columnList = "rating")
})
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;


    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category categories;

    private BigDecimal price;
    private Integer stockQuantity = 0;
    private String brand;

    @Size(max = 2000)
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

    @PositiveOrZero
    @Max(5)
    private Double rating;

    @PositiveOrZero
    private Integer reviews;

    private List<String> colors;

    private List<String> sizes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Specifications> specifications;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dimensions> dimensions;


    private Integer popularity;
    private LocalDateTime createdDate;

    @Version
    private Long version; // For optimistic locking

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        popularity = 0;
    }

    private LocalDateTime updatedAt;

}
