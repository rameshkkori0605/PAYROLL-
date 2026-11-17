package com.payroll.service;

import com.payroll.model.Payroll;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PayrollService {
    List<Payroll> getPayrolls(String month, String status, Integer departmentId) throws SQLException;
    Payroll getPayrollById(int payrollId) throws SQLException;
    Payroll computePayroll(String employeeId, String month, BigDecimal bonus, BigDecimal overtime,
                           BigDecimal otherAllowances, BigDecimal otherDeductions) throws SQLException;
    Payroll generateAndSavePayroll(String employeeId, String month, BigDecimal bonus, BigDecimal overtime,
                                  BigDecimal otherAllowances, BigDecimal otherDeductions) throws SQLException;
    int processBatchPayroll(String month) throws SQLException;
    boolean deletePayroll(int payrollId) throws SQLException;
}
