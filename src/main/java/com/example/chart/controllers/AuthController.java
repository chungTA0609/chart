package com.example.chart.controllers;

import com.example.chart.config.JwtTokenProvider;
import com.example.chart.dto.LoginRequest;
import com.example.chart.dto.UserDTO;
import com.example.chart.models.User;
import com.example.chart.services.AuthService;
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
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        User user = authService.registerUser(
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName()
        );

        UserDTO response = new UserDTO();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.login(loginRequest);
        List<String> roles = new ArrayList<>();
        roles.add(authentication.getAuthorities().iterator().next().getAuthority());
        String token = jwtTokenProvider.generateToken(authentication.getAuthorities().iterator().next().getAuthority(), roles);
        return ResponseEntity.ok(token);
    }
}
