package com.infosys.bankingApplication.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;      // e.g. ACCOUNT_CREATED, DEPOSIT, WITHDRAW, TRANSFER, EMAIL_SENT
    private String details;        // human-readable description
    private Long accountId;        // nullable for events not tied to an account
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public AuditLog(String eventType, String details, Long accountId, LocalDateTime createdAt) {
        this.eventType = eventType;
        this.details = details;
        this.accountId = accountId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

