package com.example.chart.services;

import com.example.chart.dto.ProductImageDTO;
import java.util.List;

public interface ProductImageService {
    List<ProductImageDTO> saveImages(Long productId, List<ProductImageDTO> images);
    void deleteImagesByProductId(Long productId);
    List<ProductImageDTO> getImagesByProductId(Long productId);
} 