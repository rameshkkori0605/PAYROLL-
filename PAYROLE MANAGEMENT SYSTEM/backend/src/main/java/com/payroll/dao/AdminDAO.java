package com.payroll.dao;

import com.payroll.model.Admin;
import java.sql.SQLException;

/**
 * AdminDAO - Data Access Object interface for Administrator authentication and records.
 */
public interface AdminDAO {
    Admin findByUsername(String username) throws SQLException;
    Admin findById(int adminId) throws SQLException;
    boolean updatePassword(int adminId, String newPasswordHash, String newSalt) throws SQLException;
}
