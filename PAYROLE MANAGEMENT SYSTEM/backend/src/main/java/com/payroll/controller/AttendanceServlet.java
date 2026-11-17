package com.payroll.controller;

import com.payroll.model.Attendance;
import com.payroll.service.AttendanceService;
import com.payroll.service.impl.AttendanceServiceImpl;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AttendanceServlet", urlPatterns = {"/api/attendance/*"})
public class AttendanceServlet extends HttpServlet {

    private final AttendanceService attendanceService = new AttendanceServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if ("/summary".equalsIgnoreCase(pathInfo)) {
                String empId = request.getParameter("employeeId");
                String month = request.getParameter("month");
                if (empId == null || month == null) {
                    JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Parameters 'employeeId' and 'month' are required");
                    return;
                }
                Map<String, Object> summary = attendanceService.getMonthlyAttendanceSummary(empId, month);
                JsonUtil.sendSuccess(response, summary, "Attendance summary computed");
            } else {
                String date = request.getParameter("date");
                String empId = request.getParameter("employeeId");
                String month = request.getParameter("month");

                if (empId != null && month != null) {
                    List<Attendance> list = attendanceService.getAttendanceByEmployeeAndMonth(empId, month);
                    JsonUtil.sendSuccess(response, list, "Employee monthly attendance records");
                } else {
                    if (date == null || date.trim().isEmpty()) {
                        date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
                    }
                    List<Attendance> list = attendanceService.getAttendanceByDate(date);
                    Map<String, Integer> counts = attendanceService.getDailyStatusCounts(date);

                    Map<String, Object> result = new HashMap<>();
                    result.put("date", date);
                    result.put("records", list);
                    result.put("counts", counts);

                    JsonUtil.sendSuccess(response, result, "Daily attendance loaded for " + date);
                }
            }
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Attendance att = JsonUtil.fromJson(request, Attendance.class);
            if (att == null) {
                JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Attendance payload cannot be null");
                return;
            }

            boolean saved = attendanceService.recordAttendance(att);
            if (saved) {
                JsonUtil.sendSuccess(response, att, "Attendance recorded successfully for " + att.getEmployeeId());
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to record attendance");
            }
        } catch (IllegalArgumentException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        }
    }
}
