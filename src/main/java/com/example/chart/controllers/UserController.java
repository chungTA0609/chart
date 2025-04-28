package com.example.chart.controllers;

import com.example.chart.core.ApiResponse;
import com.example.chart.dto.AddressDTO;
import com.example.chart.dto.UserDTO;
import com.example.chart.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userDTO = userService.getUserProfile(email);
        return ResponseEntity.ok(ApiResponse.success(userDTO, "Profile retrieved successfully"));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@RequestBody UserDTO userDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO updatedUser = userService.updateUserProfile(email, userDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "Profile updated successfully"));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody String newPassword) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changePassword(email, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }

    @PostMapping("/address")
    public ResponseEntity<ApiResponse<AddressDTO>> addAddress(@RequestBody AddressDTO addressDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        AddressDTO addedAddress = userService.addAddress(email, addressDTO);
        return ResponseEntity.ok(ApiResponse.success(addedAddress, "Address added successfully"));
    }

    @GetMapping("/address")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AddressDTO> addresses = userService.getAddresses(email);
        return ResponseEntity.ok(ApiResponse.success(addresses, "Addresses retrieved successfully"));
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }
}


