package com.example.chart.controllers;

import com.example.chart.core.ApiResponse;
import com.example.chart.dto.address.AddressDTO;
import com.example.chart.dto.UserDTO;
import com.example.chart.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserDTO>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success(userService.getCurrentUser(), "User profile retrieved successfully"));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<UserDTO>> updateProfile(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateCurrentUser(userDTO), "Profile updated successfully"));
    }

    @PutMapping("/profile/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        userService.updatePassword(currentPassword, newPassword);
        return ResponseEntity.ok(ApiResponse.success(null, "Password updated successfully"));
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id), "User retrieved successfully"));
    }

    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(ApiResponse.success(
            userService.getAllUsers(page, size, sortField, sortDirection), 
            "Users retrieved successfully"
        ));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUser(id, userDTO), "User updated successfully"));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deleted successfully"));
    }

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<ApiResponse<UserDTO>> updateUserStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        return ResponseEntity.ok(ApiResponse.success(userService.updateUserStatus(id, active), "User status updated successfully"));
    }

    @PostMapping("/address")
    public ResponseEntity<ApiResponse<AddressDTO>> addAddress(@RequestBody AddressDTO addressDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ApiResponse.success(userService.addAddress(email, addressDTO), "Address added successfully"));
    }

    @GetMapping("/address")
    public ResponseEntity<ApiResponse<List<AddressDTO>>> getAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ApiResponse.success(userService.getAddresses(email), "Addresses retrieved successfully"));
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Address deleted successfully"));
    }
}


