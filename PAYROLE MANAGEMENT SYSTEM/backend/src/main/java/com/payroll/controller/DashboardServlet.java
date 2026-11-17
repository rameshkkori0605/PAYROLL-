package com.payroll.controller;

import com.payroll.service.DashboardService;
import com.payroll.service.impl.DashboardServiceImpl;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/api/dashboard/*"})
public class DashboardServlet extends HttpServlet {

    private final DashboardService dashboardService = new DashboardServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Map<String, Object> stats = dashboardService.getDashboardStats();
            JsonUtil.sendSuccess(response, stats, "Dashboard statistics loaded successfully");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error loading dashboard: " + e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error: " + e.getMessage());
        }
    }
}
