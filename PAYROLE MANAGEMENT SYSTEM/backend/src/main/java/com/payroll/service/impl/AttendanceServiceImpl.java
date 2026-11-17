package com.payroll.service.impl;

import com.payroll.dao.AttendanceDAO;
import com.payroll.dao.impl.AttendanceDAOImpl;
import com.payroll.model.Attendance;
import com.payroll.service.AttendanceService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceDAO attendanceDAO;

    public AttendanceServiceImpl() {
        this.attendanceDAO = new AttendanceDAOImpl();
    }

    public AttendanceServiceImpl(AttendanceDAO attendanceDAO) {
        this.attendanceDAO = attendanceDAO;
    }

    @Override
    public List<Attendance> getAttendanceByDate(String date) throws SQLException {
        if (date == null || date.trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance date cannot be empty");
        }
        return attendanceDAO.findByDate(date.trim());
    }

    @Override
    public List<Attendance> getAttendanceByEmployeeAndMonth(String employeeId, String yearMonth) throws SQLException {
        if (employeeId == null || yearMonth == null) {
            throw new IllegalArgumentException("Employee ID and Month must be specified");
        }
        return attendanceDAO.findByEmployeeAndMonth(employeeId.trim(), yearMonth.trim());
    }

    @Override
    public boolean recordAttendance(Attendance attendance) throws SQLException {
        if (attendance == null) {
            throw new IllegalArgumentException("Attendance cannot be null");
        }
        if (attendance.getEmployeeId() == null || attendance.getEmployeeId().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (attendance.getAttendanceDate() == null || attendance.getAttendanceDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance date is required");
        }
        if (attendance.getStatus() == null || attendance.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance status is required");
        }

        return attendanceDAO.save(attendance);
    }

    @Override
    public Map<String, Object> getMonthlyAttendanceSummary(String employeeId, String yearMonth) throws SQLException {
        Map<String, Object> summary = attendanceDAO.getMonthlyAttendanceSummary(employeeId, yearMonth);
        double effectivePresent = (double) summary.getOrDefault("effectivePresentDays", 0.0);
        int standardWorkingDays = 30; // standard default
        summary.put("attendancePercentage", calculateAttendancePercentage(effectivePresent, standardWorkingDays));
        return summary;
    }

    @Override
    public Map<String, Integer> getDailyStatusCounts(String date) throws SQLException {
        return attendanceDAO.getDailyStatusCounts(date);
    }

    @Override
    public double calculateAttendancePercentage(double effectivePresentDays, int totalWorkingDays) {
        if (totalWorkingDays <= 0) return 0.0;
        BigDecimal present = BigDecimal.valueOf(effectivePresentDays);
        BigDecimal total = BigDecimal.valueOf(totalWorkingDays);
        return present.divide(total, 4, RoundingMode.HALF_UP)
                      .multiply(BigDecimal.valueOf(100))
                      .setScale(2, RoundingMode.HALF_UP)
                      .doubleValue();
    }
}
