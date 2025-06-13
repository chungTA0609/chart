package com.example.chart.email.service;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface EmailTemplateService {
    String processTemplate(String templateId, Map<String, Object> variables);
    void validateTemplate(String templateId);
    String getTemplateContent(String templateId);
    String getOrderConfirmationTemplate(String customerName, String orderNumber, String orderDetails);
    String getWelcomeTemplate(String customerName);
    String getShippingUpdateTemplate(String customerName, String orderNumber, String trackingNumber, String status);
    String getPasswordResetTemplate(String customerName, String resetLink);
} 