package com.payroll.dao;

import java.sql.SQLException;
import java.util.Map;

public interface DashboardDAO {
    Map<String, Object> getDashboardStatistics() throws SQLException;
}
