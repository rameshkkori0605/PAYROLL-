package com.payroll.dao;

import com.payroll.model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    List<Employee> findAll() throws SQLException;
    Employee findById(String employeeId) throws SQLException;
    List<Employee> search(String query, Integer departmentId, String status) throws SQLException;
    boolean insert(Employee employee) throws SQLException;
    boolean update(Employee employee) throws SQLException;
    boolean delete(String employeeId) throws SQLException;
    String generateNextEmployeeId() throws SQLException;
    int countTotal() throws SQLException;
    int countActive() throws SQLException;
    boolean existsByEmail(String email, String excludeEmpId) throws SQLException;
}
