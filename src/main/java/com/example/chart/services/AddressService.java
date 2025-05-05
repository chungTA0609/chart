package com.example.chart.services;

import com.example.chart.dto.AddressDTO;
import com.example.chart.dto.AddressResponseDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);
    AddressDTO updateAddress(Long id, AddressDTO addressDTO);
    void deleteAddress(Long id);
    AddressResponseDTO getAddress(Long id);
    List<AddressResponseDTO> getUserAddresses(Long userId);
    AddressDTO setDefaultAddress(Long userId, Long addressId);
    AddressDTO getDefaultAddress(Long userId);
} 