package com.payroll.dao.impl;

import com.payroll.dao.AttendanceDAO;
import com.payroll.model.Attendance;
import com.payroll.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDAOImpl implements AttendanceDAO {

    private Attendance mapRow(ResultSet rs) throws SQLException {
        Attendance att = new Attendance();
        att.setAttendanceId(rs.getInt("attendance_id"));
        att.setEmployeeId(rs.getString("employee_id"));
        att.setEmployeeName(rs.getString("first_name") + " " + rs.getString("last_name"));
        att.setDepartmentName(rs.getString("department_name"));
        att.setAttendanceDate(rs.getString("attendance_date"));
        att.setStatus(rs.getString("status"));
        att.setRemarks(rs.getString("remarks"));
        att.setCreatedAt(rs.getTimestamp("created_at"));
        return att;
    }

    @Override
    public List<Attendance> findByDate(String date) throws SQLException {
        String sql = "SELECT a.*, e.first_name, e.last_name, d.department_name " +
                     "FROM attendance a " +
                     "JOIN employees e ON a.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE a.attendance_date = ? " +
                     "ORDER BY e.employee_id ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Attendance> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
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
    public List<Attendance> findByEmployeeAndMonth(String employeeId, String yearMonth) throws SQLException {
        String sql = "SELECT a.*, e.first_name, e.last_name, d.department_name " +
                     "FROM attendance a " +
                     "JOIN employees e ON a.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE a.employee_id = ? AND a.attendance_date LIKE ? " +
                     "ORDER BY a.attendance_date ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Attendance> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            stmt.setString(2, yearMonth + "%");
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
    public List<Attendance> findRecent(int limit) throws SQLException {
        String sql = "SELECT a.*, e.first_name, e.last_name, d.department_name " +
                     "FROM attendance a " +
                     "JOIN employees e ON a.employee_id = e.employee_id " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "ORDER BY a.attendance_date DESC, a.attendance_id DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Attendance> list = new ArrayList<>();

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
    public boolean save(Attendance att) throws SQLException {
        String sql = "INSERT INTO attendance (employee_id, attendance_date, status, remarks) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE status = VALUES(status), remarks = VALUES(remarks)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, att.getEmployeeId());
            stmt.setString(2, att.getAttendanceDate());
            stmt.setString(3, att.getStatus());
            stmt.setString(4, att.getRemarks());
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean update(Attendance att) throws SQLException {
        String sql = "UPDATE attendance SET status = ?, remarks = ? WHERE attendance_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, att.getStatus());
            stmt.setString(2, att.getRemarks());
            stmt.setInt(3, att.getAttendanceId());
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean exists(String employeeId, String date) throws SQLException {
        String sql = "SELECT 1 FROM attendance WHERE employee_id = ? AND attendance_date = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            stmt.setString(2, date);
            rs = stmt.executeQuery();
            return rs.next();
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public Map<String, Object> getMonthlyAttendanceSummary(String employeeId, String yearMonth) throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM attendance " +
                     "WHERE employee_id = ? AND attendance_date LIKE ? " +
                     "GROUP BY status";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Object> summary = new HashMap<>();

        double present = 0;
        double halfDay = 0;
        double leave = 0;
        double absent = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            stmt.setString(2, yearMonth + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String st = rs.getString("status");
                int count = rs.getInt("cnt");
                if ("Present".equalsIgnoreCase(st)) present += count;
                else if ("Half Day".equalsIgnoreCase(st)) halfDay += count;
                else if ("Leave".equalsIgnoreCase(st)) leave += count;
                else if ("Absent".equalsIgnoreCase(st)) absent += count;
            }

            // Effective present days = full present + half-day * 0.5
            double effectivePresent = present + (halfDay * 0.5);
            double effectiveUnpaidAbsent = absent + (halfDay * 0.5);

            summary.put("fullPresentCount", present);
            summary.put("halfDayCount", halfDay);
            summary.put("effectivePresentDays", effectivePresent);
            summary.put("paidLeaves", leave);
            summary.put("unpaidAbsentDays", effectiveUnpaidAbsent);

            return summary;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public Map<String, Integer> getDailyStatusCounts(String date) throws SQLException {
        String sql = "SELECT status, COUNT(*) AS cnt FROM attendance WHERE attendance_date = ? GROUP BY status";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Integer> map = new HashMap<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, date);
            rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("status"), rs.getInt("cnt"));
            }
            return map;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }
}
