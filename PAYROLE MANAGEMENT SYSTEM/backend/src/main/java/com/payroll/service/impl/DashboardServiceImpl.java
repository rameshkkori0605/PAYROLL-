package com.payroll.service.impl;

import com.payroll.dao.DashboardDAO;
import com.payroll.dao.impl.DashboardDAOImpl;
import com.payroll.service.DashboardService;

import java.sql.SQLException;
import java.util.Map;

public class DashboardServiceImpl implements DashboardService {

    private final DashboardDAO dashboardDAO;

    public DashboardServiceImpl() {
        this.dashboardDAO = new DashboardDAOImpl();
    }

    public DashboardServiceImpl(DashboardDAO dashboardDAO) {
        this.dashboardDAO = dashboardDAO;
    }

    @Override
    public Map<String, Object> getDashboardStats() throws SQLException {
        return dashboardDAO.getDashboardStatistics();
    }
}
