package com.payroll.dao.impl;

import com.payroll.dao.PayrollDAO;
import com.payroll.model.Payroll;
import com.payroll.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayrollDAOImpl implements PayrollDAO {

    private Payroll mapRow(ResultSet rs) throws SQLException {
        Payroll p = new Payroll();
        p.setPayrollId(rs.getInt("payroll_id"));
        p.setEmployeeId(rs.getString("employee_id"));
        p.setEmployeeName(rs.getString("first_name") + " " + rs.getString("last_name"));
        p.setDepartmentName(rs.getString("department_name"));
        p.setDesignation(rs.getString("designation"));
        p.setBankName(rs.getString("bank_name"));
        p.setBankAccountNo(rs.getString("bank_account_no"));
        p.setBankIfsc(rs.getString("bank_ifsc"));
        p.setSalaryMonth(rs.getString("salary_month"));
        p.setTotalWorkingDays(rs.getInt("total_working_days"));
        p.setPresentDays(rs.getDouble("present_days"));
        p.setPaidLeaves(rs.getDouble("paid_leaves"));
        p.setUnpaidAbsentDays(rs.getDouble("unpaid_absent_days"));

        p.setBasicSalary(rs.getBigDecimal("basic_salary"));
        p.setHra(rs.getBigDecimal("hra"));
        p.setDa(rs.getBigDecimal("da"));
        p.setOtherAllowances(rs.getBigDecimal("other_allowances"));
        p.setBonus(rs.getBigDecimal("bonus"));
        p.setOvertimePay(rs.getBigDecimal("overtime_pay"));
        p.setGrossSalary(rs.getBigDecimal("gross_salary"));

        p.setPfDeduction(rs.getBigDecimal("pf_deduction"));
        p.setProfTax(rs.getBigDecimal("prof_tax"));
        p.setIncomeTax(rs.getBigDecimal("income_tax"));
        p.setLeaveDeduction(rs.getBigDecimal("leave_deduction"));
        p.setOtherDeductions(rs.getBigDecimal("other_deductions"));
        p.setTotalDeductions(rs.getBigDecimal("total_deductions"));

        p.setNetSalary(rs.getBigDecimal("net_salary"));
        p.setPaymentStatus(rs.getString("payment_status"));
        p.setGeneratedAt(rs.getTimestamp("generated_at"));
        return p;
    }

    @Override
    public List<Payroll> findAll(String month, String status, Integer departmentId) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, e.first_name, e.last_name, e.designation, e.bank_name, e.bank_account_no, e.bank_ifsc, d.department_name " +
                "FROM payroll p " +
                "JOIN employees e ON p.employee_id = e.employee_id " +
                "JOIN departments d ON e.department_id = d.department_id " +
                "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (month != null && !month.trim().isEmpty()) {
            sql.append("AND p.salary_month = ? ");
            params.add(month.trim());
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("All")) {
            sql.append("AND p.payment_status = ? ");
            params.add(status.trim());
        }

        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
            params.add(departmentId);
        }

        sql.append("ORDER BY p.salary_month DESC, p.payroll_id DESC");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Payroll> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public Payroll findById(int payrollId) throws SQLException {
        String sql = "SELECT p.*, e.first_name, e.last_name, e.designation, e.bank_name, e.bank_account_no, e.bank_ifsc, d.department_name " +
                     "FROM payroll p " +
                     "JOIN employees e ON p.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE p.payroll_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, payrollId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public Payroll findByEmployeeAndMonth(String employeeId, String month) throws SQLException {
        String sql = "SELECT p.*, e.first_name, e.last_name, e.designation, e.bank_name, e.bank_account_no, e.bank_ifsc, d.department_name " +
                     "FROM payroll p " +
                     "JOIN employees e ON p.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE p.employee_id = ? AND p.salary_month = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            stmt.setString(2, month);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public boolean insert(Payroll p) throws SQLException {
        String sql = "INSERT INTO payroll (employee_id, salary_month, total_working_days, present_days, " +
                     "paid_leaves, unpaid_absent_days, basic_salary, hra, da, other_allowances, bonus, " +
                     "overtime_pay, gross_salary, pf_deduction, prof_tax, income_tax, leave_deduction, " +
                     "other_deductions, total_deductions, net_salary, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getEmployeeId());
            stmt.setString(2, p.getSalaryMonth());
            stmt.setInt(3, p.getTotalWorkingDays());
            stmt.setDouble(4, p.getPresentDays());
            stmt.setDouble(5, p.getPaidLeaves());
            stmt.setDouble(6, p.getUnpaidAbsentDays());
            stmt.setBigDecimal(7, p.getBasicSalary());
            stmt.setBigDecimal(8, p.getHra());
            stmt.setBigDecimal(9, p.getDa());
            stmt.setBigDecimal(10, p.getOtherAllowances());
            stmt.setBigDecimal(11, p.getBonus());
            stmt.setBigDecimal(12, p.getOvertimePay());
            stmt.setBigDecimal(13, p.getGrossSalary());
            stmt.setBigDecimal(14, p.getPfDeduction());
            stmt.setBigDecimal(15, p.getProfTax());
            stmt.setBigDecimal(16, p.getIncomeTax());
            stmt.setBigDecimal(17, p.getLeaveDeduction());
            stmt.setBigDecimal(18, p.getOtherDeductions());
            stmt.setBigDecimal(19, p.getTotalDeductions());
            stmt.setBigDecimal(20, p.getNetSalary());
            stmt.setString(21, p.getPaymentStatus());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        p.setPayrollId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean update(Payroll p) throws SQLException {
        String sql = "UPDATE payroll SET total_working_days = ?, present_days = ?, paid_leaves = ?, " +
                     "unpaid_absent_days = ?, basic_salary = ?, hra = ?, da = ?, other_allowances = ?, " +
                     "bonus = ?, overtime_pay = ?, gross_salary = ?, pf_deduction = ?, prof_tax = ?, " +
                     "income_tax = ?, leave_deduction = ?, other_deductions = ?, total_deductions = ?, " +
                     "net_salary = ?, payment_status = ? WHERE payroll_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p.getTotalWorkingDays());
            stmt.setDouble(2, p.getPresentDays());
            stmt.setDouble(3, p.getPaidLeaves());
            stmt.setDouble(4, p.getUnpaidAbsentDays());
            stmt.setBigDecimal(5, p.getBasicSalary());
            stmt.setBigDecimal(6, p.getHra());
            stmt.setBigDecimal(7, p.getDa());
            stmt.setBigDecimal(8, p.getOtherAllowances());
            stmt.setBigDecimal(9, p.getBonus());
            stmt.setBigDecimal(10, p.getOvertimePay());
            stmt.setBigDecimal(11, p.getGrossSalary());
            stmt.setBigDecimal(12, p.getPfDeduction());
            stmt.setBigDecimal(13, p.getProfTax());
            stmt.setBigDecimal(14, p.getIncomeTax());
            stmt.setBigDecimal(15, p.getLeaveDeduction());
            stmt.setBigDecimal(16, p.getOtherDeductions());
            stmt.setBigDecimal(17, p.getTotalDeductions());
            stmt.setBigDecimal(18, p.getNetSalary());
            stmt.setString(19, p.getPaymentStatus());
            stmt.setInt(20, p.getPayrollId());

            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean updateStatus(int payrollId, String status) throws SQLException {
        String sql = "UPDATE payroll SET payment_status = ? WHERE payroll_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, payrollId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean delete(int payrollId) throws SQLException {
        String sql = "DELETE FROM payroll WHERE payroll_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, payrollId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean existsForMonth(String employeeId, String month) throws SQLException {
        String sql = "SELECT 1 FROM payroll WHERE employee_id = ? AND salary_month = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            stmt.setString(2, month);
            rs = stmt.executeQuery();
            return rs.next();
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public BigDecimal getTotalPayrollAmount(String month) throws SQLException {
        String sql = "SELECT COALESCE(SUM(net_salary), 0) FROM payroll WHERE salary_month = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, month);
            rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public BigDecimal getTotalPaidAmount(String month) throws SQLException {
        String sql = "SELECT COALESCE(SUM(net_salary), 0) FROM payroll WHERE salary_month = ? AND payment_status = 'Paid'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, month);
            rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public int getPendingCount(String month) throws SQLException {
        String sql = "SELECT COUNT(*) FROM payroll WHERE salary_month = ? AND payment_status = 'Pending'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, month);
            rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyTrend(int limit) throws SQLException {
        String sql = "SELECT salary_month, SUM(net_salary) AS total_net, SUM(gross_salary) AS total_gross, " +
                     "COUNT(payroll_id) AS emp_count " +
                     "FROM payroll GROUP BY salary_month ORDER BY salary_month DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("month", rs.getString("salary_month"));
                map.put("totalNet", rs.getBigDecimal("total_net"));
                map.put("totalGross", rs.getBigDecimal("total_gross"));
                map.put("count", rs.getInt("emp_count"));
                list.add(map);
            }
            return list;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }
}
