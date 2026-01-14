# Audit and Logging Guide

## ✅ What's Implemented

### 1. **Console Logging (SLF4J)**
- All services now log to console with timestamps
- Logs show: INFO, WARN, ERROR levels
- Visible in your application console output

### 2. **Database Auditing**
- All transactions are saved to `AUDIT_LOG` table in H2 database
- Includes: event type, details, account ID, timestamp

### 3. **File Logging**
- Still writes to `transactions.txt` (legacy support)
- Now includes timestamps in format: `[2024-01-01T10:30:00] EVENT_TYPE | details`

---

## 📊 How to View Logs

### **Console Logs** (Real-time)
When you run the application, you'll see logs like:

```
2024-01-15 10:30:45 - Creating account: name=Likhitha, email=likhitha@example.com, balance=300.0
2024-01-15 10:30:45 - Account created successfully: id=1, name=Likhitha
2024-01-15 10:30:46 - Deposit request: accountId=1, amount=100.0
2024-01-15 10:30:46 - Deposit completed: accountId=1, amount=100.0, newBalance=400.0
2024-01-15 10:30:47 - Low balance detected: accountId=1, balance=400.0, threshold=500.0
2024-01-15 10:30:47 - Sending low balance alert email to: likhitha@example.com
2024-01-15 10:30:48 - Low balance email sent successfully to: likhitha@example.com
```

### **Database Audit Logs** (H2 Console)

1. **Start your application**
2. **Open H2 Console**: http://localhost:8080/h2-console
3. **JDBC URL**: `jdbc:h2:mem:bankdb`
4. **Username**: `sa`
5. **Password**: (leave empty)
6. **Run query**:
   ```sql
   SELECT * FROM AUDIT_LOG ORDER BY CREATED_AT DESC;
   ```

### **API Endpoints** (View via REST API)

```bash
# Get all audit logs
GET http://localhost:8080/audit/logs

# Get logs for specific account
GET http://localhost:8080/audit/logs/account/1

# Get logs by event type
GET http://localhost:8080/audit/logs/event/ACCOUNT_CREATED
GET http://localhost:8080/audit/logs/event/DEPOSIT
GET http://localhost:8080/audit/logs/event/WITHDRAW
GET http://localhost:8080/audit/logs/event/TRANSFER
GET http://localhost:8080/audit/logs/event/EMAIL_SENT
```

### **File Logs** (transactions.txt)

Check the `transactions.txt` file in your project root:
```
[2024-01-15T10:30:45] ACCOUNT_CREATED | ACCOUNT CREATED | ID=1 | NAME=Likhitha | BALANCE=300.0
[2024-01-15T10:30:46] DEPOSIT | DEPOSIT | ACCOUNT=1 | AMOUNT=100.0 | NEW_BALANCE=400.0
[2024-01-15T10:30:47] EMAIL_SENT | EMAIL_SENT | TO=likhitha@example.com | NAME=Likhitha | BALANCE=400.0
```

---

## 🔍 What Gets Logged

### **Account Operations**
- ✅ Account creation (with name, email, balance)
- ✅ Deposits (account ID, amount, new balance)
- ✅ Withdrawals (account ID, amount, new balance)
- ✅ Transfers (from account, to account, amount)

### **Email Operations**
- ✅ Email sent successfully
- ✅ Email failed (with error details)

### **Balance Monitoring**
- ✅ Low balance detection
- ✅ Balance recovery above threshold

### **Errors**
- ✅ Insufficient funds warnings
- ✅ Email sending failures

---

## 🧪 Test It

1. **Start application**:
   ```bash
   mvn spring-boot:run
   ```

2. **Create an account** (watch console for logs):
   ```bash
   POST http://localhost:8080/accounts
   {
     "name": "Likhitha",
     "email": "likhitha@example.com",
     "balance": 300
   }
   ```

3. **Make a transaction**:
   ```bash
   POST http://localhost:8080/accounts/1/deposit?amount=100
   ```

4. **View audit logs**:
   ```bash
   GET http://localhost:8080/audit/logs
   ```

5. **Check console** - You should see detailed logs!

---

## 📝 Log Levels

- **INFO**: Normal operations (account created, deposit completed, etc.)
- **WARN**: Low balance alerts, insufficient funds
- **ERROR**: Email failures, exceptions
- **DEBUG**: Detailed balance checks (can be enabled for more detail)

---

## 🎯 Quick Reference

| What | Where to See |
|------|-------------|
| Real-time logs | Console output |
| Historical audit trail | H2 Database (`AUDIT_LOG` table) |
| API access | `/audit/logs` endpoints |
| File backup | `transactions.txt` |

---

## 💡 Tips

- **Console logs** are best for real-time debugging
- **Database audit logs** are best for historical queries and reporting
- **File logs** provide a simple text backup
- Use H2 console to query audit logs with SQL for advanced filtering
