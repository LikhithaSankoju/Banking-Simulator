package com.infosys.bankingApplication.services;


import com.infosys.bankingApplication.models.Account;
import com.infosys.bankingApplication.repositories.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BalanceMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(BalanceMonitorService.class);
    private static final double THRESHOLD = 500;

    private final EmailService emailService;
    private final AccountRepository repository;

    public BalanceMonitorService(EmailService emailService,
                                 AccountRepository repository) {
        this.emailService = emailService;
        this.repository = repository;
    }

    public void check(Account account) {
        logger.debug("Checking balance for account: id={}, balance={}", account.getId(), account.getBalance());

        if (account.getBalance() < THRESHOLD &&
                !account.isLowBalanceAlertSent()) {
            
            logger.warn("Low balance detected: accountId={}, balance={}, threshold={}", 
                    account.getId(), account.getBalance(), THRESHOLD);

            emailService.sendLowBalanceAlert(
                    account.getEmail(),
                    account.getName(),
                    account.getBalance()
            );

            account.setLowBalanceAlertSent(true);
            repository.save(account);
            
            logger.info("Low balance alert sent and flag set for account: id={}", account.getId());
        }

        if (account.getBalance() >= THRESHOLD) {
            if (account.isLowBalanceAlertSent()) {
                logger.info("Balance recovered above threshold: accountId={}, balance={}", 
                        account.getId(), account.getBalance());
            }
            account.setLowBalanceAlertSent(false);
            repository.save(account);
        }
    }
}
