package com.payroll.service;

import com.payroll.model.Attendance;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    List<Attendance> getAttendanceByDate(String date) throws SQLException;
    List<Attendance> getAttendanceByEmployeeAndMonth(String employeeId, String yearMonth) throws SQLException;
    boolean recordAttendance(Attendance attendance) throws SQLException;
    Map<String, Object> getMonthlyAttendanceSummary(String employeeId, String yearMonth) throws SQLException;
    Map<String, Integer> getDailyStatusCounts(String date) throws SQLException;
    double calculateAttendancePercentage(double effectivePresentDays, int totalWorkingDays);
}
