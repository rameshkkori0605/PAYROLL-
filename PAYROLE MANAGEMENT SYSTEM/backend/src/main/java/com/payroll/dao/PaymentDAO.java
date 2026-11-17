package com.payroll.dao;

import com.payroll.model.Payment;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PaymentDAO {
    List<Payment> findAll(String month, String method, String status) throws SQLException;
    Payment findById(int paymentId) throws SQLException;
    Payment findByPayrollId(int payrollId) throws SQLException;
    boolean recordPayment(Payment payment) throws SQLException;
    boolean update(Payment payment) throws SQLException;
    List<Payment> findRecent(int limit) throws SQLException;
    BigDecimal getTotalDisbursed() throws SQLException;
}
