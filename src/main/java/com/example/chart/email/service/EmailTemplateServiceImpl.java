package com.example.chart.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final TemplateEngine templateEngine;
    private static final String TEMPLATE_PREFIX = "email/";

    @Override
    public String processTemplate(String templateId, Map<String, Object> variables) {
        try {
            Context context = new Context();
            variables.forEach(context::setVariable);
            String fullTemplatePath = TEMPLATE_PREFIX + templateId;
            return templateEngine.process(fullTemplatePath, context);
        } catch (Exception e) {
            log.error("Error processing template: {}", templateId, e);
            throw new RuntimeException("Failed to process email template: " + e.getMessage(), e);
        }
    }

    @Override
    public void validateTemplate(String templateId) {
        try {
            String fullTemplatePath = TEMPLATE_PREFIX + templateId;
            log.debug("Validating template: {}", fullTemplatePath);
            templateEngine.process(fullTemplatePath, new Context());
        } catch (Exception e) {
            log.error("Invalid template: {}", templateId, e);
            throw new RuntimeException("Invalid email template: " + e.getMessage(), e);
        }
    }

    @Override
    public String getTemplateContent(String templateId) {
        try {
            String fullTemplatePath = TEMPLATE_PREFIX + templateId;
            log.debug("Getting template content: {}", fullTemplatePath);
            return templateEngine.process(fullTemplatePath, new Context());
        } catch (Exception e) {
            log.error("Error getting template content: {}", templateId, e);
            throw new RuntimeException("Failed to get template content: " + e.getMessage(), e);
        }
    }

    @Override
    public String getOrderConfirmationTemplate(String customerName, String orderNumber, String orderDetails) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("orderNumber", orderNumber);
        variables.put("orderDetails", orderDetails);
        variables.put("title", "Order Confirmation");
        variables.put("header", "Order Confirmation");
        return processTemplate("order-confirmation", variables);
    }

    @Override
    public String getWelcomeTemplate(String customerName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("title", "Welcome to Chart");
        variables.put("header", "Welcome!");
        return processTemplate("welcome", variables);
    }

    @Override
    public String getShippingUpdateTemplate(String customerName, String orderNumber, String trackingNumber, String status) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("orderNumber", orderNumber);
        variables.put("trackingNumber", trackingNumber);
        variables.put("status", status);
        variables.put("title", "Shipping Update");
        variables.put("header", "Shipping Update");
        return processTemplate("shipping-update", variables);
    }
    
    @Override
    public String getPasswordResetTemplate(String customerName, String resetLink) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerName);
        variables.put("resetLink", resetLink);
        variables.put("title", "Password Reset");
        variables.put("header", "Password Reset");
        variables.put("expiryTime", "1 hour");
        return processTemplate("password-reset", variables);
    }
} 