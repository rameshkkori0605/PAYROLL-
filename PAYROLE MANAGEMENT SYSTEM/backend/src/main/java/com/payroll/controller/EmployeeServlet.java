package com.payroll.controller;

import com.google.gson.JsonObject;
import com.payroll.model.Employee;
import com.payroll.service.EmployeeService;
import com.payroll.service.impl.EmployeeServiceImpl;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/api/employees/*"})
public class EmployeeServlet extends HttpServlet {

    private final EmployeeService employeeService = new EmployeeServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
                // List or Search
                String query = request.getParameter("query");
                String deptStr = request.getParameter("departmentId");
                String status = request.getParameter("status");

                Integer deptId = null;
                if (deptStr != null && !deptStr.trim().isEmpty()) {
                    try {
                        deptId = Integer.parseInt(deptStr.trim());
                    } catch (NumberFormatException ignored) {}
                }

                List<Employee> list = employeeService.getEmployees(query, deptId, status);
                JsonUtil.sendSuccess(response, list, "Employees retrieved successfully");
            } else if (pathInfo.equalsIgnoreCase("/next-id")) {
                String nextId = employeeService.getNextEmployeeId();
                Map<String, String> data = new HashMap<>();
                data.put("nextEmployeeId", nextId);
                JsonUtil.sendSuccess(response, data, "Next employee ID generated");
            } else {
                // Get single employee by ID: e.g. /EMP1001
                String empId = pathInfo.substring(1);
                Employee emp = employeeService.getEmployeeById(empId);
                if (emp != null) {
                    JsonUtil.sendSuccess(response, emp, "Employee profile loaded");
                } else {
                    JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Employee with ID '" + empId + "' not found");
                }
            }
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Support POST /api/employees/{id} as PUT override for clients that prefer POST
        if (pathInfo != null && pathInfo.length() > 1 && !pathInfo.equalsIgnoreCase("/next-id")) {
            doPut(request, response);
            return;
        }

        try {
            Employee emp = JsonUtil.fromJson(request, Employee.class);
            if (emp == null) {
                JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid employee data payload");
                return;
            }

            boolean created = employeeService.createEmployee(emp);
            if (created) {
                JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED, true,
                        "Employee '" + emp.getFullName() + "' created successfully with ID: " + emp.getEmployeeId(), emp);
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to create employee");
            }
        } catch (IllegalArgumentException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Employee ID must be provided in path");
            return;
        }

        String empId = pathInfo.substring(1);
        try {
            Employee emp = JsonUtil.fromJson(request, Employee.class);
            if (emp == null) {
                JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid employee update payload");
                return;
            }

            emp.setEmployeeId(empId);
            boolean updated = employeeService.updateEmployee(emp);
            if (updated) {
                JsonUtil.sendSuccess(response, emp, "Employee '" + emp.getFullName() + "' updated successfully");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Employee not found or no changes made");
            }
        } catch (IllegalArgumentException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Employee ID must be provided in path");
            return;
        }

        String empId = pathInfo.substring(1);
        try {
            boolean deleted = employeeService.deleteEmployee(empId);
            if (deleted) {
                JsonUtil.sendSuccess(response, null, "Employee record '" + empId + "' deleted successfully");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Employee not found");
            }
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot delete employee: dependent payroll or attendance records exist.");
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}
