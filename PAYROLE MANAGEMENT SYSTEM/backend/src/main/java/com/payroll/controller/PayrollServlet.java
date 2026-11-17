package com.payroll.controller;

import com.google.gson.JsonObject;
import com.payroll.model.Payroll;
import com.payroll.service.PayrollService;
import com.payroll.service.impl.PayrollServiceImpl;
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

@WebServlet(name = "PayrollServlet", urlPatterns = {"/api/payroll/*"})
public class PayrollServlet extends HttpServlet {

    private final PayrollService payrollService = new PayrollServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
                String month = request.getParameter("month");
                String status = request.getParameter("status");
                String deptStr = request.getParameter("departmentId");

                Integer deptId = null;
                if (deptStr != null && !deptStr.trim().isEmpty()) {
                    try {
                        deptId = Integer.parseInt(deptStr.trim());
                    } catch (NumberFormatException ignored) {}
                }

                List<Payroll> list = payrollService.getPayrolls(month, status, deptId);
                JsonUtil.sendSuccess(response, list, "Payroll records loaded successfully");
            } else {
                String idStr = pathInfo.substring(1);
                int id = Integer.parseInt(idStr);
                Payroll p = payrollService.getPayrollById(id);
                if (p != null) {
                    JsonUtil.sendSuccess(response, p, "Payroll record loaded");
                } else {
                    JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Payroll record not found for ID: " + id);
                }
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid payroll ID format");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if ("/calculate".equalsIgnoreCase(pathInfo)) {
                // Live preview of salary calculation
                JsonObject json = JsonUtil.fromJson(request, JsonObject.class);
                String empId = json.get("employeeId").getAsString();
                String month = json.get("salaryMonth").getAsString();

                BigDecimal bonus = json.has("bonus") && !json.get("bonus").isJsonNull()
                        ? json.get("bonus").getAsBigDecimal() : BigDecimal.ZERO;
                BigDecimal overtime = json.has("overtimePay") && !json.get("overtimePay").isJsonNull()
                        ? json.get("overtimePay").getAsBigDecimal() : BigDecimal.ZERO;
                BigDecimal otherAllow = json.has("otherAllowances") && !json.get("otherAllowances").isJsonNull()
                        ? json.get("otherAllowances").getAsBigDecimal() : BigDecimal.ZERO;
                BigDecimal otherDeduct = json.has("otherDeductions") && !json.get("otherDeductions").isJsonNull()
                        ? json.get("otherDeductions").getAsBigDecimal() : BigDecimal.ZERO;

                Payroll preview = payrollService.computePayroll(empId, month, bonus, overtime, otherAllow, otherDeduct);
                JsonUtil.sendSuccess(response, preview, "Salary calculation preview computed");
            } else if ("/generate".equalsIgnoreCase(pathInfo) || "/process".equalsIgnoreCase(pathInfo)) {
                // Generate and save payroll
                JsonObject json = JsonUtil.fromJson(request, JsonObject.class);
                String empId = json.get("employeeId").getAsString();
                String month = json.get("salaryMonth").getAsString();

                BigDecimal bonus = json.has("bonus") && !json.get("bonus").isJsonNull()
                        ? json.get("bonus").getAsBigDecimal() : BigDecimal.ZERO;
                BigDecimal overtime = json.has("overtimePay") && !json.get("overtimePay").isJsonNull()
                        ? json.get("overtimePay").getAsBigDecimal() : BigDecimal.ZERO;
                BigDecimal otherAllow = json.has("otherAllowances") && !json.get("otherAllowances").isJsonNull()
                        ? json.get("otherAllowances").getAsBigDecimal() : BigDecimal.ZERO;
                BigDecimal otherDeduct = json.has("otherDeductions") && !json.get("otherDeductions").isJsonNull()
                        ? json.get("otherDeductions").getAsBigDecimal() : BigDecimal.ZERO;

                Payroll p = payrollService.generateAndSavePayroll(empId, month, bonus, overtime, otherAllow, otherDeduct);
                JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED, true,
                        "Payroll generated and saved for " + p.getEmployeeName() + " (" + p.getSalaryMonth() + ")", p);
            } else if ("/process-batch".equalsIgnoreCase(pathInfo)) {
                // Batch processing for entire company
                JsonObject json = JsonUtil.fromJson(request, JsonObject.class);
                String month = json.get("salaryMonth").getAsString();
                int processed = payrollService.processBatchPayroll(month);

                Map<String, Object> res = new HashMap<>();
                res.put("salaryMonth", month);
                res.put("generatedCount", processed);
                JsonUtil.sendSuccess(response, res, "Batch payroll processed successfully. Created " + processed + " payroll records.");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Unknown payroll endpoint action");
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
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Payroll ID must be provided in path");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = payrollService.deletePayroll(id);
            if (deleted) {
                JsonUtil.sendSuccess(response, null, "Payroll record " + id + " deleted successfully");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Payroll record not found");
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid payroll ID format");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
