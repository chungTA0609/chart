package com.example.chart.email.repository;

import com.example.chart.email.model.EmailMessage;
import com.example.chart.email.model.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, Long> {
    List<EmailMessage> findByStatus(EmailStatus status);
    List<EmailMessage> findByStatusAndCreatedAtBefore(EmailStatus status, LocalDateTime dateTime);
} 