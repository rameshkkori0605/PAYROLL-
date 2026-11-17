package com.payroll.service;

import java.sql.SQLException;
import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardStats() throws SQLException;
}
