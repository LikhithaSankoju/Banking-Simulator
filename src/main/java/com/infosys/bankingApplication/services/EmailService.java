package com.infosys.bankingApplication.services;

import com.infosys.bankingApplication.configs.MailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final MailConfig mailConfig;
    private final AuditService auditService;

    public EmailService(MailConfig mailConfig, AuditService auditService) {
        this.mailConfig = mailConfig;
        this.auditService = auditService;
    }

    public void sendLowBalanceAlert(String toEmail, String name, double balance) {
        logger.info("Sending low balance alert email to: {}, name: {}, balance: {}", toEmail, name, balance);

        Session session = Session.getInstance(
                mailConfig.getProperties(),
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                mailConfig.getUsername(),
                                mailConfig.getPassword()
                        );
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getUsername()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("⚠ Low Balance Alert");
            message.setText(
                    "Hello " + name + ",\n\n" +
                            "Your account balance is LOW.\n\n" +
                            "Current Balance: ₹" + balance + "\n\n" +
                            "Please deposit funds.\n\n" +
                            "— Banking System"
            );

            Transport.send(message);
            
            String details = "EMAIL_SENT | TO=" + toEmail + 
                    " | NAME=" + name + 
                    " | BALANCE=" + balance;
            
            // Audit logging
            auditService.audit("EMAIL_SENT", details, null);
            
            logger.info("Low balance email sent successfully to: {}", toEmail);

        } catch (MessagingException e) {
            logger.error("Failed to send email to: {}, error: {}", toEmail, e.getMessage(), e);
            auditService.audit("EMAIL_FAILED", 
                    "EMAIL_FAILED | TO=" + toEmail + " | ERROR=" + e.getMessage(), null);
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
