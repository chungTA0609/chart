package com.example.chart.services;

import com.example.chart.dto.ColorDTO;
import java.util.List;

public interface ColorService {
    List<ColorDTO> saveColors(Long productId, List<ColorDTO> colors);
    void deleteColorsByProductId(Long productId);
    List<ColorDTO> getColorsByProductId(Long productId);
} 