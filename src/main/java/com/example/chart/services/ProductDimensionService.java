package com.example.chart.services;

import com.example.chart.dto.products.ProductDimensionDTO;
import java.util.List;

public interface ProductDimensionService {
    List<ProductDimensionDTO> saveDimensions(Long productId, List<ProductDimensionDTO> dimensions);
    void deleteDimensionsByProductId(Long productId);
    List<ProductDimensionDTO> getDimensionsByProductId(Long productId);
} 