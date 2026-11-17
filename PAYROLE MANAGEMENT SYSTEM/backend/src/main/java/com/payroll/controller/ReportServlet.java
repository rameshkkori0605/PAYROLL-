package com.payroll.controller;

import com.payroll.dao.PayrollDAO;
import com.payroll.dao.PaymentDAO;
import com.payroll.dao.EmployeeDAO;
import com.payroll.dao.impl.PayrollDAOImpl;
import com.payroll.dao.impl.PaymentDAOImpl;
import com.payroll.dao.impl.EmployeeDAOImpl;
import com.payroll.model.Payroll;
import com.payroll.model.Payment;
import com.payroll.model.Employee;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ReportServlet", urlPatterns = {"/api/reports/*"})
public class ReportServlet extends HttpServlet {

    private final PayrollDAO payrollDAO = new PayrollDAOImpl();
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final EmployeeDAO employeeDAO = new EmployeeDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if ("/payroll".equalsIgnoreCase(pathInfo)) {
                String month = request.getParameter("month");
                String status = request.getParameter("status");
                String deptStr = request.getParameter("departmentId");
                Integer deptId = (deptStr != null && !deptStr.isEmpty()) ? Integer.parseInt(deptStr) : null;

                List<Payroll> list = payrollDAO.findAll(month, status, deptId);
                JsonUtil.sendSuccess(response, list, "Payroll report data loaded");
            } else if ("/payments".equalsIgnoreCase(pathInfo)) {
                String month = request.getParameter("month");
                String method = request.getParameter("method");
                String status = request.getParameter("status");

                List<Payment> list = paymentDAO.findAll(month, method, status);
                JsonUtil.sendSuccess(response, list, "Payment history report data loaded");
            } else if ("/employees".equalsIgnoreCase(pathInfo)) {
                String deptStr = request.getParameter("departmentId");
                String status = request.getParameter("status");
                Integer deptId = (deptStr != null && !deptStr.isEmpty()) ? Integer.parseInt(deptStr) : null;

                List<Employee> list = employeeDAO.search(null, deptId, status);
                JsonUtil.sendSuccess(response, list, "Employee report data loaded");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Unknown report type");
            }
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
