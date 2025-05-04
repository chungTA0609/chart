package com.example.chart.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotBlank(message = "Long description is required")
    private String longDescription;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Main image is required")
    private String mainImage;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotBlank(message = "Brand is required")
    private String brand;

    private List<ProductImageDTO> images;
    private List<ColorDTO> colors;
    private List<String> sizes;
    private List<ProductSpecificationDTO> specifications;
    private List<ProductDimensionDTO> dimensions;
    private boolean active;
    private Double rating;
    private Integer reviewCount;
} 