package com.payroll.dao.impl;

import com.payroll.dao.CompanySettingDAO;
import com.payroll.model.CompanySetting;
import com.payroll.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanySettingDAOImpl implements CompanySettingDAO {

    @Override
    public CompanySetting getSettings() throws SQLException {
        String sql = "SELECT * FROM company_settings LIMIT 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                CompanySetting s = new CompanySetting();
                s.setSettingId(rs.getInt("setting_id"));
                s.setCompanyName(rs.getString("company_name"));
                s.setCompanyAddress(rs.getString("company_address"));
                s.setCompanyEmail(rs.getString("company_email"));
                s.setCompanyPhone(rs.getString("company_phone"));
                s.setCurrencySymbol(rs.getString("currency_symbol"));
                s.setStandardWorkingDays(rs.getInt("standard_working_days"));
                s.setDefaultHraPct(rs.getBigDecimal("default_hra_pct"));
                s.setDefaultDaPct(rs.getBigDecimal("default_da_pct"));
                s.setDefaultPfPct(rs.getBigDecimal("default_pf_pct"));
                s.setDefaultPtax(rs.getBigDecimal("default_ptax"));
                return s;
            }
            // Fallback default
            CompanySetting fallback = new CompanySetting();
            fallback.setCompanyName("Payroll Management System");
            fallback.setCompanyAddress("Corporate Headquarters, Tech Park");
            fallback.setCompanyEmail("info@payroll.com");
            fallback.setCompanyPhone("+91 80 1234 5678");
            return fallback;
        } finally {
            DBConnection.closeQuietly(rs, stmt, conn);
        }
    }

    @Override
    public boolean updateSettings(CompanySetting settings) throws SQLException {
        String sql = "UPDATE company_settings SET company_name = ?, company_address = ?, " +
                     "company_email = ?, company_phone = ?, currency_symbol = ?, " +
                     "standard_working_days = ?, default_hra_pct = ?, default_da_pct = ?, " +
                     "default_pf_pct = ?, default_ptax = ? WHERE setting_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, settings.getCompanyName());
            stmt.setString(2, settings.getCompanyAddress());
            stmt.setString(3, settings.getCompanyEmail());
            stmt.setString(4, settings.getCompanyPhone());
            stmt.setString(5, settings.getCurrencySymbol());
            stmt.setInt(6, settings.getStandardWorkingDays());
            stmt.setBigDecimal(7, settings.getDefaultHraPct());
            stmt.setBigDecimal(8, settings.getDefaultDaPct());
            stmt.setBigDecimal(9, settings.getDefaultPfPct());
            stmt.setBigDecimal(10, settings.getDefaultPtax());
            stmt.setInt(11, settings.getSettingId());
            return stmt.executeUpdate() > 0;
        } finally {
            DBConnection.closeQuietly(stmt, conn);
        }
    }
}
