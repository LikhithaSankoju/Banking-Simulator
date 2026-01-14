package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.loggers.TransactionLogger;
import com.infosys.bankingApplication.models.AuditLog;
import com.infosys.bankingApplication.repositories.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void audit(String eventType, String details, Long accountId) {
        AuditLog audit = new AuditLog(
                eventType,
                details,
                accountId,
                LocalDateTime.now()
        );

        // Persist to database
        repository.save(audit);

        // Also append to existing transactions.txt file for file-based audit trail
        TransactionLogger.log("[" + audit.getCreatedAt() + "] " + eventType + " | " + details);
    }
}

