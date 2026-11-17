package com.payroll.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    private int attendanceId;
    private String employeeId;
    private String employeeName;    // Populated via JOIN
    private String departmentName;  // Populated via JOIN
    private String attendanceDate;  // 'YYYY-MM-DD'
    private String status;          // 'Present', 'Absent', 'Half Day', 'Leave'
    private String remarks;
    private Timestamp createdAt;

    public Attendance() {}

    public Attendance(String employeeId, String attendanceDate, String status, String remarks) {
        this.employeeId = employeeId;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
