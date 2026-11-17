package com.payroll.controller;

import com.payroll.dao.DepartmentDAO;
import com.payroll.dao.impl.DepartmentDAOImpl;
import com.payroll.model.Department;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "DepartmentServlet", urlPatterns = {"/api/departments/*"})
public class DepartmentServlet extends HttpServlet {

    private final DepartmentDAO departmentDAO = new DepartmentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Department> list = departmentDAO.findAll();
            JsonUtil.sendSuccess(response, list, "Departments loaded successfully");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Department dept = JsonUtil.fromJson(request, Department.class);
            if (dept == null || dept.getDepartmentName() == null || dept.getDepartmentName().trim().isEmpty()) {
                JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Department name is required");
                return;
            }
            boolean saved = departmentDAO.save(dept);
            if (saved) {
                JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED, true, "Department added successfully", dept);
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create department");
            }
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
