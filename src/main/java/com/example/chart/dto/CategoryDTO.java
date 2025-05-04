package com.example.chart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
    private Long parentId;
    private Set<CategoryDTO> subcategories;
    private boolean active;
} 