package com.example.chart.controllers;

import com.example.chart.core.ApiResponse;
import com.example.chart.dto.AddressDTO;
import com.example.chart.dto.AddressResponseDTO;
import com.example.chart.models.Address;
import com.example.chart.models.User;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.AddressService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final UserRepository userRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
                .getId();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressDTO>> createAddress(@Valid @RequestBody AddressDTO address) {
        return ResponseEntity.ok(ApiResponse.success(
            addressService.createAddress(address),
            "Address created successfully"
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDTO>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressDTO address) {
        return ResponseEntity.ok(ApiResponse.success(
            addressService.updateAddress(id, address),
            "Address updated successfully"
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success(
            null,
            "Address deleted successfully"
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponseDTO>> getAddress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
            addressService.getAddress(id),
            "Address retrieved successfully"
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> getUserAddresses() {
        return ResponseEntity.ok(ApiResponse.success(
            addressService.getUserAddresses(getCurrentUserId()),
            "User addresses retrieved successfully"
        ));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ApiResponse<AddressDTO>> setDefaultAddress(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
            addressService.setDefaultAddress(getCurrentUserId(), id),
            "Default address updated successfully"
        ));
    }

    @GetMapping("/default")
    public ResponseEntity<ApiResponse<AddressDTO>> getDefaultAddress() {
        return ResponseEntity.ok(ApiResponse.success(
            addressService.getDefaultAddress(getCurrentUserId()),
            "Default address retrieved successfully"
        ));
    }
} 