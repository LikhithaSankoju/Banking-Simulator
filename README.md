# Banking Application

Spring Boot demo for basic banking operations with low-balance email alerts and auditing (DB + file logging).

## Features
- **Banking operations**: Create account, deposit, withdraw, transfer
- **Low-balance email alert** when balance \< ₹500 (single alert per event)
- **Auditing** to DB (`AUDIT_LOG` table) and file (`transactions.txt`)
- **REST APIs** for accounts and audit logs
- **H2 in-memory database** with console enabled

## Tech Stack
- **Java 17**, **Spring Boot**
- **Spring Web**, **Spring Data JPA**, **H2**
- **Jakarta Mail** (email alerts)
- **Thymeleaf** (simple pages), **CSS**
- **SLF4J + Logback** for logging

## Quick Start
In the project root (`bankingApplication`), run:

```bash
mvn spring-boot:run
```

The app runs at `http://localhost:8080`.

## Configuration
Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:bankdb
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true

mail.smtp.host=smtp.gmail.com
mail.smtp.port=587
mail.smtp.username=YOUR_EMAIL@gmail.com
mail.smtp.password=YOUR_APP_PASSWORD   # Use Gmail App Password
```

## API Endpoints

### **Accounts**
- **Create account**
  - `POST /accounts`
  - Body:
    ```json
    {
      "name": "User",
      "email": "user@example.com",
      "balance": 500
    }
    ```
- **Deposit**
  - `POST /accounts/{id}/deposit?amount=100`
- **Withdraw**
  - `POST /accounts/{id}/withdraw?amount=50`

> The transfer logic is implemented as withdraw + deposit in `AccountService`.  
> If you expose a transfer endpoint later, it can call those methods.

### **Audit Logs**
- `GET /audit/logs` – all audit logs
- `GET /audit/logs/account/{accountId}` – logs for a specific account
- `GET /audit/logs/event/{eventType}` – logs by event type  
  (`ACCOUNT_CREATED`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `EMAIL_SENT`, `EMAIL_FAILED`, etc.)

### **Low-Balance Email**
- Triggered when:
  - Account balance drops below **₹500**
  - `lowBalanceAlertSent` flag is **false**
- **Subject**: `⚠ Low Balance Alert`
- **Body** includes:
  - Account holder name
  - Current balance
  - Reminder to deposit funds

## Viewing Data

### **H2 Console**
1. Start the app.
2. Open: `http://localhost:8080/h2-console`
3. JDBC URL: `jdbc:h2:mem:bankdb`
4. Username: `sa`
5. Password: *(leave empty by default)*
6. Example query:
   ```sql
   SELECT * FROM ACCOUNT;
   SELECT * FROM AUDIT_LOG ORDER BY CREATED_AT DESC;
   ```

### **File Log**
- `transactions.txt` (in the project root) contains a simple text audit trail.

## Logging
- Logback config: `src/main/resources/logback-spring.xml`
- Console logs show:
  - **INFO**: normal operations (create, deposit, withdraw, transfer, email success)
  - **WARN**: low balance, insufficient funds
  - **ERROR**: email failures or unexpected exceptions

## Running Tests

```bash
mvn test
```

## Project Structure (key files)

- `src/main/java/com/infosys/bankingApplication/`
  - `BankingApplication.java` – Spring Boot main class
  - `controllers/`
    - `AccountController` – REST endpoints for account operations
    - `AuditController` – REST endpoints for audit logs
    - `DashboardController` – web pages (Thymeleaf)
  - `services/`
    - `AccountService` – core banking logic
    - `BalanceMonitorService` – checks balance and triggers alerts
    - `EmailService` – sends low-balance emails
    - `AuditService` – writes audit logs to DB and file
  - `models/`
    - `Account` – account entity
    - `AuditLog` – audit log entity
  - `repositories/`
    - `AccountRepository` – JPA repository for accounts
    - `AuditLogRepository` – JPA repository for audit logs
  - `loggers/`
    - `TransactionLogger` – legacy file-based logger used by `AuditService`
- `src/main/resources/`
  - `application.properties` – DB and mail configuration
  - `logback-spring.xml` – logging configuration
  - `templates/`
    - `create-account.html`
    - `dashboard.html`
  - `static/css/style.css` – basic styling
- `transactions.txt` – file-based audit trail

## Notes
- Use a **Gmail App Password** for SMTP; regular Gmail password will not work.
- Email sending is synchronous: if sending fails, `EmailService` throws a `RuntimeException`.
- Low-balance alerts are sent **once per event**; the alert flag resets when balance goes back to **≥ ₹500**.

