package com.example.chart.controllers;

import com.example.chart.config.JwtTokenProvider;
import com.example.chart.core.ApiResponse;
import com.example.chart.dto.ChangePasswordDTO;
import com.example.chart.dto.LoginRequest;
import com.example.chart.dto.PasswordResetConfirmDTO;
import com.example.chart.dto.PasswordResetRequestDTO;
import com.example.chart.dto.UserDTO;
import com.example.chart.models.User;
import com.example.chart.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) {
        User user = authService.registerUser(
                userDTO
        );

        UserDTO response = new UserDTO();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());

        return ResponseEntity.ok(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.login(loginRequest);
        List<String> roles = new ArrayList<>();
        roles.add(authentication.getAuthorities().iterator().next().getAuthority());
        String token = jwtTokenProvider.generateToken(loginRequest.getEmail(), roles);
        return ResponseEntity.ok(ApiResponse.success(token, "Login successful"));
    }
    
    @PostMapping("/password/reset-request")
    public ResponseEntity<ApiResponse<Void>> requestPasswordReset(@Valid @RequestBody PasswordResetRequestDTO requestDTO) {
        authService.requestPasswordReset(requestDTO);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset email sent successfully"));
    }
    
    @PostMapping("/password/reset-confirm")
    public ResponseEntity<ApiResponse<Void>> confirmPasswordReset(@Valid @RequestBody PasswordResetConfirmDTO confirmDTO) {
        authService.confirmPasswordReset(confirmDTO);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successful"));
    }
    
    @PostMapping("/password/change")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(ApiResponse.success(null, "Password changed successfully"));
    }
}
