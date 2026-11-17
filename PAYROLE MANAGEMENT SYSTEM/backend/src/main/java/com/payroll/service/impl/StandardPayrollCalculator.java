package com.payroll.service.impl;

import com.payroll.service.PayrollCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * StandardPayrollCalculator - Standard corporate implementation of PayrollCalculator using BigDecimal.
 */
public class StandardPayrollCalculator implements PayrollCalculator {

    private static final BigDecimal HUNDRED = new BigDecimal("100.00");

    @Override
    public BigDecimal calculateHra(BigDecimal basicSalary, BigDecimal hraPercentage) {
        if (basicSalary == null || hraPercentage == null) return BigDecimal.ZERO;
        return basicSalary.multiply(hraPercentage).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateDa(BigDecimal basicSalary, BigDecimal daPercentage) {
        if (basicSalary == null || daPercentage == null) return BigDecimal.ZERO;
        return basicSalary.multiply(daPercentage).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateGross(BigDecimal basic, BigDecimal hra, BigDecimal da,
                                     BigDecimal otherAllowances, BigDecimal bonus, BigDecimal overtimePay) {
        BigDecimal b = basic != null ? basic : BigDecimal.ZERO;
        BigDecimal h = hra != null ? hra : BigDecimal.ZERO;
        BigDecimal d = da != null ? da : BigDecimal.ZERO;
        BigDecimal o = otherAllowances != null ? otherAllowances : BigDecimal.ZERO;
        BigDecimal bo = bonus != null ? bonus : BigDecimal.ZERO;
        BigDecimal ot = overtimePay != null ? overtimePay : BigDecimal.ZERO;

        return b.add(h).add(d).add(o).add(bo).add(ot).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculatePf(BigDecimal basicSalary, BigDecimal pfPercentage) {
        if (basicSalary == null || pfPercentage == null) return BigDecimal.ZERO;
        return basicSalary.multiply(pfPercentage).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateLeaveDeduction(BigDecimal basicSalary, int totalWorkingDays, double unpaidAbsentDays) {
        if (basicSalary == null || totalWorkingDays <= 0 || unpaidAbsentDays <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        BigDecimal perDaySalary = basicSalary.divide(BigDecimal.valueOf(totalWorkingDays), 6, RoundingMode.HALF_UP);
        return perDaySalary.multiply(BigDecimal.valueOf(unpaidAbsentDays)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateIncomeTax(BigDecimal grossSalary, BigDecimal taxPercentage) {
        if (grossSalary == null || taxPercentage == null || taxPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return grossSalary.multiply(taxPercentage).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateTotalDeductions(BigDecimal pf, BigDecimal profTax, BigDecimal incomeTax,
                                               BigDecimal leaveDeduction, BigDecimal otherDeductions) {
        BigDecimal p = pf != null ? pf : BigDecimal.ZERO;
        BigDecimal pt = profTax != null ? profTax : BigDecimal.ZERO;
        BigDecimal it = incomeTax != null ? incomeTax : BigDecimal.ZERO;
        BigDecimal ld = leaveDeduction != null ? leaveDeduction : BigDecimal.ZERO;
        BigDecimal od = otherDeductions != null ? otherDeductions : BigDecimal.ZERO;

        return p.add(pt).add(it).add(ld).add(od).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateNetSalary(BigDecimal grossSalary, BigDecimal totalDeductions) {
        BigDecimal g = grossSalary != null ? grossSalary : BigDecimal.ZERO;
        BigDecimal d = totalDeductions != null ? totalDeductions : BigDecimal.ZERO;
        BigDecimal net = g.subtract(d).setScale(2, RoundingMode.HALF_UP);
        return net.compareTo(BigDecimal.ZERO) >= 0 ? net : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }
}
