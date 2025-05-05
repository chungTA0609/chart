package com.example.chart.services.impl;

import com.example.chart.dto.ColorDTO;
import com.example.chart.models.Color;
import com.example.chart.repository.ColorRepository;
import com.example.chart.services.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    @Transactional
    public List<ColorDTO> saveColors(Long productId, List<ColorDTO> colors) {
        // Delete existing colors for the product
        deleteColorsByProductId(productId);

        // Create and save new colors
        List<Color> savedColors = colors.stream()
            .map(colorDTO -> {
                Color color = new Color();
                color.setName(colorDTO.getName());
                color.setValue(colorDTO.getValue());
                color.setHex(colorDTO.getHex());
                color.setProductId(productId);
                return colorRepository.save(color);
            })
            .collect(Collectors.toList());

        // Map saved colors back to DTOs
        return savedColors.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteColorsByProductId(Long productId) {
        colorRepository.deleteByProductId(productId);
    }

    @Override
    public List<ColorDTO> getColorsByProductId(Long productId) {
        return colorRepository.findByProductId(productId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private ColorDTO mapToDTO(Color color) {
        ColorDTO dto = new ColorDTO();
        dto.setId(color.getId());
        dto.setName(color.getName());
        dto.setValue(color.getValue());
        dto.setHex(color.getHex());
        dto.setProductId(color.getProductId());
        return dto;
    }
} 