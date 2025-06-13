package com.example.chart.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailTestRequestDTO {
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    private String to;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    @NotBlank(message = "Template type is required")
    private String templateType;
    
    private String recipientName;
    
    // For order confirmation template
    private String orderNumber;
    private String orderDetails;
    
    // For shipping update template
    private String trackingNumber;
    private String shippingStatus;
    
    // For password reset template
    private String resetLink;
    
    // For custom template
    private String customContent;
} 