package com.example.chart.email.service;

import com.example.chart.email.model.EmailMessage;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
    void processEmailQueue();
    void handleFailedEmail(EmailMessage emailMessage);
    void updateEmailStatus(EmailMessage emailMessage);
    EmailMessage getEmailStatus(Long emailId);
} 