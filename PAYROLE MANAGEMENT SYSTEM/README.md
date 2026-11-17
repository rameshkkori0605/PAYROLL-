# 🏢 Full-Stack Payroll Management System

A complete, production-grade, web-based **Payroll Management System** engineered with **Core Java (Servlets, JDBC)**, **MySQL**, and a modern **HTML5 / CSS3 / Vanilla JavaScript (Fetch API)** user interface.

Built following an enterprise **three-tier architecture** with strict Object-Oriented Programming (OOP) principles, thread-safe database connections, atomic ACID transactions, and session-based authentication.

---

## 📋 Table of Contents
1. [Project Overview](#-project-overview)
2. [Technology Stack](#-technology-stack)
3. [Architecture Overview](#-architecture-overview)
4. [Four Core OOP Concepts Explained](#-four-core-oop-concepts-explained)
5. [Project Directory Structure](#-project-directory-structure)
6. [Database Schema & Setup](#-database-schema--setup)
7. [Configuring Database Credentials](#-configuring-database-credentials)
8. [Running with Apache Tomcat & IDEs](#-running-with-apache-tomcat--ides)
   - [Method 1: Eclipse IDE (Recommended for College Students)](#method-1-eclipse-ide-recommended)
   - [Method 2: IntelliJ IDEA](#method-2-intellij-idea)
   - [Method 3: Maven Command Line & Tomcat Webapps](#method-3-maven-cli--standalone-tomcat)
9. [Sample Credentials for Demonstration](#-sample-credentials-for-demonstration)
10. [REST API Endpoints Reference](#-rest-api-endpoints-reference)
11. [Module Walkthrough & College Viva Checklist](#-module-walkthrough--college-viva-checklist)

---

## 🎯 Project Overview

The **Payroll Management System** automates the entire compensation cycle for modern enterprises:
- **Employee Management**: Full CRUD operations, automatic sequential ID generation (`EMP1001`...), department assignments, salary structures, and masked bank credentials.
- **Attendance Tracking**: Real-time recording of `Present`, `Half Day`, `Leave`, and `Absent`. Prevents duplicate records for the same employee and date.
- **Salary Calculation Engine**: Automatically computes Gross Salary, HRA, DA, Allowances, PF, Professional Tax, TDS Income Tax, and Unpaid Leave Deductions using `BigDecimal` for zero precision loss.
- **Payslip Generation**: Generates official corporate payslips with complete salary breakdowns, formatted numbers, net salary in words, and one-click PDF printing (`@media print`).
- **Disbursement & Payment Tracking**: Records transaction reference numbers, payment modes (NEFT, UPI, Cheque, Cash), and executes atomic database updates.
- **Executive Dashboard & Reports**: Dynamic statistics, staffing breakdown, attendance pulse, monthly payroll trends, and instant CSV export.

---

## 🛠 Technology Stack

### Presentation Layer (Frontend)
- **HTML5**: Semantic web architecture.
- **CSS3**: Custom design system featuring dark navy (`#0f172a`), royal blue (`#2563eb`), card elevation shadows, responsive grid layouts, and print media queries.
- **Vanilla JavaScript (ES6+)**: Zero framework bloat (no React/Angular/Vue).
- **Fetch API**: Clean asynchronous JSON communication with Java Servlets.

### Business & Persistence Layer (Backend)
- **Java 8+ / Core Java**: Pure object-oriented domain models and service logic.
- **Java Servlets (`javax.servlet.http.HttpServlet`)**: RESTful endpoint controllers.
- **JDBC (`java.sql.*`)**: High-performance parameterized queries with `PreparedStatement` to prevent SQL Injection.
- **Google Gson**: High-speed JSON serialization with custom `BigDecimal` and `LocalDate` adapters.
- **Apache Tomcat (8.5 / 9 / 10)**: Enterprise servlet container.

### Data Layer (Database)
- **MySQL (5.7 / 8.x)**: Relational database with foreign key constraints, indexes, and transactional integrity.

---

## 🏛 Architecture Overview

The system strictly adheres to the standard **Three-Tier Architecture**:

```
[ Frontend: HTML5 / CSS3 / Vanilla JS Fetch API ]
                       │
                  JSON Requests
                       ▼
[ Presentation / Controller Layer: Java Servlets & AuthFilter ]
                       │
              Service Invocations
                       ▼
[ Business Logic Layer: Service Classes & StandardPayrollCalculator ]
                       │
              DAO Operations (JDBC)
                       ▼
[ Data Access Layer: DAO Interfaces & PreparedStatement Implementations ]
                       │
                  ACID SQL
                       ▼
[ Relational Storage: MySQL Database (`payroll_management`) ]
```

---

## 🧬 Four Core OOP Concepts Explained

This project is an exemplary showcase of Object-Oriented Programming (OOP) in Java:

### 1. Encapsulation
- **What it is**: Bundling data (fields) and methods that operate on that data inside private variables, accessible only through public getters and setters.
- **Where in code**: 
  - In `com.payroll.model.Employee`, all sensitive fields (`basicSalary`, `bankAccountNo`, `email`) are marked `private`.
  - The model provides encapsulation methods like `getMaskedBankAccount()` which hides account digits (`*******1102`) from unauthorized visibility.
  - Server-side validations in `EmployeeServiceImpl.validateEmployee()` ensure invariant data integrity before persisting.

### 2. Inheritance
- **What it is**: Enabling child classes to inherit attributes and methods from a common parent class to promote reusability and avoid duplicate code.
- **Where in code**:
  - `com.payroll.model.Person` is an `abstract` base class containing common human identity attributes (`firstName`, `lastName`, `email`, `phone`, `getFullName()`).
  - `com.payroll.model.Employee extends Person`: Inherits personal identity and adds employment attributes (`employeeId`, `departmentId`, `designation`, `basicSalary`).
  - `com.payroll.model.Admin extends Person`: Inherits personal identity and adds administrative credentials (`adminId`, `username`, `role`).

### 3. Polymorphism
- **What it is**: The ability of an object or method to take many forms through **Method Overriding** (runtime) and **Method Overloading** (compile-time).
- **Where in code**:
  - **Method Overriding**: `Person` defines `public abstract String getRoleDescription()`. Both `Employee` and `Admin` override this method to provide their own distinct role descriptions:
    ```java
    // In Employee.java
    @Override
    public String getRoleDescription() {
        return designation + " (" + departmentName + ")";
    }

    // In Admin.java
    @Override
    public String getRoleDescription() {
        return role + " System Administrator";
    }
    ```
  - **Interface Polymorphism**: `PayrollCalculator` defines the contract for salary calculations. `StandardPayrollCalculator` implements this interface using configurable percentage rules and `BigDecimal` rounding.
  - **Method Overloading**: In `com.payroll.service.EmployeeService`, multiple versions of `getEmployees` exist with differing parameters:
    - `getEmployees(String searchQuery)`
    - `getEmployees(Integer departmentId, String status)`
    - `getEmployees(String searchQuery, Integer departmentId, String status)`

### 4. Abstraction
- **What it is**: Hiding underlying implementation complexity and exposing only essential interfaces to callers.
- **Where in code**:
  - **DAO Pattern**: Controllers and Services depend strictly on DAO interfaces (`EmployeeDAO`, `PayrollDAO`, `AttendanceDAO`, `PaymentDAO`), completely decoupling database SQL queries from application business logic.
  - **Service Interfaces**: Controller servlets interact only with service interfaces (`PayrollService`, `AttendanceService`), allowing implementations to change without breaking endpoint contracts.
  - **Connection Abstraction**: `DBConnection.getConnection()` encapsulates driver registration, connection pooling, and configuration properties.

---

## 📂 Project Directory Structure

```
PayrollManagementSystem/
│
├── database/
│   └── payroll_management.sql          <-- Complete MySQL database schema & sample records
│
├── frontend/                            <-- Clean Frontend distribution
│   ├── index.html                      <-- Administrator login page
│   ├── dashboard.html                  <-- Executive statistics & analytics dashboard
│   ├── employees.html                  <-- Employee management CRUD
│   ├── attendance.html                 <-- Daily attendance tracking
│   ├── payroll.html                    <-- Live payroll processing & calculations
│   ├── payslip.html                    <-- Official corporate payslip view & print
│   ├── payments.html                   <-- Payment disbursements & history
│   ├── reports.html                    <-- Audit reports & CSV export
│   ├── css/
│   │   └── style.css                   <-- Master responsive stylesheet
│   └── js/
│       └── script.js                   <-- Modular Vanilla JS Fetch API client
│
├── backend/
│   ├── pom.xml                         <-- Maven configuration for WAR deployment
│   └── src/
│       └── main/
│           ├── java/com/payroll/
│           │   ├── controller/         <-- Java Servlets & Security Filters
│           │   │   ├── AuthFilter.java
│           │   │   ├── LoginServlet.java
│           │   │   ├── EmployeeServlet.java
│           │   │   ├── AttendanceServlet.java
│           │   │   ├── PayrollServlet.java
│           │   │   ├── PayslipServlet.java
│           │   │   ├── PaymentServlet.java
│           │   │   ├── DashboardServlet.java
│           │   │   ├── DepartmentServlet.java
│           │   │   └── ReportServlet.java
│           │   │
│           │   ├── model/              <-- OOP Domain Entities
│           │   │   ├── Person.java (Abstract parent)
│           │   │   ├── Employee.java (extends Person)
│           │   │   ├── Admin.java (extends Person)
│           │   │   ├── Attendance.java
│           │   │   ├── Department.java
│           │   │   ├── SalaryStructure.java
│           │   │   ├── Payroll.java
│           │   │   ├── Payment.java
│           │   │   └── CompanySetting.java
│           │   │
│           │   ├── dao/                <-- Data Access Layer (Interfaces & JDBC Impls)
│           │   │   ├── EmployeeDAO.java
│           │   │   ├── AttendanceDAO.java
│           │   │   ├── PayrollDAO.java
│           │   │   ├── PaymentDAO.java
│           │   │   ├── AdminDAO.java
│           │   │   ├── DepartmentDAO.java
│           │   │   ├── DashboardDAO.java
│           │   │   ├── CompanySettingDAO.java
│           │   │   └── impl/
│           │   │       ├── EmployeeDAOImpl.java
│           │   │       ├── AttendanceDAOImpl.java
│           │   │       ├── PayrollDAOImpl.java
│           │   │       ├── PaymentDAOImpl.java
│           │   │       ├── AdminDAOImpl.java
│           │   │       ├── DepartmentDAOImpl.java
│           │   │       ├── DashboardDAOImpl.java
│           │   │       └── CompanySettingDAOImpl.java
│           │   │
│           │   ├── service/            <-- Business Logic Layer & Calculator
│           │   │   ├── AuthService.java
│           │   │   ├── EmployeeService.java
│           │   │   ├── AttendanceService.java
│           │   │   ├── PayrollService.java
│           │   │   ├── PaymentService.java
│           │   │   ├── DashboardService.java
│           │   │   ├── PayrollCalculator.java (Interface)
│           │   │   └── impl/
│           │   │       ├── AuthServiceImpl.java
│           │   │       ├── EmployeeServiceImpl.java
│           │   │       ├── AttendanceServiceImpl.java
│           │   │       ├── PayrollServiceImpl.java
│           │   │       ├── PaymentServiceImpl.java
│           │   │       ├── DashboardServiceImpl.java
│           │   │       └── StandardPayrollCalculator.java
│           │   │
│           │   └── util/               <-- Utilities
│           │       ├── DBConnection.java
│           │       ├── PasswordUtil.java (SHA-256 with Salt)
│           │       └── JsonUtil.java (Gson + Response Envelopes)
│           │
│           ├── resources/
│           │   └── db.properties       <-- Database connection parameters
│           │
│           └── webapp/                 <-- Ready for Tomcat container deployment
│               ├── WEB-INF/
│               │   └── web.xml         <-- Servlet container deployment descriptor
│               ├── index.html
│               ├── dashboard.html
│               ├── employees.html
│               ├── attendance.html
│               ├── payroll.html
│               ├── payslip.html
│               ├── payments.html
│               ├── reports.html
│               ├── css/style.css
│               └── js/script.js
│
├── pom.xml                             <-- Root Maven POM
└── README.md                           <-- Complete Project Guide
```

---

## 🗄 Database Schema & Setup

### 1. Create and Seed Database in MySQL
Open your MySQL Command Line Client or MySQL Workbench and run:

```sql
source c:/Users/ullas/Downloads/prACTICE/PayrollManagementSystem/database/payroll_management.sql;
```

Alternatively, copy and paste the contents of `database/payroll_management.sql` into **MySQL Workbench** or **phpMyAdmin** and execute it.

The script creates the database `payroll_management` with 8 relational tables:
1. `admins`: Administrator login accounts with SHA-256 salted password hashes.
2. `departments`: Company departments (Engineering, HR, Finance, etc.).
3. `salary_structures`: Default grade allowances and deduction percentages.
4. `employees`: Complete employee profiles, bank information, and designations.
5. `attendance`: Daily presence tracking with unique `(employee_id, attendance_date)` constraint.
6. `payroll`: Monthly calculated compensation figures with unique `(employee_id, salary_month)` constraint.
7. `payments`: Salary disbursement transactions with foreign keys.
8. `company_settings`: Corporate metadata (Company Name, Address, Contact, Default Rules).

---

## ⚙ Configuring Database Credentials

Open `backend/src/main/resources/db.properties` (or `src/main/resources/db.properties`) and ensure your local MySQL password matches:

```properties
db.url=jdbc:mysql://localhost:3306/payroll_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
db.user=root
db.password=root
db.driver=com.mysql.cj.jdbc.Driver
```

*(If your MySQL `root` user has no password or a different password like `mysql` or `admin`, update `db.password` accordingly).*

---

## 🚀 Running with Apache Tomcat & IDEs

### Method 1: Eclipse IDE (Recommended for College Students)
1. Open **Eclipse IDE for Enterprise Java and Web Developers**.
2. Go to **File** → **Import...** → **Maven** → **Existing Maven Projects**.
3. Browse and select the `PayrollManagementSystem` folder.
4. Click **Finish**. Eclipse will import and configure the project.
5. Add Apache Tomcat 9:
   - In the **Servers** tab, right-click → **New** → **Server**.
   - Select **Apache** → **Tomcat v9.0 Server** (or your installed Tomcat version).
   - Point to your local Tomcat directory.
6. Right-click the project in Project Explorer → **Run As** → **Run on Server**.
7. Access the application in your browser:  
   👉 `http://localhost:8080/payroll/` (or `http://localhost:8080/`)

---

### Method 2: IntelliJ IDEA
1. Open IntelliJ IDEA Ultimate.
2. Select **Open** and select the `PayrollManagementSystem` folder.
3. IntelliJ will recognize the `pom.xml` and resolve Maven dependencies.
4. Go to **Run** → **Edit Configurations...** → Click **+** → **Tomcat Server** → **Local**.
5. In the **Deployment** tab, click **+** → **Artifact...** → Select `payroll:war` or `payroll-management-system:war exploded`.
6. Set **Application context** to `/payroll` (or `/`).
7. Click **Apply** and **Run**.
8. Access in browser:  
   👉 `http://localhost:8080/payroll/`

---

### Method 3: Maven CLI & Standalone Tomcat
If you have Maven and Tomcat installed on your computer:
1. Open terminal inside `PayrollManagementSystem/`:
   ```bash
   mvn clean package
   ```
2. This generates `target/payroll.war`.
3. Copy `payroll.war` into your Tomcat `webapps/` directory:
   ```bash
   cp target/payroll.war /path/to/tomcat/webapps/
   ```
4. Start Tomcat:
   - On Windows: `bin\startup.bat`
   - On Linux/Mac: `bin/startup.sh`
5. Open browser:  
   👉 `http://localhost:8080/payroll/`

---

## 🔑 Sample Credentials for Demonstration

The database is pre-seeded with two administrative accounts for demonstration:

| Role | Username | Password | Email | Access Scope |
| :--- | :--- | :--- | :--- | :--- |
| **Super Admin** | `admin` | `admin123` | `admin@payrollapp.com` | Full Administrative Access |
| **HR Admin** | `hr_admin` | `hr123` | `hr@payrollapp.com` | Operations & Payroll Processing |

*Note: Passwords are protected using cryptographic SHA-256 with random salt strings; plaintext passwords are never stored.*

---

## 📡 REST API Endpoints Reference

All endpoints return structured JSON with `{ "success": boolean, "message": string, "data": ... }` and standard HTTP status codes (`200 OK`, `201 Created`, `400 Bad Request`, `401 Unauthorized`, `404 Not Found`, `500 Server Error`).

### 1. Authentication (`/api/auth/*`)
- `POST /api/auth/login`: Authenticate admin (`{ "username": "...", "password": "..." }`).
- `POST /api/auth/logout`: Invalidate current session.
- `GET  /api/auth/status`: Check active session profile.

### 2. Employees (`/api/employees/*`)
- `GET    /api/employees`: List employees with filters (`?query=...&departmentId=...&status=...`).
- `GET    /api/employees/{id}`: Retrieve profile by ID (e.g. `EMP1001`).
- `GET    /api/employees/next-id`: Get next auto-incremented employee code (`EMP1008`).
- `POST   /api/employees`: Create new employee with validation.
- `PUT    /api/employees/{id}`: Update employee details.
- `DELETE /api/employees/{id}`: Delete employee record.

### 3. Attendance (`/api/attendance/*`)
- `GET  /api/attendance?date=YYYY-MM-DD`: Get attendance for a specific date with count totals.
- `GET  /api/attendance?employeeId=EMP1001&month=YYYY-MM`: Monthly attendance register.
- `GET  /api/attendance/summary?employeeId=EMP1001&month=YYYY-MM`: Computed attendance percentage and working days.
- `POST /api/attendance`: Record or update daily attendance (`{ "employeeId", "attendanceDate", "status", "remarks" }`).

### 4. Payroll Engine (`/api/payroll/*`)
- `GET    /api/payroll`: Retrieve payrolls (`?month=YYYY-MM&status=...`).
- `GET    /api/payroll/{id}`: Get single payroll record.
- `POST   /api/payroll/calculate`: Live interactive salary calculation preview without saving.
- `POST   /api/payroll/generate`: Generate and persist payroll record.
- `POST   /api/payroll/process-batch`: Batch process all active employees for a given month.
- `DELETE /api/payroll/{id}`: Delete payroll record.

### 5. Payslips (`/api/payslips/*`)
- `GET /api/payslips/{id}`: Returns finalized historical payroll figures and company branding for printable pay stub.

### 6. Disbursements & Payments (`/api/payments/*`)
- `GET  /api/payments`: List payments (`?month=YYYY-MM&method=...&status=...`).
- `POST /api/payments`: Record salary payment with transaction reference, atomically marking payroll as `Paid`.

### 7. Dashboard & Reports (`/api/dashboard`, `/api/reports/*`)
- `GET /api/dashboard`: Aggregated KPIs, payroll trend, department distribution, and recent activities.
- `GET /api/reports/payroll`: Detailed payroll audit report.
- `GET /api/reports/payments`: Disbursement audit trail report.
- `GET /api/reports/employees`: Master compensation roster report.

---

## 🎓 Module Walkthrough & College Viva Checklist

During your project demonstration or oral examination (Viva Voce), follow this recommended flow:

1. **Login & Session Security**:
   - Demonstrate entering wrong credentials to show the validation error toast.
   - Click the demo credential pill (`admin` / `admin123`) to log in.
   - Show how navigating directly to `/api/employees` in an incognito window returns `401 Unauthorized` via `AuthFilter`.

2. **Executive Dashboard**:
   - Point out dynamic counts for Total Employees, Active Staff, Total Monthly Payroll, and Pending Payments.
   - Highlight the CSS-rendered monthly payroll trend chart and department staffing percentages.

3. **Employee CRUD**:
   - Add a new employee: Notice the automatically calculated Employee ID (`EMP1008`).
   - Search for an employee by name or filter by department.
   - Click the "View Profile" icon to showcase the modal with masked bank account numbers.

4. **Attendance Management**:
   - Select a date and mark an employee as `Half Day` or `Leave`.
   - Explain how attendance links directly to the salary deduction calculation.

5. **Live Payroll Calculation & Batch Generation**:
   - Open the "Generate Payroll" modal.
   - Type in performance bonus or overtime hours: note how the live preview updates instantaneously via Java backend calculation.
   - Click "Batch Generate" to process all employees for a given month in one click.

6. **Payslip Printing & PDF Export**:
   - Open a payslip: showcase the corporate layout with company letterhead, metadata, earnings breakdown, deductions breakdown, and Net Pay in words.
   - Click "Print Payslip" or "Download PDF" to show the print-optimized view (`@media print`).

7. **Recording Payments & Transactions**:
   - Click the payment icon on a pending payroll record.
   - Enter payment method (e.g. `Bank Transfer`) and reference (`NEFT-123456789`).
   - Show that the payroll status atomically changes to `Paid` in the database.

8. **Audits & CSV Export**:
   - Navigate to **Reports & Audits**, filter by month, and click **Export to CSV** to demonstrate instantaneous CSV file download.

---

## 👨‍💻 Developer & Project Details
- **Project Name**: Full-Stack Payroll Management System
- **Architecture**: 3-Tier Enterprise Web Application
- **Backend Technology**: Core Java Servlets, JDBC, MySQL
- **Frontend Technology**: HTML5, CSS3, Vanilla JavaScript (ES6+), Fetch API
- **License**: Educational College Project (Open Source)
