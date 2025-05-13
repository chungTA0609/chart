package com.example.chart.email.consumer;

import com.example.chart.email.model.EmailMessage;
import com.example.chart.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMessageConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "${spring.kafka.topic.email}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(EmailMessage emailMessage) {
        try {
            log.info("Received email message for processing: {}", emailMessage.getId());
            emailService.sendEmail(emailMessage);
        } catch (Exception e) {
            log.error("Error processing email message: {}", emailMessage.getId(), e);
            emailService.handleFailedEmail(emailMessage);
        }
    }
} 