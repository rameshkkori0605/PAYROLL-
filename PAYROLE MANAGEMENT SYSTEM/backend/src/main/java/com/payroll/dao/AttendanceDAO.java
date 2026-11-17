package com.payroll.dao;

import com.payroll.model.Attendance;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AttendanceDAO {
    List<Attendance> findByDate(String date) throws SQLException;
    List<Attendance> findByEmployeeAndMonth(String employeeId, String yearMonth) throws SQLException;
    List<Attendance> findRecent(int limit) throws SQLException;
    boolean save(Attendance attendance) throws SQLException;
    boolean update(Attendance attendance) throws SQLException;
    boolean exists(String employeeId, String date) throws SQLException;
    Map<String, Object> getMonthlyAttendanceSummary(String employeeId, String yearMonth) throws SQLException;
    Map<String, Integer> getDailyStatusCounts(String date) throws SQLException;
}
