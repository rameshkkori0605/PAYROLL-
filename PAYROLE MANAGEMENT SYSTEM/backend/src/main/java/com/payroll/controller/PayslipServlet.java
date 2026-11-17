package com.payroll.controller;

import com.payroll.dao.CompanySettingDAO;
import com.payroll.dao.impl.CompanySettingDAOImpl;
import com.payroll.model.CompanySetting;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PayslipServlet", urlPatterns = {"/api/payslips/*"})
public class PayslipServlet extends HttpServlet {

    private final PayrollService payrollService = new PayrollServiceImpl();
    private final CompanySettingDAO companySettingDAO = new CompanySettingDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Payroll ID must be specified in the URL: /api/payslips/{id}");
            return;
        }

        try {
            int payrollId = Integer.parseInt(pathInfo.substring(1));
            Payroll payroll = payrollService.getPayrollById(payrollId);
            if (payroll == null) {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Payroll record not found for ID: " + payrollId);
                return;
            }

            CompanySetting company = companySettingDAO.getSettings();

            Map<String, Object> payslipData = new HashMap<>();
            payslipData.put("company", company);
            payslipData.put("payroll", payroll);

            JsonUtil.sendSuccess(response, payslipData, "Payslip generated successfully for " + payroll.getEmployeeName());
        } catch (NumberFormatException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid payroll ID format");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
