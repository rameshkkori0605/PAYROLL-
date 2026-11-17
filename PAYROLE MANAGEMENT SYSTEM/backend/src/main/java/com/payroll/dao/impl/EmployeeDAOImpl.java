package com.payroll.dao.impl;

import com.payroll.dao.EmployeeDAO;
import com.payroll.model.Employee;
import com.payroll.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmployeeId(rs.getString("employee_id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setEmail(rs.getString("email"));
        emp.setPhone(rs.getString("phone"));
        emp.setGender(rs.getString("gender"));
        emp.setAddress(rs.getString("address"));
        emp.setDepartmentId(rs.getInt("department_id"));
        emp.setDepartmentName(rs.getString("department_name"));
        emp.setDesignation(rs.getString("designation"));
        emp.setJoiningDate(rs.getString("joining_date"));
        emp.setBasicSalary(rs.getBigDecimal("basic_salary"));
        emp.setBankName(rs.getString("bank_name"));
        emp.setBankAccountNo(rs.getString("bank_account_no"));
        emp.setBankIfsc(rs.getString("bank_ifsc"));
        emp.setEmploymentStatus(rs.getString("employment_status"));
        emp.setCreatedAt(rs.getTimestamp("created_at"));
        emp.setUpdatedAt(rs.getTimestamp("updated_at"));
        return emp;
    }

    @Override
    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT e.*, d.department_name " +
                     "FROM employees e " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "ORDER BY e.employee_id ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Employee> list = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
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
    public Employee findById(String employeeId) throws SQLException {
        String sql = "SELECT e.*, d.department_name " +
                     "FROM employees e " +
                     "JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE e.employee_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
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
    public List<Employee> search(String query, Integer departmentId, String status) throws SQLException {
        StringBuilder sql = new StringBuilder(
                "SELECT e.*, d.department_name FROM employees e " +
                "JOIN departments d ON e.department_id = d.department_id WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (e.employee_id LIKE ? OR e.first_name LIKE ? OR e.last_name LIKE ? OR e.email LIKE ? OR e.designation LIKE ?) ");
            String wild = "%" + query.trim() + "%";
            for (int i = 0; i < 5; i++) {
                params.add(wild);
            }
        }

        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
            params.add(departmentId);
        }

        if (status != null && !status.trim().isEmpty() && !status.equalsIgnoreCase("All")) {
            sql.append("AND e.employment_status = ? ");
            params.add(status.trim());
        }

        sql.append("ORDER BY e.employee_id ASC");

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Employee> list = new ArrayList<>();

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
    public boolean insert(Employee emp) throws SQLException {
        String sql = "INSERT INTO employees (employee_id, first_name, last_name, email, phone, gender, " +
                     "address, department_id, designation, joining_date, basic_salary, bank_name, " +
                     "bank_account_no, bank_ifsc, employment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, emp.getEmployeeId());
            stmt.setString(2, emp.getFirstName());
            stmt.setString(3, emp.getLastName());
            stmt.setString(4, emp.getEmail());
            stmt.setString(5, emp.getPhone());
            stmt.setString(6, emp.getGender());
            stmt.setString(7, emp.getAddress());
            stmt.setInt(8, emp.getDepartmentId());
            stmt.setString(9, emp.getDesignation());
            stmt.setString(10, emp.getJoiningDate());
            stmt.setBigDecimal(11, emp.getBasicSalary());
            stmt.setString(12, emp.getBankName());
            stmt.setString(13, emp.getBankAccountNo());
            stmt.setString(14, emp.getBankIfsc());
            stmt.setString(15, emp.getEmploymentStatus());

            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean update(Employee emp) throws SQLException {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone = ?, gender = ?, " +
                     "address = ?, department_id = ?, designation = ?, joining_date = ?, basic_salary = ?, " +
                     "bank_name = ?, bank_account_no = ?, bank_ifsc = ?, employment_status = ? " +
                     "WHERE employee_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, emp.getFirstName());
            stmt.setString(2, emp.getLastName());
            stmt.setString(3, emp.getEmail());
            stmt.setString(4, emp.getPhone());
            stmt.setString(5, emp.getGender());
            stmt.setString(6, emp.getAddress());
            stmt.setInt(7, emp.getDepartmentId());
            stmt.setString(8, emp.getDesignation());
            stmt.setString(9, emp.getJoiningDate());
            stmt.setBigDecimal(10, emp.getBasicSalary());
            stmt.setString(11, emp.getBankName());
            stmt.setString(12, emp.getBankAccountNo());
            stmt.setString(13, emp.getBankIfsc());
            stmt.setString(14, emp.getEmploymentStatus());
            stmt.setString(15, emp.getEmployeeId());

            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public boolean delete(String employeeId) throws SQLException {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, employeeId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }

    @Override
    public String generateNextEmployeeId() throws SQLException {
        String sql = "SELECT employee_id FROM employees ORDER BY employee_id DESC LIMIT 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("employee_id");
                if (lastId != null && lastId.toUpperCase().startsWith("EMP")) {
                    try {
                        int num = Integer.parseInt(lastId.substring(3));
                        return String.format("EMP%04d", num + 1);
                    } catch (NumberFormatException ignored) {}
                }
            }
            return "EMP1001";
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public int countTotal() throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM employees WHERE employment_status = 'Active'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public boolean existsByEmail(String email, String excludeEmpId) throws SQLException {
        String sql = "SELECT 1 FROM employees WHERE email = ? AND (employee_id != ? OR ? IS NULL)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, excludeEmpId != null ? excludeEmpId : "");
            stmt.setString(3, excludeEmpId);
            rs = stmt.executeQuery();
            return rs.next();
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }
}
