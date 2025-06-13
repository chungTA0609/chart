package com.example.chart.services;

import com.example.chart.dto.ChangePasswordDTO;
import com.example.chart.dto.LoginRequest;
import com.example.chart.dto.PasswordResetConfirmDTO;
import com.example.chart.dto.PasswordResetRequestDTO;
import com.example.chart.dto.UserDTO;
import com.example.chart.email.model.EmailMessage;
import com.example.chart.email.model.EmailStatus;
import com.example.chart.email.service.EmailService;
import com.example.chart.email.service.EmailTemplateService;
import com.example.chart.models.PasswordResetToken;
import com.example.chart.models.Role;
import com.example.chart.models.RoleName;
import com.example.chart.models.User;
import com.example.chart.repository.PasswordResetTokenRepository;
import com.example.chart.repository.RoleRepository;
import com.example.chart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    
    @Value("${app.frontend.url:https://crochet-shop.vercel.app")
    private String frontendUrl;
    
    @Value("${app.password-reset.expiration:3600}")
    private int passwordResetExpirationSeconds;
    
    @Autowired
    private final AuthenticationManager authenticationManager;

    public User registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        Role customerRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Customer role not found"));

        user.setRoles(new HashSet<>() {{ add(customerRole); }});
        return userRepository.save(user);
    }

    public Authentication login(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
    }
    
    @Transactional
    public void requestPasswordReset(PasswordResetRequestDTO requestDTO) {
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + requestDTO.getEmail()));
        
        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);
        
        // Create new token
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusSeconds(passwordResetExpirationSeconds));
        passwordResetTokenRepository.save(passwordResetToken);
        
        // Send email with reset link
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        String emailContent = emailTemplateService.getPasswordResetTemplate(user.getFirstName(), resetLink);
        
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setReceiver(user.getEmail());
        emailMessage.setSubject("Password Reset Request");
        emailMessage.setContent(emailContent);
        emailMessage.setStatus(EmailStatus.PENDING);
        
        emailService.sendEmail(emailMessage);
    }
    
    @Transactional
    public void confirmPasswordReset(PasswordResetConfirmDTO confirmDTO) {
        if (!confirmDTO.getPassword().equals(confirmDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(confirmDTO.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));
        
        if (passwordResetToken.isExpired()) {
            passwordResetTokenRepository.delete(passwordResetToken);
            throw new RuntimeException("Password reset token has expired");
        }
        
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(confirmDTO.getPassword()));
        userRepository.save(user);
        
        passwordResetTokenRepository.delete(passwordResetToken);
    }
    
    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify current password
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        
        // Verify password confirmation
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
    }
}
