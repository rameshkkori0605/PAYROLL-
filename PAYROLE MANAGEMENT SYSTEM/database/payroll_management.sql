-- ====================================================================
-- PAYROLL MANAGEMENT SYSTEM - DATABASE SCHEMA & SAMPLE DATA
-- Database: payroll_management
-- Technology Stack: MySQL 8.x / 5.7+
-- ====================================================================

CREATE DATABASE IF NOT EXISTS payroll_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE payroll_management;

-- --------------------------------------------------------------------
-- 1. Table: admins
-- --------------------------------------------------------------------
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS payroll;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS salary_structures;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS company_settings;
DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 2. Table: departments
-- --------------------------------------------------------------------
CREATE TABLE departments (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 3. Table: salary_structures
-- --------------------------------------------------------------------
CREATE TABLE salary_structures (
    structure_id INT AUTO_INCREMENT PRIMARY KEY,
    grade_name VARCHAR(50) NOT NULL UNIQUE,
    hra_percentage DECIMAL(5,2) NOT NULL DEFAULT 20.00,
    da_percentage DECIMAL(5,2) NOT NULL DEFAULT 10.00,
    pf_percentage DECIMAL(5,2) NOT NULL DEFAULT 12.00,
    tax_percentage DECIMAL(5,2) NOT NULL DEFAULT 5.00,
    professional_tax DECIMAL(10,2) NOT NULL DEFAULT 200.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- 4. Table: employees
-- --------------------------------------------------------------------
CREATE TABLE employees (
    employee_id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    address TEXT,
    department_id INT NOT NULL,
    designation VARCHAR(100) NOT NULL,
    joining_date DATE NOT NULL,
    basic_salary DECIMAL(12,2) NOT NULL,
    bank_name VARCHAR(100) NOT NULL,
    bank_account_no VARCHAR(50) NOT NULL,
    bank_ifsc VARCHAR(20) NOT NULL,
    employment_status ENUM('Active', 'Inactive', 'Terminated', 'On Leave') NOT NULL DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_emp_dept FOREIGN KEY (department_id) REFERENCES departments(department_id) ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_emp_dept ON employees(department_id);
CREATE INDEX idx_emp_status ON employees(employment_status);

-- --------------------------------------------------------------------
-- 5. Table: attendance
-- --------------------------------------------------------------------
CREATE TABLE attendance (
    attendance_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Half Day', 'Leave') NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_att_emp FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_emp_date UNIQUE (employee_id, attendance_date)
) ENGINE=InnoDB;

CREATE INDEX idx_att_date ON attendance(attendance_date);
CREATE INDEX idx_att_emp_status ON attendance(employee_id, status);

-- --------------------------------------------------------------------
-- 6. Table: payroll
-- --------------------------------------------------------------------
CREATE TABLE payroll (
    payroll_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(20) NOT NULL,
    salary_month VARCHAR(7) NOT NULL, -- e.g. '2026-10' (YYYY-MM)
    total_working_days INT NOT NULL DEFAULT 30,
    present_days DECIMAL(4,1) NOT NULL DEFAULT 0.0,
    paid_leaves DECIMAL(4,1) NOT NULL DEFAULT 0.0,
    unpaid_absent_days DECIMAL(4,1) NOT NULL DEFAULT 0.0,
    basic_salary DECIMAL(12,2) NOT NULL,
    hra DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    da DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    other_allowances DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    bonus DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    overtime_pay DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    gross_salary DECIMAL(12,2) NOT NULL,
    pf_deduction DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    prof_tax DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    income_tax DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    leave_deduction DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    other_deductions DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    total_deductions DECIMAL(12,2) NOT NULL,
    net_salary DECIMAL(12,2) NOT NULL,
    payment_status ENUM('Pending', 'Paid') NOT NULL DEFAULT 'Pending',
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payroll_emp FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_emp_month UNIQUE (employee_id, salary_month)
) ENGINE=InnoDB;

CREATE INDEX idx_payroll_month ON payroll(salary_month);
CREATE INDEX idx_payroll_status ON payroll(payment_status);

-- --------------------------------------------------------------------
-- 7. Table: payments
-- --------------------------------------------------------------------
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    payroll_id INT NOT NULL UNIQUE,
    employee_id VARCHAR(20) NOT NULL,
    payment_date DATE NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_method ENUM('Bank Transfer', 'Cheque', 'UPI', 'Cash') NOT NULL DEFAULT 'Bank Transfer',
    transaction_reference VARCHAR(100) NOT NULL,
    status ENUM('Success', 'Pending', 'Failed') NOT NULL DEFAULT 'Success',
    notes TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pay_payroll FOREIGN KEY (payroll_id) REFERENCES payroll(payroll_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_pay_emp FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_pay_date ON payments(payment_date);

-- --------------------------------------------------------------------
-- 8. Table: company_settings
-- --------------------------------------------------------------------
CREATE TABLE company_settings (
    setting_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL,
    company_address TEXT NOT NULL,
    company_email VARCHAR(100) NOT NULL,
    company_phone VARCHAR(30) NOT NULL,
    currency_symbol VARCHAR(10) DEFAULT '₹',
    standard_working_days INT DEFAULT 30,
    default_hra_pct DECIMAL(5,2) DEFAULT 20.00,
    default_da_pct DECIMAL(5,2) DEFAULT 10.00,
    default_pf_pct DECIMAL(5,2) DEFAULT 12.00,
    default_ptax DECIMAL(10,2) DEFAULT 200.00,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ====================================================================
-- SAMPLE DATA INSERTION
-- ====================================================================

-- 1. Admins
-- Password for admin: admin123 (hash + salt)
-- Password for hr_admin: hr123 (hash + salt)
INSERT INTO admins (username, password_hash, salt, full_name, email, role) VALUES
('admin', '43cfa277b1a58ea872a8aed8aae16079485c7d5a627347c12e269687411fd009', 'a1b2c3d4e5f67890', 'System Administrator', 'admin@payrollapp.com', 'SUPER_ADMIN'),
('hr_admin', 'bc1b6cfbf38bc9f5354a94b5c1f2e85d3079ef2fa534891861af67014e022b9e', 'b2c3d4e5f678901a', 'HR Executive Officer', 'hr@payrollapp.com', 'HR_ADMIN');

-- 2. Departments
INSERT INTO departments (department_id, department_name, description) VALUES
(1, 'Engineering', 'Software Development, QA, and Infrastructure'),
(2, 'Human Resources', 'Talent Acquisition, Employee Relations, and Payroll'),
(3, 'Finance & Accounts', 'Financial Planning, Budgeting, and Audits'),
(4, 'Marketing & Sales', 'Brand Outreach, Customer Success, and Growth'),
(5, 'Operations', 'Business Logistics, Facilities, and Procurement');

-- 3. Salary Structures
INSERT INTO salary_structures (grade_name, hra_percentage, da_percentage, pf_percentage, tax_percentage, professional_tax) VALUES
('Grade A - Executive', 25.00, 15.00, 12.00, 10.00, 200.00),
('Grade B - Senior', 22.00, 12.00, 12.00, 7.50, 200.00),
('Grade C - Associate', 20.00, 10.00, 12.00, 5.00, 200.00),
('Grade D - Intern/Junior', 15.00, 8.00, 12.00, 0.00, 200.00);

-- 4. Company Settings
INSERT INTO company_settings (company_name, company_address, company_email, company_phone, currency_symbol, standard_working_days, default_hra_pct, default_da_pct, default_pf_pct, default_ptax) VALUES
('Apex Global Solutions Inc.', '104 Innovate Boulevard, Tech Hub Park, Sector 5, Bangalore - 560100', 'payroll@apexsolutions.com', '+91 80 4123 4567', '₹', 30, 20.00, 10.00, 12.00, 200.00);

-- 5. Employees
INSERT INTO employees (employee_id, first_name, last_name, email, phone, gender, address, department_id, designation, joining_date, basic_salary, bank_name, bank_account_no, bank_ifsc, employment_status) VALUES
('EMP1001', 'Aarav', 'Sharma', 'aarav.sharma@apexsolutions.com', '+91 9876543210', 'Male', '45 Indiranagar, 100ft Road, Bangalore', 1, 'Senior Software Engineer', '2023-03-15', 75000.00, 'HDFC Bank', '50100234891102', 'HDFC0001234', 'Active'),
('EMP1002', 'Priya', 'Nair', 'priya.nair@apexsolutions.com', '+91 9876543211', 'Female', '12 Koramangala 4th Block, Bangalore', 2, 'HR Business Partner', '2023-06-01', 58000.00, 'ICICI Bank', '002101569842', 'ICIC0000021', 'Active'),
('EMP1003', 'Rohan', 'Verma', 'rohan.verma@apexsolutions.com', '+91 9876543212', 'Male', '78 Whitefield Main Road, Bangalore', 1, 'Full Stack Developer', '2024-01-10', 50000.00, 'State Bank of India', '30491823901', 'SBIN0004567', 'Active'),
('EMP1004', 'Sneha', 'Patel', 'sneha.patel@apexsolutions.com', '+91 9876543213', 'Female', '22 HSR Layout Sector 2, Bangalore', 3, 'Financial Analyst', '2023-09-20', 52000.00, 'Axis Bank', '918020038910234', 'UTIB0000123', 'Active'),
('EMP1005', 'Vikram', 'Rao', 'vikram.rao@apexsolutions.com', '+91 9876543214', 'Male', '15 Jayanagar 9th Block, Bangalore', 4, 'Growth Marketing Lead', '2023-11-05', 62000.00, 'Kotak Mahindra Bank', '6712398401', 'KKBK0000456', 'Active'),
('EMP1006', 'Ananya', 'Iyer', 'ananya.iyer@apexsolutions.com', '+91 9876543215', 'Female', '88 Malleshwaram 18th Cross, Bangalore', 1, 'QA Automation Engineer', '2024-04-01', 45000.00, 'HDFC Bank', '50100456789012', 'HDFC0001234', 'Active'),
('EMP1007', 'Karan', 'Mehta', 'karan.mehta@apexsolutions.com', '+91 9876543216', 'Male', '34 Bellandur Green Glen, Bangalore', 5, 'Operations Specialist', '2024-02-15', 40000.00, 'ICICI Bank', '002105432198', 'ICIC0000021', 'Active');

-- 6. Sample Attendance (Past dates for demonstration)
INSERT INTO attendance (employee_id, attendance_date, status, remarks) VALUES
-- EMP1001
('EMP1001', '2026-10-01', 'Present', 'Regular duty'),
('EMP1001', '2026-10-02', 'Present', 'Regular duty'),
('EMP1001', '2026-10-03', 'Present', 'Regular duty'),
('EMP1001', '2026-10-04', 'Leave', 'Sick leave approved'),
('EMP1001', '2026-10-05', 'Present', 'Regular duty'),
-- EMP1002
('EMP1002', '2026-10-01', 'Present', 'Regular duty'),
('EMP1002', '2026-10-02', 'Present', 'Regular duty'),
('EMP1002', '2026-10-03', 'Half Day', 'Personal emergency'),
('EMP1002', '2026-10-04', 'Present', 'Regular duty'),
('EMP1002', '2026-10-05', 'Present', 'Regular duty'),
-- EMP1003
('EMP1003', '2026-10-01', 'Present', 'Regular duty'),
('EMP1003', '2026-10-02', 'Absent', 'Uninformed absence'),
('EMP1003', '2026-10-03', 'Present', 'Regular duty'),
('EMP1003', '2026-10-04', 'Present', 'Regular duty'),
('EMP1003', '2026-10-05', 'Present', 'Regular duty'),
-- EMP1004
('EMP1004', '2026-10-01', 'Present', 'Regular duty'),
('EMP1004', '2026-10-02', 'Present', 'Regular duty'),
('EMP1004', '2026-10-03', 'Present', 'Regular duty'),
('EMP1004', '2026-10-04', 'Present', 'Regular duty'),
('EMP1004', '2026-10-05', 'Present', 'Regular duty'),
-- EMP1005
('EMP1005', '2026-10-01', 'Present', 'Regular duty'),
('EMP1005', '2026-10-02', 'Present', 'Regular duty'),
('EMP1005', '2026-10-03', 'Present', 'Regular duty'),
('EMP1005', '2026-10-04', 'Leave', 'Casual leave approved'),
('EMP1005', '2026-10-05', 'Present', 'Regular duty');

-- 7. Sample Payroll for '2026-10'
-- EMP1001: Basic=75000, HRA=15000 (20%), DA=7500 (10%), Allow=3000, Bonus=5000, OT=0 => Gross = 100500.00
-- PF=9000 (12% of basic), PT=200, Tax=5025 (5%), Leave=0 => Deductions = 14225.00 => Net = 86275.00 [PAID]
INSERT INTO payroll (payroll_id, employee_id, salary_month, total_working_days, present_days, paid_leaves, unpaid_absent_days, basic_salary, hra, da, other_allowances, bonus, overtime_pay, gross_salary, pf_deduction, prof_tax, income_tax, leave_deduction, other_deductions, total_deductions, net_salary, payment_status) VALUES
(1, 'EMP1001', '2026-10', 30, 29.0, 1.0, 0.0, 75000.00, 15000.00, 7500.00, 3000.00, 5000.00, 0.00, 100500.00, 9000.00, 200.00, 5025.00, 0.00, 0.00, 14225.00, 86275.00, 'Paid'),

-- EMP1002: Basic=58000, HRA=11600, DA=5800, Allow=2500, Bonus=0, OT=0 => Gross = 77900.00
-- PF=6960, PT=200, Tax=3895, Leave=0 => Deductions = 11055.00 => Net = 66845.00 [PAID]
(2, 'EMP1002', '2026-10', 30, 29.5, 0.0, 0.5, 58000.00, 11600.00, 5800.00, 2500.00, 0.00, 0.00, 77900.00, 6960.00, 200.00, 3895.00, 966.67, 0.00, 12021.67, 65878.33, 'Paid'),

-- EMP1003: Basic=50000, HRA=10000, DA=5000, Allow=2000, Bonus=0, OT=1500 => Gross = 68500.00
-- PF=6000, PT=200, Tax=3425, Leave=1666.67 (1 unpaid day) => Deductions = 11291.67 => Net = 57208.33 [PAID]
(3, 'EMP1003', '2026-10', 30, 29.0, 0.0, 1.0, 50000.00, 10000.00, 5000.00, 2000.00, 0.00, 1500.00, 68500.00, 6000.00, 200.00, 3425.00, 1666.67, 0.00, 11291.67, 57208.33, 'Paid'),

-- EMP1004: Basic=52000, HRA=10400, DA=5200, Allow=2000, Bonus=2500, OT=0 => Gross = 72100.00
-- PF=6240, PT=200, Tax=3605, Leave=0 => Deductions = 10045.00 => Net = 62055.00 [PENDING]
(4, 'EMP1004', '2026-10', 30, 30.0, 0.0, 0.0, 52000.00, 10400.00, 5200.00, 2000.00, 2500.00, 0.00, 72100.00, 6240.00, 200.00, 3605.00, 0.00, 0.00, 10045.00, 62055.00, 'Pending'),

-- EMP1005: Basic=62000, HRA=12400, DA=6200, Allow=3000, Bonus=4000, OT=0 => Gross = 87600.00
-- PF=7440, PT=200, Tax=4380, Leave=0 => Deductions = 12020.00 => Net = 75580.00 [PENDING]
(5, 'EMP1005', '2026-10', 30, 29.0, 1.0, 0.0, 62000.00, 12400.00, 6200.00, 3000.00, 4000.00, 0.00, 87600.00, 7440.00, 200.00, 4380.00, 0.00, 0.00, 12020.00, 75580.00, 'Pending');

-- 8. Sample Payments
INSERT INTO payments (payroll_id, employee_id, payment_date, amount, payment_method, transaction_reference, status, notes) VALUES
(1, 'EMP1001', '2026-10-31', 86275.00, 'Bank Transfer', 'NEFT-HDFC-9842104921', 'Success', 'October 2026 Salary credited via NEFT'),
(2, 'EMP1002', '2026-10-31', 65878.33, 'Bank Transfer', 'IMPS-ICIC-8723419042', 'Success', 'October 2026 Salary credited via IMPS'),
(3, 'EMP1003', '2026-10-31', 57208.33, 'UPI', 'UPI-SBIN-5412984120', 'Success', 'October 2026 Salary credited via UPI Corporate');
