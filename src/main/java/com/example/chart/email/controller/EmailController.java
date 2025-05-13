package com.example.chart.email.controller;

import com.example.chart.email.dto.EmailRequest;
import com.example.chart.email.model.EmailMessage;
import com.example.chart.email.service.EmailService;
import com.example.chart.email.service.EmailTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final EmailTemplateService templateService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
        try {
            // Process template with variables
            String content = templateService.processTemplate(request.getTemplateId(), request.getTemplateVariables());

            // Create email message
            EmailMessage emailMessage = new EmailMessage();
            emailMessage.setReceiver(request.getTo());
            emailMessage.setSubject(request.getSubject());
            emailMessage.setContent(content);
            emailMessage.setTemplateId(request.getTemplateId());

            // Send email
            emailService.sendEmail(emailMessage);

            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email", e);
            return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
        }
    }

    @GetMapping("/status/{emailId}")
    public ResponseEntity<EmailMessage> getEmailStatus(@PathVariable Long emailId) {
        try {
            EmailMessage emailMessage = emailService.getEmailStatus(emailId);
            return ResponseEntity.ok(emailMessage);
        } catch (Exception e) {
            log.error("Failed to get email status", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/template/validate")
    public ResponseEntity<String> validateTemplate(@RequestParam String templateId) {
        try {
            templateService.validateTemplate(templateId);
            return ResponseEntity.ok("Template is valid");
        } catch (Exception e) {
            log.error("Invalid template", e);
            return ResponseEntity.badRequest().body("Invalid template: " + e.getMessage());
        }
    }
} 