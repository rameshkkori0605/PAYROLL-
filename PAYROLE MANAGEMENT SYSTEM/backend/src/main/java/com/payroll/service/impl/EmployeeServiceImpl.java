package com.payroll.service.impl;

import com.payroll.dao.EmployeeDAO;
import com.payroll.dao.impl.EmployeeDAOImpl;
import com.payroll.model.Employee;
import com.payroll.service.EmployeeService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public EmployeeServiceImpl() {
        this.employeeDAO = new EmployeeDAOImpl();
    }

    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.findAll();
    }

    @Override
    public List<Employee> getEmployees(String searchQuery) throws SQLException {
        return employeeDAO.search(searchQuery, null, null);
    }

    @Override
    public List<Employee> getEmployees(Integer departmentId, String status) throws SQLException {
        return employeeDAO.search(null, departmentId, status);
    }

    @Override
    public List<Employee> getEmployees(String searchQuery, Integer departmentId, String status) throws SQLException {
        return employeeDAO.search(searchQuery, departmentId, status);
    }

    @Override
    public Employee getEmployeeById(String employeeId) throws SQLException {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }
        return employeeDAO.findById(employeeId.trim());
    }

    @Override
    public boolean createEmployee(Employee employee) throws SQLException {
        validateEmployee(employee, false);
        if (employee.getEmployeeId() == null || employee.getEmployeeId().trim().isEmpty()) {
            employee.setEmployeeId(employeeDAO.generateNextEmployeeId());
        }
        return employeeDAO.insert(employee);
    }

    @Override
    public boolean updateEmployee(Employee employee) throws SQLException {
        validateEmployee(employee, true);
        return employeeDAO.update(employee);
    }

    @Override
    public boolean deleteEmployee(String employeeId) throws SQLException {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be empty");
        }
        return employeeDAO.delete(employeeId.trim());
    }

    @Override
    public String getNextEmployeeId() throws SQLException {
        return employeeDAO.generateNextEmployeeId();
    }

    @Override
    public void validateEmployee(Employee emp, boolean isUpdate) throws IllegalArgumentException, SQLException {
        if (emp == null) {
            throw new IllegalArgumentException("Employee data cannot be null");
        }
        if (emp.getFirstName() == null || emp.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (emp.getLastName() == null || emp.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (emp.getEmail() == null || !EMAIL_PATTERN.matcher(emp.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("Valid corporate email address is required");
        }
        if (emp.getPhone() == null || emp.getPhone().trim().length() < 7) {
            throw new IllegalArgumentException("Valid phone number is required");
        }
        if (emp.getDepartmentId() <= 0) {
            throw new IllegalArgumentException("Department selection is required");
        }
        if (emp.getDesignation() == null || emp.getDesignation().trim().isEmpty()) {
            throw new IllegalArgumentException("Designation is required");
        }
        if (emp.getJoiningDate() == null || emp.getJoiningDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Joining date is required");
        }
        if (emp.getBasicSalary() == null || emp.getBasicSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Basic salary must be greater than zero");
        }
        if (emp.getBankName() == null || emp.getBankName().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank name is required");
        }
        if (emp.getBankAccountNo() == null || emp.getBankAccountNo().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account number is required");
        }
        if (emp.getBankIfsc() == null || emp.getBankIfsc().trim().isEmpty()) {
            throw new IllegalArgumentException("Bank IFSC code is required");
        }

        // Check email uniqueness
        String excludeId = isUpdate ? emp.getEmployeeId() : null;
        if (employeeDAO.existsByEmail(emp.getEmail().trim(), excludeId)) {
            throw new IllegalArgumentException("Email '" + emp.getEmail() + "' is already registered to another employee");
        }
    }
}
