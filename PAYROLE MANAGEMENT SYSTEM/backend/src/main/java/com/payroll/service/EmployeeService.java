package com.payroll.service;

import com.payroll.model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeService {
    // Polymorphism: Method Overloading
    List<Employee> getAllEmployees() throws SQLException;
    List<Employee> getEmployees(String searchQuery) throws SQLException;
    List<Employee> getEmployees(Integer departmentId, String status) throws SQLException;
    List<Employee> getEmployees(String searchQuery, Integer departmentId, String status) throws SQLException;

    Employee getEmployeeById(String employeeId) throws SQLException;
    boolean createEmployee(Employee employee) throws SQLException;
    boolean updateEmployee(Employee employee) throws SQLException;
    boolean deleteEmployee(String employeeId) throws SQLException;
    String getNextEmployeeId() throws SQLException;
    void validateEmployee(Employee employee, boolean isUpdate) throws IllegalArgumentException, SQLException;
}
