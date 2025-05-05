package com.example.chart.services;

import com.example.chart.dto.ShippingMethodDTO;
import java.util.List;

public interface ShippingMethodService {
    ShippingMethodDTO createShippingMethod(ShippingMethodDTO shippingMethodDTO);
    ShippingMethodDTO updateShippingMethod(Long id, ShippingMethodDTO shippingMethodDTO);
    void deleteShippingMethod(Long id);
    ShippingMethodDTO getShippingMethod(Long id);
    List<ShippingMethodDTO> getAllActiveShippingMethods();
    void toggleShippingMethodStatus(Long id);
} 