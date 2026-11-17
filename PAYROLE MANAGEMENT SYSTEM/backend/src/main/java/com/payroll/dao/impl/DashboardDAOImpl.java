package com.payroll.dao.impl;

import com.payroll.dao.DashboardDAO;
import com.payroll.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAOImpl implements DashboardDAO {

    @Override
    public Map<String, Object> getDashboardStatistics() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // 1. Employee counts
            stmt = conn.prepareStatement(
                    "SELECT COUNT(*) AS total, " +
                    "SUM(CASE WHEN employment_status = 'Active' THEN 1 ELSE 0 END) AS active " +
                    "FROM employees");
            rs = stmt.executeQuery();
            if (rs.next()) {
                stats.put("totalEmployees", rs.getInt("total"));
                stats.put("activeEmployees", rs.getInt("active"));
            }
            DBConnection.closeQuietly(rs, stmt);

            // Current Month
            String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            stats.put("currentMonth", currentMonth);

            // 2. Monthly Payroll Total & Paid Total & Pending count
            // Look for current month; if none exists yet, fall back to the latest month in table
            String targetMonth = currentMonth;
            stmt = conn.prepareStatement("SELECT DISTINCT salary_month FROM payroll ORDER BY salary_month DESC LIMIT 1");
            rs = stmt.executeQuery();
            if (rs.next()) {
                targetMonth = rs.getString("salary_month");
            }
            DBConnection.closeQuietly(rs, stmt);

            stats.put("activePayrollMonth", targetMonth);

            stmt = conn.prepareStatement(
                    "SELECT " +
                    "COALESCE(SUM(net_salary), 0) AS total_payroll, " +
                    "COALESCE(SUM(CASE WHEN payment_status = 'Paid' THEN net_salary ELSE 0 END), 0) AS paid_amount, " +
                    "COALESCE(SUM(CASE WHEN payment_status = 'Pending' THEN net_salary ELSE 0 END), 0) AS pending_amount, " +
                    "SUM(CASE WHEN payment_status = 'Pending' THEN 1 ELSE 0 END) AS pending_count " +
                    "FROM payroll WHERE salary_month = ?");
            stmt.setString(1, targetMonth);
            rs = stmt.executeQuery();
            if (rs.next()) {
                stats.put("totalMonthlyPayroll", rs.getBigDecimal("total_payroll"));
                stats.put("totalPaidSalaries", rs.getBigDecimal("paid_amount"));
                stats.put("pendingAmount", rs.getBigDecimal("pending_amount"));
                stats.put("pendingPaymentsCount", rs.getInt("pending_count"));
            }
            DBConnection.closeQuietly(rs, stmt);

            // 3. Attendance Summary (Today or latest recorded date)
            String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String attDate = today;
            stmt = conn.prepareStatement("SELECT DISTINCT attendance_date FROM attendance ORDER BY attendance_date DESC LIMIT 1");
            rs = stmt.executeQuery();
            if (rs.next()) {
                attDate = rs.getString("attendance_date");
            }
            DBConnection.closeQuietly(rs, stmt);

            stats.put("latestAttendanceDate", attDate);

            stmt = conn.prepareStatement(
                    "SELECT " +
                    "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) AS present_count, " +
                    "SUM(CASE WHEN status = 'Half Day' THEN 1 ELSE 0 END) AS half_day_count, " +
                    "SUM(CASE WHEN status = 'Leave' THEN 1 ELSE 0 END) AS leave_count, " +
                    "SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) AS absent_count " +
                    "FROM attendance WHERE attendance_date = ?");
            stmt.setString(1, attDate);
            rs = stmt.executeQuery();
            Map<String, Integer> attSummary = new HashMap<>();
            if (rs.next()) {
                attSummary.put("present", rs.getInt("present_count"));
                attSummary.put("halfDay", rs.getInt("half_day_count"));
                attSummary.put("leave", rs.getInt("leave_count"));
                attSummary.put("absent", rs.getInt("absent_count"));
            }
            stats.put("attendanceSummary", attSummary);
            DBConnection.closeQuietly(rs, stmt);

            // 4. Department-wise employee breakdown
            stmt = conn.prepareStatement(
                    "SELECT d.department_name, COUNT(e.employee_id) AS emp_count " +
                    "FROM departments d " +
                    "LEFT JOIN employees e ON d.department_id = e.department_id " +
                    "GROUP BY d.department_name ORDER BY emp_count DESC");
            rs = stmt.executeQuery();
            List<Map<String, Object>> deptStats = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> d = new HashMap<>();
                d.put("department", rs.getString("department_name"));
                d.put("count", rs.getInt("emp_count"));
                deptStats.add(d);
            }
            stats.put("departmentStats", deptStats);
            DBConnection.closeQuietly(rs, stmt);

            // 5. Monthly Payroll History Trend (Past 6 months)
            stmt = conn.prepareStatement(
                    "SELECT salary_month, SUM(gross_salary) AS gross, SUM(net_salary) AS net, COUNT(payroll_id) AS cnt " +
                    "FROM payroll GROUP BY salary_month ORDER BY salary_month ASC LIMIT 6");
            rs = stmt.executeQuery();
            List<Map<String, Object>> trend = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> t = new HashMap<>();
                t.put("month", rs.getString("salary_month"));
                t.put("gross", rs.getBigDecimal("gross"));
                t.put("net", rs.getBigDecimal("net"));
                t.put("employeeCount", rs.getInt("cnt"));
                trend.add(t);
            }
            stats.put("payrollTrend", trend);
            DBConnection.closeQuietly(rs, stmt);

            // 6. Recent Activities (recent payrolls and payments)
            stmt = conn.prepareStatement(
                    "SELECT pay.payment_id, pay.amount, pay.payment_date, pay.payment_method, " +
                    "e.first_name, e.last_name, pr.salary_month " +
                    "FROM payments pay " +
                    "JOIN payroll pr ON pay.payroll_id = pr.payroll_id " +
                    "JOIN employees e ON pay.employee_id = e.employee_id " +
                    "ORDER BY pay.payment_date DESC, pay.payment_id DESC LIMIT 5");
            rs = stmt.executeQuery();
            List<Map<String, Object>> recentActivities = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> a = new HashMap<>();
                a.put("title", "Salary Disbursed to " + rs.getString("first_name") + " " + rs.getString("last_name"));
                a.put("subtitle", "Month: " + rs.getString("salary_month") + " via " + rs.getString("payment_method"));
                a.put("amount", rs.getBigDecimal("amount"));
                a.put("date", rs.getString("payment_date"));
                recentActivities.add(a);
            }
            stats.put("recentActivities", recentActivities);

            return stats;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }
}
