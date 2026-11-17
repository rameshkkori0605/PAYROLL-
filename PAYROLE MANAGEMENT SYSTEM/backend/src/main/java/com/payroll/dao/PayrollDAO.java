package com.payroll.dao;

import com.payroll.model.Payroll;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface PayrollDAO {
    List<Payroll> findAll(String month, String status, Integer departmentId) throws SQLException;
    Payroll findById(int payrollId) throws SQLException;
    Payroll findByEmployeeAndMonth(String employeeId, String month) throws SQLException;
    boolean insert(Payroll payroll) throws SQLException;
    boolean update(Payroll payroll) throws SQLException;
    boolean updateStatus(int payrollId, String status) throws SQLException;
    boolean delete(int payrollId) throws SQLException;
    boolean existsForMonth(String employeeId, String month) throws SQLException;
    BigDecimal getTotalPayrollAmount(String month) throws SQLException;
    BigDecimal getTotalPaidAmount(String month) throws SQLException;
    int getPendingCount(String month) throws SQLException;
    List<Map<String, Object>> getMonthlyTrend(int limit) throws SQLException;
}
