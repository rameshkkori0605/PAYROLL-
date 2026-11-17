package com.payroll.dao;

import com.payroll.model.CompanySetting;
import java.sql.SQLException;

public interface CompanySettingDAO {
    CompanySetting getSettings() throws SQLException;
    boolean updateSettings(CompanySetting settings) throws SQLException;
}
