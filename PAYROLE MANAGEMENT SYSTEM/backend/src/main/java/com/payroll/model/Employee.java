package com.payroll.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Employee - Core model entity representing an employee.
 * Extends Person (Inheritance and Polymorphism).
 */
public class Employee extends Person {

    private static final long serialVersionUID = 1L;

    private String employeeId;
    private String gender;
    private String address;
    private int departmentId;
    private String departmentName; // Populated via SQL JOIN
    private String designation;
    private String joiningDate;    // Formatted 'YYYY-MM-DD'
    private BigDecimal basicSalary;
    private String bankName;
    private String bankAccountNo;
    private String bankIfsc;
    private String employmentStatus; // 'Active', 'Inactive', 'Terminated', 'On Leave'
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Employee() {
        super();
        this.basicSalary = BigDecimal.ZERO;
        this.employmentStatus = "Active";
    }

    public Employee(String employeeId, String firstName, String lastName, String email, String phone,
                    String gender, String address, int departmentId, String designation,
                    String joiningDate, BigDecimal basicSalary, String bankName,
                    String bankAccountNo, String bankIfsc, String employmentStatus) {
        super(firstName, lastName, email, phone);
        this.employeeId = employeeId;
        this.gender = gender;
        this.address = address;
        this.departmentId = departmentId;
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.basicSalary = basicSalary;
        this.bankName = bankName;
        this.bankAccountNo = bankAccountNo;
        this.bankIfsc = bankIfsc;
        this.employmentStatus = employmentStatus;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Masks bank account number for security (PCI/PII compliance).
     * e.g., 50100234891102 -> *******1102
     */
    public String getMaskedBankAccount() {
        if (bankAccountNo == null || bankAccountNo.length() < 4) {
            return "****";
        }
        int len = bankAccountNo.length();
        String lastFour = bankAccountNo.substring(len - 4);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len - 4; i++) {
            sb.append("*");
        }
        sb.append(lastFour);
        return sb.toString();
    }

    @Override
    public String getRoleDescription() {
        return designation + (departmentName != null ? " (" + departmentName + ")" : "");
    }
}
