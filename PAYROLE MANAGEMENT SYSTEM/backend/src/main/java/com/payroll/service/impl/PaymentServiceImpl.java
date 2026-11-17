package com.payroll.service.impl;

import com.payroll.dao.PaymentDAO;
import com.payroll.dao.PayrollDAO;
import com.payroll.dao.impl.PaymentDAOImpl;
import com.payroll.dao.impl.PayrollDAOImpl;
import com.payroll.model.Payment;
import com.payroll.model.Payroll;
import com.payroll.service.PaymentService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final PayrollDAO payrollDAO;

    public PaymentServiceImpl() {
        this.paymentDAO = new PaymentDAOImpl();
        this.payrollDAO = new PayrollDAOImpl();
    }

    public PaymentServiceImpl(PaymentDAO paymentDAO, PayrollDAO payrollDAO) {
        this.paymentDAO = paymentDAO;
        this.payrollDAO = payrollDAO;
    }

    @Override
    public List<Payment> getPayments(String month, String method, String status) throws SQLException {
        return paymentDAO.findAll(month, method, status);
    }

    @Override
    public Payment getPaymentById(int paymentId) throws SQLException {
        return paymentDAO.findById(paymentId);
    }

    @Override
    public Payment getPaymentByPayrollId(int payrollId) throws SQLException {
        return paymentDAO.findByPayrollId(payrollId);
    }

    @Override
    public boolean recordPayment(Payment payment) throws SQLException {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        if (payment.getPayrollId() <= 0) {
            throw new IllegalArgumentException("Valid Payroll ID is required");
        }
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (payment.getPaymentDate() == null || payment.getPaymentDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment date is required");
        }
        if (payment.getTransactionReference() == null || payment.getTransactionReference().trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction reference is required");
        }

        // Verify payroll exists
        Payroll p = payrollDAO.findById(payment.getPayrollId());
        if (p == null) {
            throw new IllegalArgumentException("Payroll record not found for ID: " + payment.getPayrollId());
        }

        payment.setEmployeeId(p.getEmployeeId());
        return paymentDAO.recordPayment(payment);
    }

    @Override
    public boolean updatePayment(Payment payment) throws SQLException {
        if (payment == null || payment.getPaymentId() <= 0) {
            throw new IllegalArgumentException("Valid Payment ID is required");
        }
        return paymentDAO.update(payment);
    }

    @Override
    public List<Payment> getRecentPayments(int limit) throws SQLException {
        return paymentDAO.findRecent(limit);
    }
}
