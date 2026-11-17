package com.payroll.dao.impl;

import com.payroll.dao.AdminDAO;
import com.payroll.model.Admin;
import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public Admin findByUsername(String username) throws SQLException {
        String sql = "SELECT admin_id, username, password_hash, salt, full_name, email, role, created_at " +
                     "FROM admins WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPasswordHash(rs.getString("password_hash"));
                admin.setSalt(rs.getString("salt"));
                String fullName = rs.getString("full_name");
                if (fullName != null) {
                    int spaceIdx = fullName.indexOf(" ");
                    if (spaceIdx > 0) {
                        admin.setFirstName(fullName.substring(0, spaceIdx));
                        admin.setLastName(fullName.substring(spaceIdx + 1));
                    } else {
                        admin.setFirstName(fullName);
                        admin.setLastName("");
                    }
                }
                admin.setEmail(rs.getString("email"));
                admin.setRole(rs.getString("role"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
                return admin;
            }
            return null;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public Admin findById(int adminId) throws SQLException {
        String sql = "SELECT admin_id, username, password_hash, salt, full_name, email, role, created_at " +
                     "FROM admins WHERE admin_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, adminId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                admin.setPasswordHash(rs.getString("password_hash"));
                admin.setSalt(rs.getString("salt"));
                admin.setEmail(rs.getString("email"));
                admin.setRole(rs.getString("role"));
                admin.setCreatedAt(rs.getTimestamp("created_at"));
                return admin;
            }
            return null;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public boolean updatePassword(int adminId, String newPasswordHash, String newSalt) throws SQLException {
        String sql = "UPDATE admins SET password_hash = ?, salt = ? WHERE admin_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPasswordHash);
            stmt.setString(2, newSalt);
            stmt.setInt(3, adminId);
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }
}
