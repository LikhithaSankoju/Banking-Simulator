# 📘 Agile Execution Document
**Project:** Banking Application  
**Methodology:** Agile (Sprint-based execution)

---

## 🗓 Sprint 1 – Project Setup & Core Design
**Duration:** 05-Jan-2026 to 07-Jan-2026  

### 📅 05-Jan-2026
**Planned:**
- Understand problem statement
- Define project scope
- Decide technology stack

**Executed:**
- Finalized banking use cases
- Selected Spring Boot, Java 17, H2, Thymeleaf
- Created initial project structure

**Owner:** Likhitha  
**Status:** Completed ✅

---

### 📅 06-Jan-2026
**Planned:**
- Create Account entity
- Configure database & JPA

**Executed:**
- Implemented Account entity
- Configured H2 database
- Verified table creation

**Owner:** Likhitha  
**Status:** Completed ✅

---

### 📅 07-Jan-2026
**Planned:**
- Implement deposit & withdraw functionality

**Executed:**
- Added deposit logic
- Added withdraw logic with balance validation
- Basic service layer testing

**Owner:** Likhitha  
**Status:** Completed ✅

---

## 🗓 Sprint 2 – Alerts, Auditing & Logging
**Duration:** 08-Jan-2026 to 10-Jan-2026  

### 📅 08-Jan-2026
**Planned:**
- Implement low-balance alert logic

**Executed:**
- Added balance threshold check (₹500)
- Implemented alert flag to avoid duplicate emails

**Owner:** Likhitha  
**Status:** Completed ✅

---

### 📅 09-Jan-2026
**Planned:**
- Add email notifications

**Executed:**
- Configured Jakarta Mail
- Implemented Gmail SMTP email alerts
- Tested email triggering logic

**Owner:** Likhitha  
**Status:** Completed ✅

---

### 📅 10-Jan-2026
**Planned:**
- Implement auditing and logging

**Executed:**
- Created AUDIT_LOG table
- Logged all transactions in DB
- Implemented file logging in transactions.txt

**Owner:** Likhitha  
**Status:** Completed ✅

---

## 🗓 Sprint 3 – UI, APIs & Testing
**Duration:** 11-Jan-2026 to 13-Jan-2026  

### 📅 11-Jan-2026
**Planned:**
- Develop REST APIs

**Executed:**
- Created account, deposit, withdraw, transfer APIs
- Tested APIs using Postman

**Owner:** Likhitha  
**Status:** Completed ✅

---

### 📅 12-Jan-2026
**Planned:**
- Develop UI using Thymeleaf

**Executed:**
- Created dashboard page
- Added account details & audit log views

**Owner:** Likhitha  
**Status:** Completed ✅

---

### 📅 13-Jan-2026
**Planned:**
- Testing & documentation

**Executed:**
- Wrote unit tests
- Created README.md
- Final code cleanup

**Owner:** Likhitha  
**Status:** Completed ✅

---

## ✅ Final Outcome
- Core banking operations implemented
- Low-balance alert system working
- Full auditing & logging implemented
- Project ready for demonstration & interviews
