package com.payroll.dao.impl;

import com.payroll.dao.PaymentDAO;
import com.payroll.model.Payment;
import com.payroll.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setPayrollId(rs.getInt("payroll_id"));
        p.setEmployeeId(rs.getString("employee_id"));
        p.setEmployeeName(rs.getString("first_name") + " " + rs.getString("last_name"));
        p.setDepartmentName(rs.getString("department_name"));
        p.setSalaryMonth(rs.getString("salary_month"));
        p.setPaymentDate(rs.getString("payment_date"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setTransactionReference(rs.getString("transaction_reference"));
        p.setStatus(rs.getString("status"));
        p.setNotes(rs.getString("notes"));
        p.setRecordedAt(rs.getTimestamp("recorded_at"));
        return p;
    }

    @Override
    public List<Payment> findAll(String month, String method, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT pay.*, pr.salary_month, e.first_name, e.last_name, d.department_name " +
                "FROM payments pay " +
                "JOIN payroll pr ON pay.payroll_id = pr.payroll_id " +
                "JOIN employees e ON pay.employee_id = e.employee_id " +
                "JOIN departments d ON e.department_id = d.department_id " +
                "WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (month != null && !month.trim().isEmpty()) {
            sql.append("AND pr.salary_month = ? ");
            params.add(month.trim());
        }

        if (method != null && !method.trim().isEmpty() && !method.equalsIgnoreCase("All")) {
            sql.append("AND pay.payment_method = ? ");
            params.add(method.trim());
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("All")) {
            sql.append("AND pay.status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY pay.payment_date DESC, pay.payment_id DESC");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Payment> list = new ArrayList<>();

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
    public Payment findById(int paymentId) throws SQLException {
        String sql = "SELECT pay.*, pr.salary_month, e.first_name, e.last_name, d.department_name " +
                     "FROM payments pay " +
                     "JOIN payroll pr ON pay.payroll_id = pr.payroll_id " +
                     "JOIN employees e ON pay.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE pay.payment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, paymentId);
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
    public Payment findByPayrollId(int payrollId) throws SQLException {
        String sql = "SELECT pay.*, pr.salary_month, e.first_name, e.last_name, d.department_name " +
                     "FROM payments pay " +
                     "JOIN payroll pr ON pay.payroll_id = pr.payroll_id " +
                     "JOIN employees e ON pay.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE pay.payroll_id = ?";
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
    public boolean recordPayment(Payment payment) throws SQLException {
        String insertSql = "INSERT INTO payments (payroll_id, employee_id, payment_date, amount, " +
                           "payment_method, transaction_reference, status, notes) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String updatePayrollSql = "UPDATE payroll SET payment_status = 'Paid' WHERE payroll_id = ?";

        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin ACID Transaction

            insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            insertStmt.setInt(1, payment.getPayrollId());
            insertStmt.setString(2, payment.getEmployeeId());
            insertStmt.setString(3, payment.getPaymentDate());
            insertStmt.setBigDecimal(4, payment.getAmount());
            insertStmt.setString(5, payment.getPaymentMethod());
            insertStmt.setString(6, payment.getTransactionReference());
            insertStmt.setString(7, payment.getStatus());
            insertStmt.setString(8, payment.getNotes());
            insertStmt.executeUpdate();

            try (ResultSet gk = insertStmt.getGeneratedKeys()) {
                if (gk.next()) {
                    payment.setPaymentId(gk.getInt(1));
                }
            }

            // Update Payroll status in the same transaction
            updateStmt = conn.prepareStatement(updatePayrollSql);
            updateStmt.setInt(1, payment.getPayrollId());
            updateStmt.executeUpdate();

            conn.commit(); // Commit Transaction
            return true;
        } catch (SQLException ex) {
            DBConnection.rollbackQuietly(conn);
            throw ex;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException ignored) {}
            DBConnection.closeQuietly(insertStmt, updateStmt, conn);
        }
    }

    @Override
    public boolean update(Payment payment) throws SQLException {
        String sql = "UPDATE payments SET payment_date = ?, amount = ?, payment_method = ?, " +
                     "transaction_reference = ?, status = ?, notes = ? WHERE payment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, payment.getPaymentDate());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getTransactionReference());
            stmt.setString(5, payment.getStatus());
            stmt.setString(6, payment.getNotes());
            stmt.setInt(7, payment.getPaymentId());

            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public List<Payment> findRecent(int limit) throws SQLException {
        String sql = "SELECT pay.*, pr.salary_month, e.first_name, e.last_name, d.department_name " +
                     "FROM payments pay " +
                     "JOIN payroll pr ON pay.payroll_id = pr.payroll_id " +
                     "JOIN employees e ON pay.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "ORDER BY pay.payment_date DESC, pay.payment_id DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Payment> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
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
    public BigDecimal getTotalDisbursed() throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE status = 'Success'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }
}
