
# 🚀 VigVam.Manager
Spring Boot Project (Java 21)

### 🔐 Identity & Access Management
* **User & Role Management:** Granular RBAC (Role-Based Access Control) for different staff levels.
* **Authentication:** Secure login and session management.
* **Organization Onboarding:** Seamless process for users to join and manage different organizations.

### 📚 Academic & Operational Management
* **Lesson Lifecycle:** Create lessons with full version history and internal comments.
* **Performance Tracking:** Analyze lesson outcomes and student/tutor effectiveness.
* **Smart Scheduling:** Automated generation and management of the teaching schedule.
* **Notification Engine:** Real-time alerts and notifications for all system users.

### 📊 Financial & Analytics
* **Payroll Reports:** Automated data generation for salary calculation based on lessons and performance.
* **General Reporting:** Business intelligence tools for overall administration.
---

## 🛠 Prerequisites
Before starting, ensure you have the following installed and running:
* **JDK 21** (Amazon Corretto, Liberica, or Oracle)
* **Docker Desktop** (Must be active)
* **IDE** (IntelliJ IDEA is recommended for `.http` file support)

---

## 🏃 Quick Start Guide

### 1. Start Infrastructure (Docker)
Launch the required services (Database, etc.) in the background:
```bash
  docker-compose up -d
```

### 2. Run the Application

Run VigVamManagerApplication or open a terminal in the project root and execute:


**Using Maven:**

```bash
  ./mvnw spring-boot:run

```

---

## ⚡ Testing API Endpoints

All API commands are documented in the `http.http` file located in the project root.

1. Open the **`http.http`** file in your IDE.
2. Look for the **Green Play Icon** (Run button) next to the HTTP methods (GET, POST, etc.).
3. Click the icon to execute the request and view the response in the tool window.

---

## 💡 Important Notes

* **Java 21:** This project is optimized for Java 21. Ensure your `JAVA_HOME` is set correctly.
* **Database Connection:** Configuration details can be found in `src/main/resources/application.properties`.
* **Port Conflicts:** If a container fails to start, ensure the required ports (e.g., 5432, 8082) are not occupied by other services.

