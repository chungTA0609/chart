package com.example.chart.services;

import com.example.chart.dto.carts.CategoryRequestDTO;
import com.example.chart.dto.carts.CategoryResponseDTO;
import com.example.chart.helpers.DtoMapper;
import com.example.chart.models.Category;
import com.example.chart.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;
    private DtoMapper dtoMapper;

    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(dtoMapper::mapToCategoryResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponseDTO saveCategory(CategoryRequestDTO requestDTO) {
        Category parentCategory = requestDTO.getParentCategoryId() != null ?
                categoryRepository.findById(requestDTO.getParentCategoryId())
                        .orElseThrow(() -> new RuntimeException("Parent category not found")) :
                null;
        Category category = dtoMapper.mapToCategoryEntity(requestDTO, parentCategory);
        Category savedCategory = categoryRepository.save(category);
        return dtoMapper.mapToCategoryResponseDTO(savedCategory);
    }
}
