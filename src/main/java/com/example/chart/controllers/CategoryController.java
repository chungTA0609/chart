package com.example.chart.controllers;

import com.example.chart.dto.carts.CategoryRequestDTO;
import com.example.chart.dto.carts.CategoryResponseDTO;
import com.example.chart.services.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private CategoryService categoryService;

    @GetMapping("/get-list")
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/create")
    public CategoryResponseDTO createCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {
        return categoryService.saveCategory(requestDTO);
    }
}