package com.example.chart.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final TemplateEngine templateEngine;

    @Override
    public String processTemplate(String templateId, Map<String, Object> variables) {
        try {
            Context context = new Context();
            variables.forEach(context::setVariable);
            return templateEngine.process(templateId, context);
        } catch (Exception e) {
            log.error("Error processing template: {}", templateId, e);
            throw new RuntimeException("Failed to process email template", e);
        }
    }

    @Override
    public void validateTemplate(String templateId) {
        try {
            templateEngine.process(templateId, new Context());
        } catch (Exception e) {
            log.error("Invalid template: {}", templateId, e);
            throw new RuntimeException("Invalid email template", e);
        }
    }

    @Override
    public String getTemplateContent(String templateId) {
        try {
            return templateEngine.process(templateId, new Context());
        } catch (Exception e) {
            log.error("Error getting template content: {}", templateId, e);
            throw new RuntimeException("Failed to get template content", e);
        }
    }
} 