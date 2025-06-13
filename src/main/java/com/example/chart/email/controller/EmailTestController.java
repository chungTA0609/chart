package com.example.chart.email.controller;

import com.example.chart.core.ApiResponse;
import com.example.chart.email.dto.EmailTestRequestDTO;
import com.example.chart.email.model.EmailMessage;
import com.example.chart.email.model.EmailStatus;
import com.example.chart.email.service.EmailService;
import com.example.chart.email.service.EmailTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/email/test")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> testSendEmail(@Valid @RequestBody EmailTestRequestDTO requestDTO) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setReceiver(requestDTO.getTo());
        emailMessage.setSubject(requestDTO.getSubject());
        
        // Generate content based on template type
        String content;
        switch (requestDTO.getTemplateType()) {
            case "welcome":
                content = emailTemplateService.getWelcomeTemplate(requestDTO.getRecipientName());
                break;
            case "order-confirmation":
                content = emailTemplateService.getOrderConfirmationTemplate(
                        requestDTO.getRecipientName(),
                        requestDTO.getOrderNumber(),
                        requestDTO.getOrderDetails());
                break;
            case "shipping-update":
                content = emailTemplateService.getShippingUpdateTemplate(
                        requestDTO.getRecipientName(),
                        requestDTO.getOrderNumber(),
                        requestDTO.getTrackingNumber(),
                        requestDTO.getShippingStatus());
                break;
            case "password-reset":
                content = emailTemplateService.getPasswordResetTemplate(
                        requestDTO.getRecipientName(),
                        requestDTO.getResetLink());
                break;
            case "custom":
                content = requestDTO.getCustomContent();
                break;
            case "email-test":
                content = content = emailTemplateService.getShippingUpdateTemplate(
                        requestDTO.getRecipientName(),
                        requestDTO.getOrderNumber(),
                        requestDTO.getTrackingNumber(),
                        requestDTO.getShippingStatus());
                break;
            default:
                return ResponseEntity.badRequest().body(
                        ApiResponse.error("Invalid template type. Supported types: welcome, order-confirmation, shipping-update, password-reset, custom", null));
        }
        
        emailMessage.setContent(content);
        emailMessage.setStatus(EmailStatus.PENDING);
        
        try {
            emailService.sendEmail(emailMessage);
            return ResponseEntity.ok(ApiResponse.success(
                    emailMessage.getId().toString(), 
                    "Email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.error("Failed to send email: ", Collections.singletonList(e.getMessage())));
        }
    }
    
    @GetMapping("/templates")
    public ResponseEntity<ApiResponse<String[]>> getAvailableTemplates() {
        String[] templates = {"welcome", "order-confirmation", "shipping-update", "password-reset", "custom"};
        return ResponseEntity.ok(ApiResponse.success(templates, "Available email templates"));
    }
} 