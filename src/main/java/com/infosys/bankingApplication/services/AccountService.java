package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.exceptions.InsufficientFundsException;
import com.infosys.bankingApplication.loggers.TransactionLogger;
import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountRepository repository;
    private final BalanceMonitorService monitor;
    private final AuditService auditService;

    public AccountService(AccountRepository repository,
                          BalanceMonitorService monitor,
                          AuditService auditService) {
        this.repository = repository;
        this.monitor = monitor;
        this.auditService = auditService;
    }

    public Account createAccount(String name, String email, double balance) {
        logger.info("Creating account: name={}, email={}, balance={}", name, email, balance);
        
        Account acc = repository.save(new Account(name, email, balance));

        String details = "ACCOUNT CREATED | ID=" + acc.getId() +
                " | NAME=" + name +
                " | BALANCE=" + balance;
        
        // Audit logging
        auditService.audit("ACCOUNT_CREATED", details, acc.getId());
        
        // File logging (legacy)
        TransactionLogger.log(details);
        
        logger.info("Account created successfully: id={}, name={}", acc.getId(), name);

        return acc;
    }

    public void deposit(Long id, double amount) {
        logger.info("Deposit request: accountId={}, amount={}", id, amount);
        
        Account acc = repository.findById(id).orElseThrow();

        acc.setBalance(acc.getBalance() + amount);
        repository.save(acc);

        String details = "DEPOSIT | ACCOUNT=" + id +
                " | AMOUNT=" + amount +
                " | NEW_BALANCE=" + acc.getBalance();
        
        // Audit logging
        auditService.audit("DEPOSIT", details, id);
        
        // File logging (legacy)
        TransactionLogger.log(details);
        
        logger.info("Deposit completed: accountId={}, amount={}, newBalance={}", id, amount, acc.getBalance());

        monitor.check(acc);
    }

    public void withdraw(Long id, double amount) {
        logger.info("Withdraw request: accountId={}, amount={}", id, amount);
        
        Account acc = repository.findById(id).orElseThrow();

        if (acc.getBalance() < amount) {
            logger.warn("Insufficient funds: accountId={}, balance={}, requested={}", 
                    id, acc.getBalance(), amount);
            throw new InsufficientFundsException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        repository.save(acc);

        String details = "WITHDRAW | ACCOUNT=" + id +
                " | AMOUNT=" + amount +
                " | NEW_BALANCE=" + acc.getBalance();
        
        // Audit logging
        auditService.audit("WITHDRAW", details, id);
        
        // File logging (legacy)
        TransactionLogger.log(details);
        
        logger.info("Withdraw completed: accountId={}, amount={}, newBalance={}", id, amount, acc.getBalance());

        monitor.check(acc);
    }

    public void transfer(Long fromId, Long toId, double amount) {
        logger.info("Transfer request: fromId={}, toId={}, amount={}", fromId, toId, amount);
        
        withdraw(fromId, amount);
        deposit(toId, amount);

        String details = "TRANSFER | FROM=" + fromId +
                " | TO=" + toId +
                " | AMOUNT=" + amount;
        
        // Audit logging
        auditService.audit("TRANSFER", details, fromId);
        
        // File logging (legacy)
        TransactionLogger.log(details);
        
        logger.info("Transfer completed: fromId={}, toId={}, amount={}", fromId, toId, amount);
    }
}
