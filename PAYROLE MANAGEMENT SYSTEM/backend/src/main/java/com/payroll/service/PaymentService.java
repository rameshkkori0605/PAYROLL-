package com.payroll.service;

import com.payroll.model.Payment;
import java.sql.SQLException;
import java.util.List;

public interface PaymentService {
    List<Payment> getPayments(String month, String method, String status) throws SQLException;
    Payment getPaymentById(int paymentId) throws SQLException;
    Payment getPaymentByPayrollId(int payrollId) throws SQLException;
    boolean recordPayment(Payment payment) throws SQLException;
    boolean updatePayment(Payment payment) throws SQLException;
    List<Payment> getRecentPayments(int limit) throws SQLException;
}
