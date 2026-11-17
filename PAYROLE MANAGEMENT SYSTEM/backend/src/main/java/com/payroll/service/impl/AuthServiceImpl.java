package com.payroll.service.impl;

import com.payroll.dao.AdminDAO;
import com.payroll.dao.impl.AdminDAOImpl;
import com.payroll.model.Admin;
import com.payroll.service.AuthService;
import com.payroll.util.PasswordUtil;

import java.sql.SQLException;

public class AuthServiceImpl implements AuthService {

    private final AdminDAO adminDAO;

    public AuthServiceImpl() {
        this.adminDAO = new AdminDAOImpl();
    }

    public AuthServiceImpl(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override
    public Admin authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }

        Admin admin = adminDAO.findByUsername(username.trim());
        if (admin == null) {
            return null;
        }

        boolean valid = PasswordUtil.verifyPassword(password, admin.getSalt(), admin.getPasswordHash());
        if (valid) {
            // Nullify hash and salt before returning to protect security credentials
            admin.setPasswordHash(null);
            admin.setSalt(null);
            return admin;
        }
        return null;
    }

    @Override
    public boolean changePassword(int adminId, String oldPassword, String newPassword) throws SQLException {
        Admin admin = adminDAO.findById(adminId);
        if (admin == null) return false;

        boolean matches = PasswordUtil.verifyPassword(oldPassword, admin.getSalt(), admin.getPasswordHash());
        if (!matches) return false;

        String newSalt = PasswordUtil.generateSalt();
        String newHash = PasswordUtil.hashPassword(newPassword, newSalt);
        return adminDAO.updatePassword(adminId, newHash, newSalt);
    }
}
