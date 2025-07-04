package com.example.chart.dto.carts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private CategoryResponseDTO parentCategory;
    private List<CategoryResponseDTO> subCategories;
}
