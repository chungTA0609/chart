package com.example.chart.controllers;

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
    public ResponseEntity<UserDTO> getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getUserProfile(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateProfile(@RequestBody UserDTO userDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.updateUserProfile(email, userDTO));
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody String newPassword) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changePassword(email, newPassword);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.addAddress(email, addressDTO));
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getAddresses(email));
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(id);
        return ResponseEntity.ok("Address deleted successfully");
    }
}


