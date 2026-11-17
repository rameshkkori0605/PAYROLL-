package com.payroll.controller;

import com.payroll.model.Payment;
import com.payroll.service.PaymentService;
import com.payroll.service.impl.PaymentServiceImpl;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/api/payments/*"})
public class PaymentServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
                String month = request.getParameter("month");
                String method = request.getParameter("method");
                String status = request.getParameter("status");

                List<Payment> list = paymentService.getPayments(month, method, status);
                JsonUtil.sendSuccess(response, list, "Payments loaded successfully");
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Payment payment = paymentService.getPaymentById(id);
                if (payment != null) {
                    JsonUtil.sendSuccess(response, payment, "Payment loaded");
                } else {
                    JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Payment record not found for ID: " + id);
                }
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID format");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Payment payment = JsonUtil.fromJson(request, Payment.class);
            if (payment == null) {
                JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Payment payload is required");
                return;
            }

            boolean saved = paymentService.recordPayment(payment);
            if (saved) {
                JsonUtil.sendJsonResponse(response, HttpServletResponse.SC_CREATED, true,
                        "Payment of ₹" + payment.getAmount() + " recorded successfully. Payroll marked as Paid.", payment);
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to record payment");
            }
        } catch (IllegalArgumentException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Payment ID must be provided in path");
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Payment payment = JsonUtil.fromJson(request, Payment.class);
            payment.setPaymentId(id);

            boolean updated = paymentService.updatePayment(payment);
            if (updated) {
                JsonUtil.sendSuccess(response, payment, "Payment record updated successfully");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Payment record not found");
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid payment ID format");
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
