package com.infosys.bankingApplication.repositories;

import com.infosys.bankingApplication.models.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}

