package com.payroll.service.impl;

import com.payroll.dao.*;
import com.payroll.dao.impl.*;
import com.payroll.model.CompanySetting;
import com.payroll.model.Employee;
import com.payroll.model.Payroll;
import com.payroll.service.PayrollCalculator;
import com.payroll.service.PayrollService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PayrollServiceImpl implements PayrollService {

    private final PayrollDAO payrollDAO;
    private final EmployeeDAO employeeDAO;
    private final AttendanceDAO attendanceDAO;
    private final CompanySettingDAO companySettingDAO;
    private final PayrollCalculator calculator;

    public PayrollServiceImpl() {
        this.payrollDAO = new PayrollDAOImpl();
        this.employeeDAO = new EmployeeDAOImpl();
        this.attendanceDAO = new AttendanceDAOImpl();
        this.companySettingDAO = new CompanySettingDAOImpl();
        this.calculator = new StandardPayrollCalculator();
    }

    public PayrollServiceImpl(PayrollDAO payrollDAO, EmployeeDAO employeeDAO,
                              AttendanceDAO attendanceDAO, CompanySettingDAO companySettingDAO,
                              PayrollCalculator calculator) {
        this.payrollDAO = payrollDAO;
        this.employeeDAO = employeeDAO;
        this.attendanceDAO = attendanceDAO;
        this.companySettingDAO = companySettingDAO;
        this.calculator = calculator;
    }

    @Override
    public List<Payroll> getPayrolls(String month, String status, Integer departmentId) throws SQLException {
        return payrollDAO.findAll(month, status, departmentId);
    }

    @Override
    public Payroll getPayrollById(int payrollId) throws SQLException {
        if (payrollId <= 0) {
            throw new IllegalArgumentException("Valid Payroll ID is required");
        }
        return payrollDAO.findById(payrollId);
    }

    @Override
    public Payroll computePayroll(String employeeId, String month, BigDecimal bonus, BigDecimal overtime,
                                  BigDecimal otherAllowances, BigDecimal otherDeductions) throws SQLException {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required");
        }
        if (month == null || month.trim().isEmpty()) {
            throw new IllegalArgumentException("Salary Month (YYYY-MM) is required");
        }

        Employee emp = employeeDAO.findById(employeeId.trim());
        if (emp == null) {
            throw new IllegalArgumentException("Employee not found with ID: " + employeeId);
        }

        CompanySetting settings = companySettingDAO.getSettings();
        int workingDays = settings.getStandardWorkingDays() > 0 ? settings.getStandardWorkingDays() : 30;

        // Fetch attendance summary for the given month
        Map<String, Object> attSummary = attendanceDAO.getMonthlyAttendanceSummary(employeeId.trim(), month.trim());
        double presentDays = (double) attSummary.getOrDefault("effectivePresentDays", 0.0);
        double paidLeaves = (double) attSummary.getOrDefault("paidLeaves", 0.0);
        double unpaidAbsents = (double) attSummary.getOrDefault("unpaidAbsentDays", 0.0);

        // If no attendance was marked at all for this month, assume standard full month for testing convenience
        if (presentDays == 0 && paidLeaves == 0 && unpaidAbsents == 0) {
            presentDays = workingDays;
            unpaidAbsents = 0;
        }

        BigDecimal basic = emp.getBasicSalary();
        BigDecimal hra = calculator.calculateHra(basic, settings.getDefaultHraPct());
        BigDecimal da = calculator.calculateDa(basic, settings.getDefaultDaPct());
        BigDecimal b = bonus != null ? bonus : BigDecimal.ZERO;
        BigDecimal ot = overtime != null ? overtime : BigDecimal.ZERO;
        BigDecimal oa = otherAllowances != null ? otherAllowances : BigDecimal.ZERO;

        BigDecimal gross = calculator.calculateGross(basic, hra, da, oa, b, ot);

        BigDecimal pf = calculator.calculatePf(basic, settings.getDefaultPfPct());
        BigDecimal pt = settings.getDefaultPtax() != null ? settings.getDefaultPtax() : new BigDecimal("200.00");
        BigDecimal leaveDeduction = calculator.calculateLeaveDeduction(basic, workingDays, unpaidAbsents);
        BigDecimal incomeTax = calculator.calculateIncomeTax(gross, new BigDecimal("5.00")); // standard 5%
        BigDecimal od = otherDeductions != null ? otherDeductions : BigDecimal.ZERO;

        BigDecimal totalDeductions = calculator.calculateTotalDeductions(pf, pt, incomeTax, leaveDeduction, od);
        BigDecimal net = calculator.calculateNetSalary(gross, totalDeductions);

        Payroll p = new Payroll();
        p.setEmployeeId(emp.getEmployeeId());
        p.setEmployeeName(emp.getFullName());
        p.setDepartmentName(emp.getDepartmentName());
        p.setDesignation(emp.getDesignation());
        p.setBankName(emp.getBankName());
        p.setBankAccountNo(emp.getBankAccountNo());
        p.setBankIfsc(emp.getBankIfsc());
        p.setSalaryMonth(month.trim());
        p.setTotalWorkingDays(workingDays);
        p.setPresentDays(presentDays);
        p.setPaidLeaves(paidLeaves);
        p.setUnpaidAbsentDays(unpaidAbsents);

        p.setBasicSalary(basic);
        p.setHra(hra);
        p.setDa(da);
        p.setOtherAllowances(oa);
        p.setBonus(b);
        p.setOvertimePay(ot);
        p.setGrossSalary(gross);

        p.setPfDeduction(pf);
        p.setProfTax(pt);
        p.setIncomeTax(incomeTax);
        p.setLeaveDeduction(leaveDeduction);
        p.setOtherDeductions(od);
        p.setTotalDeductions(totalDeductions);

        p.setNetSalary(net);
        p.setPaymentStatus("Pending");

        return p;
    }

    @Override
    public Payroll generateAndSavePayroll(String employeeId, String month, BigDecimal bonus, BigDecimal overtime,
                                         BigDecimal otherAllowances, BigDecimal otherDeductions) throws SQLException {
        if (payrollDAO.existsForMonth(employeeId.trim(), month.trim())) {
            throw new IllegalArgumentException("Payroll record already exists for employee " + employeeId + " for month " + month);
        }

        Payroll payroll = computePayroll(employeeId, month, bonus, overtime, otherAllowances, otherDeductions);
        boolean saved = payrollDAO.insert(payroll);
        if (!saved) {
            throw new SQLException("Failed to save payroll record to database");
        }
        return payroll;
    }

    @Override
    public int processBatchPayroll(String month) throws SQLException {
        List<Employee> activeEmployees = employeeDAO.search(null, null, "Active");
        int count = 0;
        for (Employee emp : activeEmployees) {
            if (!payrollDAO.existsForMonth(emp.getEmployeeId(), month.trim())) {
                Payroll p = computePayroll(emp.getEmployeeId(), month, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                if (payrollDAO.insert(p)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public boolean deletePayroll(int payrollId) throws SQLException {
        if (payrollId <= 0) {
            throw new IllegalArgumentException("Valid Payroll ID is required");
        }
        return payrollDAO.delete(payrollId);
    }
}
