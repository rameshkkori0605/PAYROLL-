package com.payroll.service;

import com.payroll.model.Admin;
import java.sql.SQLException;

public interface AuthService {
    Admin authenticate(String username, String password) throws SQLException;
    boolean changePassword(int adminId, String oldPassword, String newPassword) throws SQLException;
}
