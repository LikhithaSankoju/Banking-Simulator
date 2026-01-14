package com.infosys.bankingApplication.controllers;

import com.infosys.bankingApplication.models.AuditLog;
import com.infosys.bankingApplication.repositories.AuditLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditLogRepository repository;

    public AuditController(AuditLogRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/logs")
    public List<AuditLog> getAllAuditLogs() {
        return repository.findAll();
    }

    @GetMapping("/logs/account/{accountId}")
    public List<AuditLog> getAuditLogsByAccount(@PathVariable Long accountId) {
        return repository.findAll().stream()
                .filter(log -> log.getAccountId() != null && log.getAccountId().equals(accountId))
                .toList();
    }

    @GetMapping("/logs/event/{eventType}")
    public List<AuditLog> getAuditLogsByEventType(@PathVariable String eventType) {
        return repository.findAll().stream()
                .filter(log -> log.getEventType().equals(eventType))
                .toList();
    }
}
