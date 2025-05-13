package com.example.chart.email.service;

import com.example.chart.email.model.EmailMessage;
import com.example.chart.email.model.EmailStatus;
import com.example.chart.email.repository.EmailMessageRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailMessageRepository emailMessageRepository;
    private final EmailTemplateService templateService;

    @Override
    @Transactional
    public void sendEmail(EmailMessage emailMessage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(emailMessage.getReceiver ());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getContent(), true);

            mailSender.send(message);
            
            emailMessage.setStatus(EmailStatus.SENT);
            emailMessage.setSentAt(LocalDateTime.now());
            emailMessageRepository.save(emailMessage);
            
            log.info("Email sent successfully to: {}", emailMessage.getReceiver());
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", emailMessage.getReceiver(), e);
            handleFailedEmail(emailMessage);
        }
    }

    @Override
    @Transactional
    public void processEmailQueue() {
        emailMessageRepository.findByStatus(EmailStatus.PENDING)
                .forEach(this::sendEmail);
    }

    @Override
    @Transactional
    public void handleFailedEmail(EmailMessage emailMessage) {
        emailMessage.setStatus(EmailStatus.FAILED);
        emailMessage.setErrorMessage("Failed to send email");
        emailMessageRepository.save(emailMessage);
    }

    @Override
    @Transactional
    public void updateEmailStatus(EmailMessage emailMessage) {
        emailMessageRepository.save(emailMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public EmailMessage getEmailStatus(Long emailId) {
        return emailMessageRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found with id: " + emailId));
    }
} 