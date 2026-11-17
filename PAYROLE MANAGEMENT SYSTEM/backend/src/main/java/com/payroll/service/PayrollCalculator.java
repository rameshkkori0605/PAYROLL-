package com.payroll.service;

import java.math.BigDecimal;

/**
 * PayrollCalculator - Interface demonstrating OOP Abstraction and Polymorphism for salary calculations.
 */
public interface PayrollCalculator {

    BigDecimal calculateHra(BigDecimal basicSalary, BigDecimal hraPercentage);

    BigDecimal calculateDa(BigDecimal basicSalary, BigDecimal daPercentage);

    BigDecimal calculateGross(BigDecimal basic, BigDecimal hra, BigDecimal da,
                             BigDecimal otherAllowances, BigDecimal bonus, BigDecimal overtimePay);

    BigDecimal calculatePf(BigDecimal basicSalary, BigDecimal pfPercentage);

    BigDecimal calculateLeaveDeduction(BigDecimal basicSalary, int totalWorkingDays, double unpaidAbsentDays);

    BigDecimal calculateIncomeTax(BigDecimal grossSalary, BigDecimal taxPercentage);

    BigDecimal calculateTotalDeductions(BigDecimal pf, BigDecimal profTax, BigDecimal incomeTax,
                                        BigDecimal leaveDeduction, BigDecimal otherDeductions);

    BigDecimal calculateNetSalary(BigDecimal grossSalary, BigDecimal totalDeductions);
}
