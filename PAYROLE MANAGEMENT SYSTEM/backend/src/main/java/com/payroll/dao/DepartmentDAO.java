package com.payroll.dao;

import com.payroll.model.Department;
import java.sql.SQLException;
import java.util.List;

public interface DepartmentDAO {
    List<Department> findAll() throws SQLException;
    Department findById(int departmentId) throws SQLException;
    boolean save(Department department) throws SQLException;
}
