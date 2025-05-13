package com.example.chart.email.service;

import java.util.Map;

public interface EmailTemplateService {
    String processTemplate(String templateId, Map<String, Object> variables);
    void validateTemplate(String templateId);
    String getTemplateContent(String templateId);
} 