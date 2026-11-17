package com.payroll.dao.impl;

import com.payroll.dao.DepartmentDAO;
import com.payroll.model.Department;
import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOImpl implements DepartmentDAO {

    @Override
    public List<Department> findAll() throws SQLException {
        String sql = "SELECT d.department_id, d.department_name, d.description, d.created_at, " +
                     "COUNT(e.employee_id) AS emp_count " +
                     "FROM departments d " +
                     "LEFT JOIN employees e ON d.department_id = e.department_id " +
                     "GROUP BY d.department_id, d.department_name, d.description, d.created_at " +
                     "ORDER BY d.department_name ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Department> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Department dept = new Department();
                dept.setDepartmentId(rs.getInt("department_id"));
                dept.setDepartmentName(rs.getString("department_name"));
                dept.setDescription(rs.getString("description"));
                dept.setEmployeeCount(rs.getInt("emp_count"));
                dept.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(dept);
            }
            return list;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public Department findById(int departmentId) throws SQLException {
        String sql = "SELECT department_id, department_name, description, created_at FROM departments WHERE department_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, departmentId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Department dept = new Department();
                dept.setDepartmentId(rs.getInt("department_id"));
                dept.setDepartmentName(rs.getString("department_name"));
                dept.setDescription(rs.getString("description"));
                dept.setCreatedAt(rs.getTimestamp("created_at"));
                return dept;
            }
            return null;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public boolean save(Department department) throws SQLException {
        String sql = "INSERT INTO departments (department_name, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, department.getDepartmentName());
            stmt.setString(2, department.getDescription());
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }
}
