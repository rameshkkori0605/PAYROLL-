package com.payroll.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Payroll implements Serializable {

    private static final long serialVersionUID = 1L;

    private int payrollId;
    private String employeeId;
    private String employeeName;    // From JOIN
    private String departmentName;  // From JOIN
    private String designation;     // From JOIN
    private String bankName;        // From JOIN
    private String bankAccountNo;   // From JOIN
    private String bankIfsc;        // From JOIN

    private String salaryMonth;     // 'YYYY-MM'
    private int totalWorkingDays;
    private double presentDays;
    private double paidLeaves;
    private double unpaidAbsentDays;

    // Earnings
    private BigDecimal basicSalary;
    private BigDecimal hra;
    private BigDecimal da;
    private BigDecimal otherAllowances;
    private BigDecimal bonus;
    private BigDecimal overtimePay;
    private BigDecimal grossSalary;

    // Deductions
    private BigDecimal pfDeduction;
    private BigDecimal profTax;
    private BigDecimal incomeTax;
    private BigDecimal leaveDeduction;
    private BigDecimal otherDeductions;
    private BigDecimal totalDeductions;

    // Net
    private BigDecimal netSalary;
    private String paymentStatus;   // 'Pending', 'Paid'
    private Timestamp generatedAt;

    public Payroll() {
        this.totalWorkingDays = 30;
        this.presentDays = 0;
        this.paidLeaves = 0;
        this.unpaidAbsentDays = 0;
        this.basicSalary = BigDecimal.ZERO;
        this.hra = BigDecimal.ZERO;
        this.da = BigDecimal.ZERO;
        this.otherAllowances = BigDecimal.ZERO;
        this.bonus = BigDecimal.ZERO;
        this.overtimePay = BigDecimal.ZERO;
        this.grossSalary = BigDecimal.ZERO;
        this.pfDeduction = BigDecimal.ZERO;
        this.profTax = BigDecimal.ZERO;
        this.incomeTax = BigDecimal.ZERO;
        this.leaveDeduction = BigDecimal.ZERO;
        this.otherDeductions = BigDecimal.ZERO;
        this.totalDeductions = BigDecimal.ZERO;
        this.netSalary = BigDecimal.ZERO;
        this.paymentStatus = "Pending";
    }

    // Getters and Setters
    public int getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public int getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public void setTotalWorkingDays(int totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public double getPresentDays() {
        return presentDays;
    }

    public void setPresentDays(double presentDays) {
        this.presentDays = presentDays;
    }

    public double getPaidLeaves() {
        return paidLeaves;
    }

    public void setPaidLeaves(double paidLeaves) {
        this.paidLeaves = paidLeaves;
    }

    public double getUnpaidAbsentDays() {
        return unpaidAbsentDays;
    }

    public void setUnpaidAbsentDays(double unpaidAbsentDays) {
        this.unpaidAbsentDays = unpaidAbsentDays;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getHra() {
        return hra;
    }

    public void setHra(BigDecimal hra) {
        this.hra = hra;
    }

    public BigDecimal getDa() {
        return da;
    }

    public void setDa(BigDecimal da) {
        this.da = da;
    }

    public BigDecimal getOtherAllowances() {
        return otherAllowances;
    }

    public void setOtherAllowances(BigDecimal otherAllowances) {
        this.otherAllowances = otherAllowances;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }

    public BigDecimal getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
    }

    public BigDecimal getPfDeduction() {
        return pfDeduction;
    }

    public void setPfDeduction(BigDecimal pfDeduction) {
        this.pfDeduction = pfDeduction;
    }

    public BigDecimal getProfTax() {
        return profTax;
    }

    public void setProfTax(BigDecimal profTax) {
        this.profTax = profTax;
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(BigDecimal incomeTax) {
        this.incomeTax = incomeTax;
    }

    public BigDecimal getLeaveDeduction() {
        return leaveDeduction;
    }

    public void setLeaveDeduction(BigDecimal leaveDeduction) {
        this.leaveDeduction = leaveDeduction;
    }

    public BigDecimal getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(BigDecimal otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public BigDecimal getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(BigDecimal netSalary) {
        this.netSalary = netSalary;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Timestamp generatedAt) {
        this.generatedAt = generatedAt;
    }
}
