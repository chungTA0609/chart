package com.example.chart.services.impl;

import com.example.chart.dto.ShippingMethodDTO;
import com.example.chart.models.ShippingMethod;
import com.example.chart.repository.ShippingMethodRepository;
import com.example.chart.services.ShippingMethodService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {
    private final ShippingMethodRepository shippingMethodRepository;

    @Override
    @Transactional
    public ShippingMethodDTO createShippingMethod(ShippingMethodDTO shippingMethodDTO) {
        ShippingMethod shippingMethod = new ShippingMethod();
        shippingMethod.setName(shippingMethodDTO.getName());
        shippingMethod.setDescription(shippingMethodDTO.getDescription());
        shippingMethod.setPrice(shippingMethodDTO.getPrice());
        shippingMethod.setEstimatedDays(shippingMethodDTO.getEstimatedDays());
        shippingMethod.setActive(shippingMethodDTO.isActive());

        ShippingMethod savedMethod = shippingMethodRepository.save(shippingMethod);
        return convertToDTO(savedMethod);
    }

    @Override
    @Transactional
    public ShippingMethodDTO updateShippingMethod(Long id, ShippingMethodDTO shippingMethodDTO) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipping method not found"));

        shippingMethod.setName(shippingMethodDTO.getName());
        shippingMethod.setDescription(shippingMethodDTO.getDescription());
        shippingMethod.setPrice(shippingMethodDTO.getPrice());
        shippingMethod.setEstimatedDays(shippingMethodDTO.getEstimatedDays());
        shippingMethod.setActive(shippingMethodDTO.isActive());

        ShippingMethod updatedMethod = shippingMethodRepository.save(shippingMethod);
        return convertToDTO(updatedMethod);
    }

    @Override
    @Transactional
    public void deleteShippingMethod(Long id) {
        if (!shippingMethodRepository.existsById(id)) {
            throw new EntityNotFoundException("Shipping method not found");
        }
        shippingMethodRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ShippingMethodDTO getShippingMethod(Long id) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipping method not found"));
        return convertToDTO(shippingMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingMethodDTO> getAllActiveShippingMethods() {
        return shippingMethodRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void toggleShippingMethodStatus(Long id) {
        ShippingMethod shippingMethod = shippingMethodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shipping method not found"));
        shippingMethod.setActive(!shippingMethod.isActive());
        shippingMethodRepository.save(shippingMethod);
    }

    private ShippingMethodDTO convertToDTO(ShippingMethod shippingMethod) {
        ShippingMethodDTO dto = new ShippingMethodDTO();
        dto.setId(shippingMethod.getId());
        dto.setName(shippingMethod.getName());
        dto.setDescription(shippingMethod.getDescription());
        dto.setPrice(shippingMethod.getPrice());
        dto.setEstimatedDays(shippingMethod.getEstimatedDays());
        dto.setActive(shippingMethod.isActive());
        return dto;
    }
} 