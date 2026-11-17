/**
 * PAYROLL MANAGEMENT SYSTEM - CLIENT APPLICATION SCRIPT
 * Architecture: Clean Modular Namespace Pattern
 * Dual-Mode: Automatically detects Apache Tomcat Java Servlet backend,
 * with zero-config seamless Fallback Engine for live Vercel / GitHub Pages deployment!
 */

const App = (() => {
  // Determine API base path based on current context
  const getApiBase = () => {
    const path = window.location.pathname;
    if (path.includes('/payroll/')) {
      return '/payroll/api';
    }
    return '/api';
  };

  const API_BASE = getApiBase();

  // ------------------------------------------------------------------
  // CLIENT-SIDE DATABASE ENGINE (FOR VERCEL / OFFLINE RUNTIME)
  // ------------------------------------------------------------------
  const ClientStorage = {
    KEY: 'APEX_PAYROLL_LOCAL_DB_V1',

    getInitialData: () => ({
      admins: [
        {
          adminId: 1,
          username: "admin",
          password: "admin123",
          fullName: "System Administrator",
          email: "admin@payrollapp.com",
          role: "SUPER_ADMIN",
          roleDescription: "SUPER_ADMIN System Administrator"
        },
        {
          adminId: 2,
          username: "hr_admin",
          password: "hr123",
          fullName: "HR Executive Officer",
          email: "hr@payrollapp.com",
          role: "HR_ADMIN",
          roleDescription: "HR_ADMIN System Administrator"
        }
      ],
      departments: [
        { departmentId: 1, departmentName: "Engineering", description: "Software QA & Infrastructure", employeeCount: 3 },
        { departmentId: 2, departmentName: "Human Resources", description: "Talent & Relations", employeeCount: 1 },
        { departmentId: 3, departmentName: "Finance & Accounts", description: "Budgeting & Audits", employeeCount: 1 },
        { departmentId: 4, departmentName: "Marketing & Sales", description: "Outreach & Growth", employeeCount: 1 },
        { departmentId: 5, departmentName: "Operations", description: "Logistics & Facilities", employeeCount: 1 }
      ],
      company: {
        settingId: 1,
        companyName: "Apex Global Solutions Inc.",
        companyAddress: "104 Innovate Boulevard, Tech Hub Park, Sector 5, Bangalore - 560100",
        companyEmail: "payroll@apexsolutions.com",
        companyPhone: "+91 80 4123 4567",
        currencySymbol: "₹",
        standardWorkingDays: 30,
        defaultHraPct: 20.0,
        defaultDaPct: 10.0,
        defaultPfPct: 12.0,
        defaultPtax: 200.0
      },
      employees: [
        {
          employeeId: "EMP1001",
          firstName: "Aarav",
          lastName: "Sharma",
          email: "aarav.sharma@apexsolutions.com",
          phone: "+91 9876543210",
          gender: "Male",
          address: "45 Indiranagar, 100ft Road, Bangalore",
          departmentId: 1,
          departmentName: "Engineering",
          designation: "Senior Software Engineer",
          joiningDate: "2023-03-15",
          basicSalary: 75000.0,
          bankName: "HDFC Bank",
          bankAccountNo: "50100234891102",
          maskedBankAccount: "**********1102",
          bankIfsc: "HDFC0001234",
          employmentStatus: "Active"
        },
        {
          employeeId: "EMP1002",
          firstName: "Priya",
          lastName: "Nair",
          email: "priya.nair@apexsolutions.com",
          phone: "+91 9876543211",
          gender: "Female",
          address: "12 Koramangala 4th Block, Bangalore",
          departmentId: 2,
          departmentName: "Human Resources",
          designation: "HR Business Partner",
          joiningDate: "2023-06-01",
          basicSalary: 58000.0,
          bankName: "ICICI Bank",
          bankAccountNo: "002101569842",
          maskedBankAccount: "********9842",
          bankIfsc: "ICIC0000021",
          employmentStatus: "Active"
        },
        {
          employeeId: "EMP1003",
          firstName: "Rohan",
          lastName: "Verma",
          email: "rohan.verma@apexsolutions.com",
          phone: "+91 9876543212",
          gender: "Male",
          address: "78 Whitefield Main Road, Bangalore",
          departmentId: 1,
          departmentName: "Engineering",
          designation: "Full Stack Developer",
          joiningDate: "2024-01-10",
          basicSalary: 50000.0,
          bankName: "State Bank of India",
          bankAccountNo: "30491823901",
          maskedBankAccount: "*******3901",
          bankIfsc: "SBIN0004567",
          employmentStatus: "Active"
        },
        {
          employeeId: "EMP1004",
          firstName: "Sneha",
          lastName: "Patel",
          email: "sneha.patel@apexsolutions.com",
          phone: "+91 9876543213",
          gender: "Female",
          address: "22 HSR Layout Sector 2, Bangalore",
          departmentId: 3,
          departmentName: "Finance & Accounts",
          designation: "Financial Analyst",
          joiningDate: "2023-09-20",
          basicSalary: 52000.0,
          bankName: "Axis Bank",
          bankAccountNo: "918020038910234",
          maskedBankAccount: "***********0234",
          bankIfsc: "UTIB0000123",
          employmentStatus: "Active"
        },
        {
          employeeId: "EMP1005",
          firstName: "Vikram",
          lastName: "Rao",
          email: "vikram.rao@apexsolutions.com",
          phone: "+91 9876543214",
          gender: "Male",
          address: "15 Jayanagar 9th Block, Bangalore",
          departmentId: 4,
          departmentName: "Marketing & Sales",
          designation: "Growth Marketing Lead",
          joiningDate: "2023-11-05",
          basicSalary: 62000.0,
          bankName: "Kotak Mahindra Bank",
          bankAccountNo: "6712398401",
          maskedBankAccount: "******8401",
          bankIfsc: "KKBK0000456",
          employmentStatus: "Active"
        },
        {
          employeeId: "EMP1006",
          firstName: "Ananya",
          lastName: "Iyer",
          email: "ananya.iyer@apexsolutions.com",
          phone: "+91 9876543215",
          gender: "Female",
          address: "88 Malleshwaram 18th Cross, Bangalore",
          departmentId: 1,
          departmentName: "Engineering",
          designation: "QA Automation Engineer",
          joiningDate: "2024-04-01",
          basicSalary: 45000.0,
          bankName: "HDFC Bank",
          bankAccountNo: "50100456789012",
          maskedBankAccount: "**********9012",
          bankIfsc: "HDFC0001234",
          employmentStatus: "Active"
        },
        {
          employeeId: "EMP1007",
          firstName: "Karan",
          lastName: "Mehta",
          email: "karan.mehta@apexsolutions.com",
          phone: "+91 9876543216",
          gender: "Male",
          address: "34 Bellandur Green Glen, Bangalore",
          departmentId: 5,
          departmentName: "Operations",
          designation: "Operations Specialist",
          joiningDate: "2024-02-15",
          basicSalary: 40000.0,
          bankName: "ICICI Bank",
          bankAccountNo: "002105432198",
          maskedBankAccount: "********2198",
          bankIfsc: "ICIC0000021",
          employmentStatus: "Active"
        }
      ],
      attendance: [
        { attendanceId: 1, employeeId: "EMP1001", employeeName: "Aarav Sharma", departmentName: "Engineering", attendanceDate: "2026-11-17", status: "Present", remarks: "Regular duty" },
        { attendanceId: 2, employeeId: "EMP1002", employeeName: "Priya Nair", departmentName: "Human Resources", attendanceDate: "2026-11-17", status: "Present", remarks: "Regular duty" },
        { attendanceId: 3, employeeId: "EMP1003", employeeName: "Rohan Verma", departmentName: "Engineering", attendanceDate: "2026-11-17", status: "Half Day", remarks: "Doctor appointment" },
        { attendanceId: 4, employeeId: "EMP1004", employeeName: "Sneha Patel", departmentName: "Finance & Accounts", attendanceDate: "2026-11-17", status: "Present", remarks: "Regular duty" },
        { attendanceId: 5, employeeId: "EMP1005", employeeName: "Vikram Rao", departmentName: "Marketing & Sales", attendanceDate: "2026-11-17", status: "Leave", remarks: "Casual leave approved" },
        { attendanceId: 6, employeeId: "EMP1006", employeeName: "Ananya Iyer", departmentName: "Engineering", attendanceDate: "2026-11-17", status: "Present", remarks: "Regular duty" },
        { attendanceId: 7, employeeId: "EMP1007", employeeName: "Karan Mehta", departmentName: "Operations", attendanceDate: "2026-11-17", status: "Absent", remarks: "Uninformed" }
      ],
      payroll: [
        {
          payrollId: 1,
          employeeId: "EMP1001",
          employeeName: "Aarav Sharma",
          departmentName: "Engineering",
          designation: "Senior Software Engineer",
          bankName: "HDFC Bank",
          bankAccountNo: "50100234891102",
          bankIfsc: "HDFC0001234",
          salaryMonth: "2026-11",
          totalWorkingDays: 30,
          presentDays: 30.0,
          paidLeaves: 0.0,
          unpaidAbsentDays: 0.0,
          basicSalary: 75000.0,
          hra: 15000.0,
          da: 7500.0,
          otherAllowances: 3000.0,
          bonus: 5000.0,
          overtimePay: 0.0,
          grossSalary: 100500.0,
          pfDeduction: 9000.0,
          profTax: 200.0,
          incomeTax: 5025.0,
          leaveDeduction: 0.0,
          otherDeductions: 0.0,
          totalDeductions: 14225.0,
          netSalary: 86275.0,
          paymentStatus: "Paid"
        },
        {
          payrollId: 2,
          employeeId: "EMP1002",
          employeeName: "Priya Nair",
          departmentName: "Human Resources",
          designation: "HR Business Partner",
          bankName: "ICICI Bank",
          bankAccountNo: "002101569842",
          bankIfsc: "ICIC0000021",
          salaryMonth: "2026-11",
          totalWorkingDays: 30,
          presentDays: 29.5,
          paidLeaves: 0.0,
          unpaidAbsentDays: 0.5,
          basicSalary: 58000.0,
          hra: 11600.0,
          da: 5800.0,
          otherAllowances: 2500.0,
          bonus: 0.0,
          overtimePay: 0.0,
          grossSalary: 77900.0,
          pfDeduction: 6960.0,
          profTax: 200.0,
          incomeTax: 3895.0,
          leaveDeduction: 966.67,
          otherDeductions: 0.0,
          totalDeductions: 12021.67,
          netSalary: 65878.33,
          paymentStatus: "Paid"
        },
        {
          payrollId: 3,
          employeeId: "EMP1003",
          employeeName: "Rohan Verma",
          departmentName: "Engineering",
          designation: "Full Stack Developer",
          bankName: "State Bank of India",
          bankAccountNo: "30491823901",
          bankIfsc: "SBIN0004567",
          salaryMonth: "2026-11",
          totalWorkingDays: 30,
          presentDays: 29.0,
          paidLeaves: 0.0,
          unpaidAbsentDays: 1.0,
          basicSalary: 50000.0,
          hra: 10000.0,
          da: 5000.0,
          otherAllowances: 2000.0,
          bonus: 0.0,
          overtimePay: 1500.0,
          grossSalary: 68500.0,
          pfDeduction: 6000.0,
          profTax: 200.0,
          incomeTax: 3425.0,
          leaveDeduction: 1666.67,
          otherDeductions: 0.0,
          totalDeductions: 11291.67,
          netSalary: 57208.33,
          paymentStatus: "Paid"
        },
        {
          payrollId: 4,
          employeeId: "EMP1004",
          employeeName: "Sneha Patel",
          departmentName: "Finance & Accounts",
          designation: "Financial Analyst",
          bankName: "Axis Bank",
          bankAccountNo: "918020038910234",
          bankIfsc: "UTIB0000123",
          salaryMonth: "2026-11",
          totalWorkingDays: 30,
          presentDays: 30.0,
          paidLeaves: 0.0,
          unpaidAbsentDays: 0.0,
          basicSalary: 52000.0,
          hra: 10400.0,
          da: 5200.0,
          otherAllowances: 2000.0,
          bonus: 2500.0,
          overtimePay: 0.0,
          grossSalary: 72100.0,
          pfDeduction: 6240.0,
          profTax: 200.0,
          incomeTax: 3605.0,
          leaveDeduction: 0.0,
          otherDeductions: 0.0,
          totalDeductions: 10045.0,
          netSalary: 62055.0,
          paymentStatus: "Pending"
        },
        {
          payrollId: 5,
          employeeId: "EMP1005",
          employeeName: "Vikram Rao",
          departmentName: "Marketing & Sales",
          designation: "Growth Marketing Lead",
          bankName: "Kotak Mahindra Bank",
          bankAccountNo: "6712398401",
          bankIfsc: "KKBK0000456",
          salaryMonth: "2026-11",
          totalWorkingDays: 30,
          presentDays: 29.0,
          paidLeaves: 1.0,
          unpaidAbsentDays: 0.0,
          basicSalary: 62000.0,
          hra: 12400.0,
          da: 6200.0,
          otherAllowances: 3000.0,
          bonus: 4000.0,
          overtimePay: 0.0,
          grossSalary: 87600.0,
          pfDeduction: 7440.0,
          profTax: 200.0,
          incomeTax: 4380.0,
          leaveDeduction: 0.0,
          otherDeductions: 0.0,
          totalDeductions: 12020.0,
          netSalary: 75580.0,
          paymentStatus: "Pending"
        }
      ],
      payments: [
        {
          paymentId: 1,
          payrollId: 1,
          employeeId: "EMP1001",
          employeeName: "Aarav Sharma",
          departmentName: "Engineering",
          salaryMonth: "2026-11",
          paymentDate: "2026-11-15",
          amount: 86275.0,
          paymentMethod: "Bank Transfer",
          transactionReference: "NEFT-HDFC-9842104921",
          status: "Success",
          notes: "November Salary credited via NEFT"
        },
        {
          paymentId: 2,
          payrollId: 2,
          employeeId: "EMP1002",
          employeeName: "Priya Nair",
          departmentName: "Human Resources",
          salaryMonth: "2026-11",
          paymentDate: "2026-11-15",
          amount: 65878.33,
          paymentMethod: "Bank Transfer",
          transactionReference: "IMPS-ICIC-8723419042",
          status: "Success",
          notes: "November Salary credited via IMPS"
        },
        {
          paymentId: 3,
          payrollId: 3,
          employeeId: "EMP1003",
          employeeName: "Rohan Verma",
          departmentName: "Engineering",
          salaryMonth: "2026-11",
          paymentDate: "2026-11-15",
          amount: 57208.33,
          paymentMethod: "UPI",
          transactionReference: "UPI-SBIN-5412984120",
          status: "Success",
          notes: "November Salary credited via UPI"
        }
      ]
    }),

    getDB: () => {
      try {
        const item = localStorage.getItem(ClientStorage.KEY);
        if (item) return JSON.parse(item);
      } catch (e) {}
      const init = ClientStorage.getInitialData();
      ClientStorage.saveDB(init);
      return init;
    },

    saveDB: (data) => {
      try {
        localStorage.setItem(ClientStorage.KEY, JSON.stringify(data));
      } catch (e) {}
    }
  };

  // ------------------------------------------------------------------
  // UTILITY HELPERS
  // ------------------------------------------------------------------
  const Utils = {
    formatCurrency: (amount) => {
      if (amount === null || amount === undefined || isNaN(amount)) return '₹0.00';
      const num = parseFloat(amount);
      return '₹' + num.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    },

    formatDate: (dateStr) => {
      if (!dateStr) return 'N/A';
      try {
        const d = new Date(dateStr);
        return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
      } catch (e) {
        return dateStr;
      }
    },

    getCurrentMonthStr: () => {
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      return `${year}-${month}`;
    },

    getTodayStr: () => {
      const now = new Date();
      return now.toISOString().split('T')[0];
    },

    showToast: (message, type = 'info') => {
      let container = document.getElementById('toast-container');
      if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
      }

      const toast = document.createElement('div');
      toast.className = `toast ${type}`;
      toast.innerHTML = `
        <span class="toast-message">${message}</span>
        <button style="background:none;border:none;cursor:pointer;color:#64748b;" onclick="this.parentElement.remove()">✕</button>
      `;

      container.appendChild(toast);
      setTimeout(() => {
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(100%)';
        setTimeout(() => toast.remove(), 300);
      }, 4000);
    },

    openModal: (modalId) => {
      const el = document.getElementById(modalId);
      if (el) el.classList.add('show');
    },

    closeModal: (modalId) => {
      const el = document.getElementById(modalId);
      if (el) el.classList.remove('show');
    },

    exportTableToCSV: (tableId, filename = 'payroll_report.csv') => {
      const table = document.getElementById(tableId);
      if (!table) return;

      const rows = Array.from(table.querySelectorAll('tr'));
      const csvContent = rows.map(row => {
        const cells = Array.from(row.querySelectorAll('th, td'));
        return cells.map(cell => {
          let text = cell.innerText.replace(/"/g, '""').trim();
          return `"${text}"`;
        }).join(',');
      }).join('\n');

      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    numberToWords: (num) => {
      if (!num) return 'Zero Rupees Only';
      const a = ['', 'One ', 'Two ', 'Three ', 'Four ', 'Five ', 'Six ', 'Seven ', 'Eight ', 'Nine ', 'Ten ', 'Eleven ', 'Twelve ', 'Thirteen ', 'Fourteen ', 'Fifteen ', 'Sixteen ', 'Seventeen ', 'Eighteen ', 'Nineteen '];
      const b = ['', '', 'Twenty', 'Thirty', 'Forty', 'Fifty', 'Sixty', 'Seventy', 'Eighty', 'Ninety'];

      const inWords = (n) => {
        let str = '';
        if (n > 99) {
          str += a[Math.floor(n / 100)] + 'Hundred ';
          n %= 100;
        }
        if (n > 19) {
          str += b[Math.floor(n / 10)] + ' ' + a[n % 10];
        } else {
          str += a[n];
        }
        return str;
      };

      let n = Math.floor(num);
      let output = '';
      if (n >= 10000000) {
        output += inWords(Math.floor(n / 10000000)) + 'Crore ';
        n %= 10000000;
      }
      if (n >= 100000) {
        output += inWords(Math.floor(n / 100000)) + 'Lakh ';
        n %= 100000;
      }
      if (n >= 1000) {
        output += inWords(Math.floor(n / 1000)) + 'Thousand ';
        n %= 1000;
      }
      if (n > 0) {
        output += inWords(n);
      }
      return (output.trim() + ' Rupees Only');
    }
  };

  // ------------------------------------------------------------------
  // HTTP CLIENT (WITH AUTOMATIC VERCEL / OFFLINE FALLBACK ENGINE)
  // ------------------------------------------------------------------
  const Http = {
    request: async (endpoint, options = {}) => {
      const url = `${API_BASE}${endpoint}`;
      const defaultOptions = {
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        },
        credentials: 'same-origin'
      };

      try {
        const res = await fetch(url, { ...defaultOptions, ...options });

        // If server returns JSON cleanly (Tomcat backend)
        const contentType = res.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
          const data = await res.json();
          if (res.status === 401) {
            if (!window.location.pathname.endsWith('index.html') && !window.location.pathname.endsWith('/')) {
              window.location.href = 'index.html';
            }
            throw new Error(data.message || 'Session expired. Please sign in.');
          }
          if (!res.ok) {
            throw new Error(data.message || `Request failed with status ${res.status}`);
          }
          return data;
        }

        // If static hosting (Vercel) returns HTML 404 for /api/...
        throw new Error('FallbackToClientEngine');
      } catch (err) {
        // Fallback Engine triggers on static Vercel deployment
        return Http.simulateClientAPI(endpoint, options);
      }
    },

    get: (endpoint) => Http.request(endpoint, { method: 'GET' }),
    post: (endpoint, body) => Http.request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
    put: (endpoint, body) => Http.request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
    delete: (endpoint) => Http.request(endpoint, { method: 'DELETE' }),

    // Seamless in-browser client engine for Vercel
    simulateClientAPI: (endpoint, options) => {
      const db = ClientStorage.getDB();
      const method = (options.method || 'GET').toUpperCase();
      const body = options.body ? JSON.parse(options.body) : {};

      const urlParts = endpoint.split('?');
      const path = urlParts[0];
      const params = new URLSearchParams(urlParts[1] || '');

      // 1. Auth
      if (path === '/auth/status' || path === '/auth/me') {
        const user = localStorage.getItem('payroll_user');
        if (user) {
          return { success: true, message: "Active session", data: JSON.parse(user) };
        }
        throw new Error("No active session");
      }

      if (path === '/auth/login') {
        const u = body.username ? body.username.trim() : '';
        const p = body.password || '';
        const admin = db.admins.find(a => a.username === u && a.password === p);
        if (admin) {
          return { success: true, message: `Welcome, ${admin.fullName}!`, data: admin };
        }
        throw new Error("Invalid username or password.");
      }

      if (path === '/auth/logout') {
        localStorage.removeItem('payroll_user');
        return { success: true, message: "Logged out" };
      }

      // 2. Departments
      if (path === '/departments') {
        return { success: true, message: "Departments", data: db.departments };
      }

      // 3. Dashboard
      if (path === '/dashboard') {
        const totalEmp = db.employees.length;
        const activeEmp = db.employees.filter(e => e.employmentStatus === 'Active').length;
        const totalPayroll = db.payroll.reduce((s, p) => s + parseFloat(p.netSalary || 0), 0);
        const paidPayroll = db.payroll.filter(p => p.paymentStatus === 'Paid').reduce((s, p) => s + parseFloat(p.netSalary || 0), 0);
        const pendingAmount = db.payroll.filter(p => p.paymentStatus === 'Pending').reduce((s, p) => s + parseFloat(p.netSalary || 0), 0);
        const pendingCount = db.payroll.filter(p => p.paymentStatus === 'Pending').length;

        const attToday = db.attendance.filter(a => a.attendanceDate === '2026-11-17');
        const attSummary = {
          present: attToday.filter(a => a.status === 'Present').length,
          halfDay: attToday.filter(a => a.status === 'Half Day').length,
          leave: attToday.filter(a => a.status === 'Leave').length,
          absent: attToday.filter(a => a.status === 'Absent').length
        };

        const deptStats = db.departments.map(d => ({
          department: d.departmentName,
          count: db.employees.filter(e => e.departmentId === d.departmentId).length
        }));

        const payrollTrend = [
          { month: "2026-08", gross: 340000.0, net: 298000.0, count: 6 },
          { month: "2026-09", gross: 380000.0, net: 332000.0, count: 7 },
          { month: "2026-10", gross: 406600.0, net: 346998.0, count: 7 },
          { month: "2026-11", gross: db.payroll.reduce((s, p) => s + parseFloat(p.grossSalary || 0), 0), net: totalPayroll, count: db.payroll.length }
        ];

        const recentActivities = db.payments.slice(-5).reverse().map(pay => ({
          title: `Salary Disbursed to ${pay.employeeName}`,
          subtitle: `Month: ${pay.salaryMonth} via ${pay.paymentMethod}`,
          amount: pay.amount,
          date: pay.paymentDate
        }));

        return {
          success: true,
          data: {
            totalEmployees: totalEmp,
            activeEmployees: activeEmp,
            currentMonth: "2026-11",
            activePayrollMonth: "2026-11",
            totalMonthlyPayroll: totalPayroll,
            totalPaidSalaries: paidPayroll,
            pendingAmount: pendingAmount,
            pendingPaymentsCount: pendingCount,
            attendanceSummary: attSummary,
            departmentStats: deptStats,
            payrollTrend: payrollTrend,
            recentActivities: recentActivities
          }
        };
      }

      // 4. Employees
      if (path === '/employees') {
        if (method === 'GET') {
          let list = [...db.employees];
          const q = (params.get('query') || '').toLowerCase();
          const deptId = params.get('departmentId');
          const status = params.get('status');

          if (q) {
            list = list.filter(e => (e.employeeId + e.firstName + e.lastName + e.email + e.designation).toLowerCase().includes(q));
          }
          if (deptId) {
            list = list.filter(e => String(e.departmentId) === String(deptId));
          }
          if (status && status !== 'All') {
            list = list.filter(e => e.employmentStatus.toLowerCase() === status.toLowerCase());
          }
          return { success: true, data: list };
        } else if (method === 'POST') {
          let empId = body.employeeId;
          if (!empId) {
            const ids = db.employees.map(e => parseInt(e.employeeId.slice(3))).filter(n => !isNaN(n));
            empId = `EMP${(Math.max(...ids, 1000) + 1)}`;
          }
          body.employeeId = empId;
          const dept = db.departments.find(d => d.departmentId === parseInt(body.departmentId));
          body.departmentName = dept ? dept.departmentName : 'General';
          const acc = String(body.bankAccountNo || '0000');
          body.maskedBankAccount = '*'.repeat(Math.max(0, acc.length - 4)) + acc.slice(-4);
          db.employees.push(body);
          ClientStorage.saveDB(db);
          return { success: true, message: `Employee ${empId} created`, data: body };
        }
      }

      if (path === '/employees/next-id') {
        const ids = db.employees.map(e => parseInt(e.employeeId.slice(3))).filter(n => !isNaN(n));
        const nextId = `EMP${(Math.max(...ids, 1000) + 1)}`;
        return { success: true, data: { nextEmployeeId: nextId } };
      }

      if (path.startsWith('/employees/')) {
        const empId = path.split('/')[2];
        if (method === 'GET') {
          const emp = db.employees.find(e => e.employeeId === empId);
          if (emp) return { success: true, data: emp };
          throw new Error("Employee not found");
        } else if (method === 'PUT') {
          const idx = db.employees.findIndex(e => e.employeeId === empId);
          if (idx !== -1) {
            body.employeeId = empId;
            const dept = db.departments.find(d => d.departmentId === parseInt(body.departmentId));
            body.departmentName = dept ? dept.departmentName : db.employees[idx].departmentName;
            const acc = String(body.bankAccountNo || '0000');
            body.maskedBankAccount = '*'.repeat(Math.max(0, acc.length - 4)) + acc.slice(-4);
            db.employees[idx] = { ...db.employees[idx], ...body };
            ClientStorage.saveDB(db);
            return { success: true, message: "Employee updated", data: db.employees[idx] };
          }
          throw new Error("Employee not found");
        } else if (method === 'DELETE') {
          db.employees = db.employees.filter(e => e.employeeId !== empId);
          ClientStorage.saveDB(db);
          return { success: true, message: "Employee deleted" };
        }
      }

      // 5. Attendance
      if (path === '/attendance') {
        if (method === 'GET') {
          const dt = params.get('date') || Utils.getTodayStr();
          const recs = db.attendance.filter(a => a.attendanceDate === dt);
          const counts = {
            Present: recs.filter(a => a.status === 'Present').length,
            'Half Day': recs.filter(a => a.status === 'Half Day').length,
            Leave: recs.filter(a => a.status === 'Leave').length,
            Absent: recs.filter(a => a.status === 'Absent').length
          };
          return { success: true, data: { date: dt, records: recs, counts: counts } };
        } else if (method === 'POST') {
          const emp = db.employees.find(e => e.employeeId === body.employeeId);
          const empName = emp ? `${emp.firstName} ${emp.lastName}` : body.employeeId;
          const deptName = emp ? emp.departmentName : 'Engineering';

          const existing = db.attendance.find(a => a.employeeId === body.employeeId && a.attendanceDate === body.attendanceDate);
          if (existing) {
            existing.status = body.status;
            existing.remarks = body.remarks || '';
          } else {
            const newId = Math.max(...db.attendance.map(a => a.attendanceId), 0) + 1;
            db.attendance.push({
              attendanceId: newId,
              employeeId: body.employeeId,
              employeeName: empName,
              departmentName: deptName,
              attendanceDate: body.attendanceDate,
              status: body.status,
              remarks: body.remarks || ''
            });
          }
          ClientStorage.saveDB(db);
          return { success: true, message: "Attendance recorded", data: body };
        }
      }

      // 6. Payroll
      if (path === '/payroll') {
        let list = [...db.payroll];
        const month = params.get('month');
        const status = params.get('status');
        if (month) list = list.filter(p => p.salaryMonth === month);
        if (status && status !== 'All') list = list.filter(p => p.paymentStatus.toLowerCase() === status.toLowerCase());
        return { success: true, data: list };
      }

      if (path.startsWith('/payroll/')) {
        const pid = parseInt(path.split('/')[2]);
        if (method === 'GET') {
          const p = db.payroll.find(x => x.payrollId === pid);
          if (p) return { success: true, data: p };
          throw new Error("Payroll record not found");
        } else if (method === 'DELETE') {
          db.payroll = db.payroll.filter(x => x.payrollId !== pid);
          ClientStorage.saveDB(db);
          return { success: true, message: "Payroll deleted" };
        }
      }

      if (path === '/payroll/calculate' || path === '/payroll/generate') {
        const emp = db.employees.find(e => e.employeeId === body.employeeId);
        if (!emp) throw new Error("Employee not found");

        const basic = parseFloat(emp.basicSalary);
        const hra = Math.round(basic * 0.20 * 100) / 100;
        const da = Math.round(basic * 0.10 * 100) / 100;
        const bonus = parseFloat(body.bonus || 0);
        const ot = parseFloat(body.overtimePay || 0);
        const allow = parseFloat(body.otherAllowances || 0);
        const gross = Math.round((basic + hra + da + bonus + ot + allow) * 100) / 100;

        const pf = Math.round(basic * 0.12 * 100) / 100;
        const ptax = 200.0;
        const tax = Math.round(gross * 0.05 * 100) / 100;
        const otherDeduct = parseFloat(body.otherDeductions || 0);
        const totalDeduct = Math.round((pf + ptax + tax + otherDeduct) * 100) / 100;
        const net = Math.round((gross - totalDeduct) * 100) / 100;

        const pData = {
          payrollId: Math.max(...db.payroll.map(p => p.payrollId), 0) + 1,
          employeeId: emp.employeeId,
          employeeName: `${emp.firstName} ${emp.lastName}`,
          departmentName: emp.departmentName,
          designation: emp.designation,
          bankName: emp.bankName,
          bankAccountNo: emp.bankAccountNo,
          bankIfsc: emp.bankIfsc,
          salaryMonth: body.salaryMonth || "2026-11",
          totalWorkingDays: 30,
          presentDays: 30.0,
          paidLeaves: 0.0,
          unpaidAbsentDays: 0.0,
          basicSalary: basic,
          hra: hra,
          da: da,
          otherAllowances: allow,
          bonus: bonus,
          overtimePay: ot,
          grossSalary: gross,
          pfDeduction: pf,
          profTax: ptax,
          incomeTax: tax,
          leaveDeduction: 0.0,
          otherDeductions: otherDeduct,
          totalDeductions: totalDeduct,
          netSalary: net,
          paymentStatus: "Pending"
        };

        if (path === '/payroll/calculate') {
          return { success: true, data: pData };
        } else {
          db.payroll.push(pData);
          ClientStorage.saveDB(db);
          return { success: true, message: `Payroll generated for ${pData.employeeName}`, data: pData };
        }
      }

      if (path === '/payroll/process-batch') {
        const month = body.salaryMonth || "2026-11";
        let count = 0;
        db.employees.forEach(emp => {
          if (emp.employmentStatus === 'Active' && !db.payroll.some(p => p.employeeId === emp.employeeId && p.salaryMonth === month)) {
            const basic = parseFloat(emp.basicSalary);
            const hra = Math.round(basic * 0.20 * 100) / 100;
            const da = Math.round(basic * 0.10 * 100) / 100;
            const gross = basic + hra + da;
            const pf = Math.round(basic * 0.12 * 100) / 100;
            const ptax = 200.0;
            const tax = Math.round(gross * 0.05 * 100) / 100;
            const totDeduct = pf + ptax + tax;
            const net = gross - totDeduct;

            db.payroll.push({
              payrollId: Math.max(...db.payroll.map(p => p.payrollId), 0) + 1,
              employeeId: emp.employeeId,
              employeeName: `${emp.firstName} ${emp.lastName}`,
              departmentName: emp.departmentName,
              designation: emp.designation,
              bankName: emp.bankName,
              bankAccountNo: emp.bankAccountNo,
              bankIfsc: emp.bankIfsc,
              salaryMonth: month,
              totalWorkingDays: 30,
              presentDays: 30.0,
              paidLeaves: 0.0,
              unpaidAbsentDays: 0.0,
              basicSalary: basic,
              hra: hra,
              da: da,
              otherAllowances: 0.0,
              bonus: 0.0,
              overtimePay: 0.0,
              grossSalary: gross,
              pfDeduction: pf,
              profTax: ptax,
              incomeTax: tax,
              leaveDeduction: 0.0,
              otherDeductions: 0.0,
              totalDeductions: totDeduct,
              netSalary: net,
              paymentStatus: "Pending"
            });
            count++;
          }
        });
        ClientStorage.saveDB(db);
        return { success: true, message: `Batch payroll processed. Created ${count} records.`, data: { generatedCount: count } };
      }

      // 7. Payslips
      if (path.startsWith('/payslips/')) {
        const pid = parseInt(path.split('/')[2]);
        const p = db.payroll.find(x => x.payrollId === pid);
        if (p) {
          return { success: true, data: { company: db.company, payroll: p } };
        }
        throw new Error("Payslip record not found");
      }

      // 8. Payments
      if (path === '/payments') {
        if (method === 'GET') {
          let list = [...db.payments];
          const month = params.get('month');
          const pMethod = params.get('method');
          if (month) list = list.filter(pay => pay.salaryMonth === month);
          if (pMethod && pMethod !== 'All') list = list.filter(pay => pay.paymentMethod.toLowerCase() === pMethod.toLowerCase());
          return { success: true, data: list };
        } else if (method === 'POST') {
          const pr = db.payroll.find(p => p.payrollId === parseInt(body.payrollId));
          if (!pr) throw new Error("Payroll record not found");

          const newId = Math.max(...db.payments.map(p => p.paymentId), 0) + 1;
          const newPay = {
            paymentId: newId,
            payrollId: pr.payrollId,
            employeeId: pr.employeeId,
            employeeName: pr.employeeName,
            departmentName: pr.departmentName,
            salaryMonth: pr.salaryMonth,
            paymentDate: body.paymentDate || Utils.getTodayStr(),
            amount: parseFloat(body.amount || pr.netSalary),
            paymentMethod: body.paymentMethod || "Bank Transfer",
            transactionReference: body.transactionReference || `TRX-${Date.now().toString().slice(-8)}`,
            status: "Success",
            notes: body.notes || "Disbursed"
          };
          db.payments.push(newPay);
          pr.paymentStatus = 'Paid';
          ClientStorage.saveDB(db);
          return { success: true, message: `Payment of ₹${newPay.amount} recorded successfully.`, data: newPay };
        }
      }

      // 9. Reports
      if (path === '/reports/payroll') {
        let list = [...db.payroll];
        const month = params.get('month');
        if (month) list = list.filter(p => p.salaryMonth === month);
        return { success: true, data: list };
      }
      if (path === '/reports/payments') {
        let list = [...db.payments];
        const month = params.get('month');
        if (month) list = list.filter(pay => pay.salaryMonth === month);
        return { success: true, data: list };
      }
      if (path === '/reports/employees') {
        return { success: true, data: db.employees };
      }

      return { success: true, message: "OK", data: null };
    }
  };

  // ------------------------------------------------------------------
  // AUTHENTICATION MODULE
  // ------------------------------------------------------------------
  const Auth = {
    checkSession: async (isLoginPage = false) => {
      try {
        const res = await Http.get('/auth/status');
        if (res.success && res.data) {
          localStorage.setItem('payroll_user', JSON.stringify(res.data));
          if (isLoginPage) {
            window.location.href = 'dashboard.html';
          } else {
            Auth.updateUI(res.data);
          }
        }
      } catch (e) {
        localStorage.removeItem('payroll_user');
        if (!isLoginPage) {
          window.location.href = 'index.html';
        }
      }
    },

    login: async (username, password) => {
      try {
        const res = await Http.post('/auth/login', { username, password });
        if (res.success && res.data) {
          localStorage.setItem('payroll_user', JSON.stringify(res.data));
          Utils.showToast('Login successful! Redirecting...', 'success');
          setTimeout(() => {
            window.location.href = 'dashboard.html';
          }, 800);
        }
      } catch (err) {
        Utils.showToast(err.message, 'error');
        throw err;
      }
    },

    logout: async () => {
      try {
        await Http.post('/auth/logout', {});
      } catch (e) {
        console.warn('Logout:', e);
      } finally {
        localStorage.removeItem('payroll_user');
        window.location.href = 'index.html';
      }
    },

    updateUI: (user) => {
      const nameEls = document.querySelectorAll('.user-name-display');
      const roleEls = document.querySelectorAll('.user-role-display');
      const avatarEls = document.querySelectorAll('.user-avatar');

      nameEls.forEach(el => el.textContent = user.fullName || user.username);
      roleEls.forEach(el => el.textContent = user.roleDescription || user.role);
      avatarEls.forEach(el => {
        const initial = (user.fullName || user.username || 'A').charAt(0).toUpperCase();
        el.textContent = initial;
      });
    }
  };

  // ------------------------------------------------------------------
  // DASHBOARD MODULE
  // ------------------------------------------------------------------
  const Dashboard = {
    init: async () => {
      try {
        const res = await Http.get('/dashboard');
        if (res.success && res.data) {
          Dashboard.renderStats(res.data);
          Dashboard.renderTrendChart(res.data.payrollTrend || []);
          Dashboard.renderDepartmentBreakdown(res.data.departmentStats || []);
          Dashboard.renderRecentActivities(res.data.recentActivities || []);
        }
      } catch (e) {
        Utils.showToast('Error loading dashboard: ' + e.message, 'error');
      }
    },

    renderStats: (data) => {
      const setVal = (id, val) => {
        const el = document.getElementById(id);
        if (el) el.textContent = val;
      };

      setVal('stat-total-emp', data.totalEmployees || 0);
      setVal('stat-active-emp', `${data.activeEmployees || 0} Active`);
      setVal('stat-total-payroll', Utils.formatCurrency(data.totalMonthlyPayroll));
      setVal('stat-paid-payroll', Utils.formatCurrency(data.totalPaidSalaries));
      setVal('stat-pending-payroll', `${data.pendingPaymentsCount || 0} Pending (${Utils.formatCurrency(data.pendingAmount)})`);

      if (data.attendanceSummary) {
        setVal('att-present-count', data.attendanceSummary.present || 0);
        setVal('att-half-count', data.attendanceSummary.halfDay || 0);
        setVal('att-leave-count', data.attendanceSummary.leave || 0);
        setVal('att-absent-count', data.attendanceSummary.absent || 0);
      }

      const monthBadge = document.getElementById('active-payroll-month');
      if (monthBadge && data.activePayrollMonth) {
        monthBadge.textContent = `Month: ${data.activePayrollMonth}`;
      }
    },

    renderTrendChart: (trend) => {
      const container = document.getElementById('payroll-trend-bars');
      if (!container) return;

      if (!trend || trend.length === 0) {
        container.innerHTML = '<div style="color:#94a3b8;padding:2rem;text-align:center;">No historical payroll trends found.</div>';
        return;
      }

      let maxVal = 1000;
      trend.forEach(t => {
        const v = parseFloat(t.gross || 0);
        if (v > maxVal) maxVal = v;
      });

      container.innerHTML = trend.map(item => {
        const net = parseFloat(item.net || 0);
        const gross = parseFloat(item.gross || 0);
        const heightPct = Math.min(100, Math.max(15, (gross / maxVal) * 100));

        return `
          <div style="flex:1;display:flex;flex-direction:column;align-items:center;gap:0.5rem;min-width:60px;">
            <div style="font-size:0.75rem;font-weight:700;color:#2563eb;">${Utils.formatCurrency(net)}</div>
            <div style="width:100%;height:140px;background:#f1f5f9;border-radius:6px;display:flex;align-items:flex-end;overflow:hidden;">
              <div style="width:100%;height:${heightPct}%;background:linear-gradient(180deg,#3b82f6,#1d4ed8);border-radius:6px 6px 0 0;transition:all 0.5s ease;" title="Gross: ${Utils.formatCurrency(gross)}"></div>
            </div>
            <div style="font-size:0.78rem;font-weight:600;color:#64748b;">${item.month}</div>
          </div>
        `;
      }).join('');
    },

    renderDepartmentBreakdown: (deptStats) => {
      const container = document.getElementById('dept-stats-list');
      if (!container) return;

      if (!deptStats || deptStats.length === 0) {
        container.innerHTML = '<div style="color:#94a3b8;padding:1rem;">No department data found.</div>';
        return;
      }

      let total = deptStats.reduce((sum, d) => sum + parseInt(d.count || 0), 0) || 1;

      container.innerHTML = deptStats.map(d => {
        const count = parseInt(d.count || 0);
        const pct = Math.round((count / total) * 100);
        return `
          <div style="margin-bottom:1rem;">
            <div style="display:flex;justify-content:space-between;font-size:0.85rem;font-weight:600;margin-bottom:0.25rem;">
              <span>${d.department}</span>
              <span style="color:#2563eb;">${count} (${pct}%)</span>
            </div>
            <div style="width:100%;height:8px;background:#f1f5f9;border-radius:4px;overflow:hidden;">
              <div style="width:${pct}%;height:100%;background:#2563eb;border-radius:4px;"></div>
            </div>
          </div>
        `;
      }).join('');
    },

    renderRecentActivities: (activities) => {
      const container = document.getElementById('recent-activities-list');
      if (!container) return;

      if (!activities || activities.length === 0) {
        container.innerHTML = '<div style="color:#94a3b8;padding:1rem;">No recent payment disbursements.</div>';
        return;
      }

      container.innerHTML = activities.map(act => `
        <div style="display:flex;align-items:center;justify-content:space-between;padding:0.75rem 0;border-bottom:1px solid #f1f5f9;">
          <div>
            <div style="font-size:0.88rem;font-weight:600;color:#0f172a;">${act.title}</div>
            <div style="font-size:0.75rem;color:#64748b;">${act.subtitle} • ${Utils.formatDate(act.date)}</div>
          </div>
          <div style="font-size:0.92rem;font-weight:700;color:#10b981;">+${Utils.formatCurrency(act.amount)}</div>
        </div>
      `).join('');
    }
  };

  // ------------------------------------------------------------------
  // EMPLOYEE MANAGEMENT MODULE
  // ------------------------------------------------------------------
  const Employees = {
    cachedDepartments: [],

    init: async () => {
      await Employees.loadDepartments();
      await Employees.loadEmployees();

      const searchInput = document.getElementById('emp-search');
      const deptFilter = document.getElementById('emp-filter-dept');
      const statusFilter = document.getElementById('emp-filter-status');

      const applyFilters = () => {
        const q = searchInput ? searchInput.value : '';
        const d = deptFilter ? deptFilter.value : '';
        const s = statusFilter ? statusFilter.value : '';
        Employees.loadEmployees(q, d, s);
      };

      if (searchInput) searchInput.addEventListener('input', debounce(applyFilters, 300));
      if (deptFilter) deptFilter.addEventListener('change', applyFilters);
      if (statusFilter) statusFilter.addEventListener('change', applyFilters);

      const form = document.getElementById('employee-form');
      if (form) {
        form.addEventListener('submit', Employees.handleFormSubmit);
      }
    },

    loadDepartments: async () => {
      try {
        const res = await Http.get('/departments');
        if (res.success && res.data) {
          Employees.cachedDepartments = res.data;
          const selects = document.querySelectorAll('.dept-select');
          selects.forEach(select => {
            const currentVal = select.value;
            select.innerHTML = select.id.includes('filter') 
              ? '<option value="">All Departments</option>' 
              : '<option value="">-- Select Department --</option>';
            res.data.forEach(d => {
              select.innerHTML += `<option value="${d.departmentId}">${d.departmentName}</option>`;
            });
            if (currentVal) select.value = currentVal;
          });
        }
      } catch (e) {
        console.error('Failed to load departments', e);
      }
    },

    loadEmployees: async (query = '', deptId = '', status = '') => {
      try {
        let ep = '/employees?';
        if (query) ep += `query=${encodeURIComponent(query)}&`;
        if (deptId) ep += `departmentId=${encodeURIComponent(deptId)}&`;
        if (status) ep += `status=${encodeURIComponent(status)}&`;

        const res = await Http.get(ep);
        if (res.success && res.data) {
          Employees.renderTable(res.data);
        }
      } catch (e) {
        Utils.showToast('Error loading employees: ' + e.message, 'error');
      }
    },

    renderTable: (employees) => {
      const tbody = document.getElementById('employee-table-body');
      if (!tbody) return;

      if (!employees || employees.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;padding:2rem;color:#94a3b8;">No employee records found.</td></tr>`;
        return;
      }

      tbody.innerHTML = employees.map(emp => {
        const statusBadge = emp.employmentStatus === 'Active' ? 'badge-success'
          : emp.employmentStatus === 'On Leave' ? 'badge-warning' : 'badge-danger';

        return `
          <tr>
            <td><strong style="color:#2563eb;">${emp.employeeId}</strong></td>
            <td>
              <div style="font-weight:600;color:#0f172a;">${emp.firstName} ${emp.lastName}</div>
              <div style="font-size:0.75rem;color:#64748b;">${emp.email} • ${emp.phone}</div>
            </td>
            <td>${emp.departmentName || 'N/A'}</td>
            <td>${emp.designation}</td>
            <td><strong>${Utils.formatCurrency(emp.basicSalary)}</strong></td>
            <td><span class="badge ${statusBadge}"><span class="badge-dot"></span>${emp.employmentStatus}</span></td>
            <td>
              <div class="action-buttons">
                <button class="icon-btn" title="View Profile" onclick="App.Employees.viewProfile('${emp.employeeId}')">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
                </button>
                <button class="icon-btn" title="Edit Employee" onclick="App.Employees.openEditModal('${emp.employeeId}')">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>
                </button>
                <button class="icon-btn delete" title="Delete Employee" onclick="App.Employees.deleteEmployee('${emp.employeeId}')">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/><line x1="10" y1="11" x2="10" y2="17"/><line x1="14" y1="11" x2="14" y2="17"/></svg>
                </button>
              </div>
            </td>
          </tr>
        `;
      }).join('');
    },

    openAddModal: async () => {
      const form = document.getElementById('employee-form');
      if (form) form.reset();

      document.getElementById('modal-emp-title').textContent = 'Add New Employee';
      document.getElementById('emp-id-field').value = '';

      try {
        const res = await Http.get('/employees/next-id');
        if (res.success && res.data) {
          document.getElementById('emp-id-field').value = res.data.nextEmployeeId;
        }
      } catch (e) {
        console.warn('Could not auto-generate next ID', e);
      }

      Utils.openModal('employee-modal');
    },

    openEditModal: async (empId) => {
      try {
        const res = await Http.get(`/employees/${empId}`);
        if (res.success && res.data) {
          const emp = res.data;
          document.getElementById('modal-emp-title').textContent = `Edit Employee (${emp.employeeId})`;
          document.getElementById('emp-id-field').value = emp.employeeId;
          document.getElementById('emp-first-name').value = emp.firstName;
          document.getElementById('emp-last-name').value = emp.lastName;
          document.getElementById('emp-email').value = emp.email;
          document.getElementById('emp-phone').value = emp.phone;
          document.getElementById('emp-gender').value = emp.gender;
          document.getElementById('emp-dept').value = emp.departmentId;
          document.getElementById('emp-designation').value = emp.designation;
          document.getElementById('emp-joining-date').value = emp.joiningDate;
          document.getElementById('emp-basic-salary').value = emp.basicSalary;
          document.getElementById('emp-bank-name').value = emp.bankName;
          document.getElementById('emp-bank-account').value = emp.bankAccountNo;
          document.getElementById('emp-bank-ifsc').value = emp.bankIfsc;
          document.getElementById('emp-status').value = emp.employmentStatus;
          document.getElementById('emp-address').value = emp.address || '';

          Utils.openModal('employee-modal');
        }
      } catch (e) {
        Utils.showToast('Failed to load employee details: ' + e.message, 'error');
      }
    },

    handleFormSubmit: async (e) => {
      e.preventDefault();
      const empId = document.getElementById('emp-id-field').value.trim();
      const isEdit = document.getElementById('modal-emp-title').textContent.includes('Edit');

      const payload = {
        employeeId: empId,
        firstName: document.getElementById('emp-first-name').value.trim(),
        lastName: document.getElementById('emp-last-name').value.trim(),
        email: document.getElementById('emp-email').value.trim(),
        phone: document.getElementById('emp-phone').value.trim(),
        gender: document.getElementById('emp-gender').value,
        departmentId: parseInt(document.getElementById('emp-dept').value),
        designation: document.getElementById('emp-designation').value.trim(),
        joiningDate: document.getElementById('emp-joining-date').value,
        basicSalary: parseFloat(document.getElementById('emp-basic-salary').value),
        bankName: document.getElementById('emp-bank-name').value.trim(),
        bankAccountNo: document.getElementById('emp-bank-account').value.trim(),
        bankIfsc: document.getElementById('emp-bank-ifsc').value.trim(),
        employmentStatus: document.getElementById('emp-status').value,
        address: document.getElementById('emp-address').value.trim()
      };

      try {
        if (isEdit) {
          await Http.put(`/employees/${empId}`, payload);
          Utils.showToast('Employee updated successfully', 'success');
        } else {
          await Http.post('/employees', payload);
          Utils.showToast('Employee created successfully', 'success');
        }
        Utils.closeModal('employee-modal');
        Employees.loadEmployees();
      } catch (err) {
        Utils.showToast(err.message, 'error');
      }
    },

    deleteEmployee: async (empId) => {
      if (!confirm(`Are you sure you want to delete employee record ${empId}? This will remove associated attendance and payroll history.`)) {
        return;
      }

      try {
        await Http.delete(`/employees/${empId}`);
        Utils.showToast(`Employee ${empId} deleted`, 'success');
        Employees.loadEmployees();
      } catch (err) {
        Utils.showToast(err.message, 'error');
      }
    },

    viewProfile: async (empId) => {
      try {
        const res = await Http.get(`/employees/${empId}`);
        if (res.success && res.data) {
          const emp = res.data;
          const body = document.getElementById('profile-modal-body');
          body.innerHTML = `
            <div style="display:flex;align-items:center;gap:1.25rem;margin-bottom:1.5rem;padding-bottom:1.25rem;border-bottom:1px solid #e2e8f0;">
              <div style="width:64px;height:64px;border-radius:50%;background:#2563eb;color:#fff;display:flex;align-items:center;justify-content:center;font-size:1.75rem;font-weight:700;">
                ${emp.firstName.charAt(0)}
              </div>
              <div>
                <h3 style="font-size:1.25rem;font-weight:700;color:#0f172a;">${emp.firstName} ${emp.lastName}</h3>
                <div style="color:#64748b;font-size:0.88rem;">${emp.designation} • ${emp.departmentName}</div>
                <div style="margin-top:0.35rem;"><span class="badge ${emp.employmentStatus === 'Active' ? 'badge-success' : 'badge-warning'}"><span class="badge-dot"></span>${emp.employmentStatus}</span></div>
              </div>
            </div>
            <div class="form-grid" style="font-size:0.88rem;">
              <div><strong>Employee ID:</strong> <span style="color:#2563eb;font-weight:600;">${emp.employeeId}</span></div>
              <div><strong>Email:</strong> ${emp.email}</div>
              <div><strong>Phone:</strong> ${emp.phone}</div>
              <div><strong>Gender:</strong> ${emp.gender}</div>
              <div><strong>Joining Date:</strong> ${Utils.formatDate(emp.joiningDate)}</div>
              <div><strong>Basic Salary:</strong> ${Utils.formatCurrency(emp.basicSalary)} / month</div>
              <div><strong>Bank Name:</strong> ${emp.bankName}</div>
              <div><strong>Account Number:</strong> ${emp.maskedBankAccount || emp.bankAccountNo}</div>
              <div><strong>IFSC Code:</strong> ${emp.bankIfsc}</div>
              <div class="col-full"><strong>Address:</strong> ${emp.address || 'Not provided'}</div>
            </div>
          `;
          Utils.openModal('profile-modal');
        }
      } catch (e) {
        Utils.showToast('Could not load profile: ' + e.message, 'error');
      }
    }
  };

  // ------------------------------------------------------------------
  // ATTENDANCE MODULE
  // ------------------------------------------------------------------
  const Attendance = {
    init: async () => {
      const datePicker = document.getElementById('attendance-date-picker');
      if (datePicker) {
        datePicker.value = Utils.getTodayStr();
        datePicker.addEventListener('change', () => Attendance.loadDaily(datePicker.value));
      }

      await Attendance.loadDaily(datePicker ? datePicker.value : Utils.getTodayStr());

      const form = document.getElementById('attendance-mark-form');
      if (form) {
        form.addEventListener('submit', Attendance.handleMarkSubmit);
      }
    },

    loadDaily: async (date) => {
      try {
        const res = await Http.get(`/attendance?date=${encodeURIComponent(date)}`);
        if (res.success && res.data) {
          Attendance.renderTable(res.data.records || []);
          Attendance.renderCounts(res.data.counts || {});
        }
      } catch (e) {
        Utils.showToast('Error loading attendance: ' + e.message, 'error');
      }
    },

    renderCounts: (counts) => {
      const setVal = (id, val) => {
        const el = document.getElementById(id);
        if (el) el.textContent = val;
      };
      setVal('att-count-present', counts.Present || 0);
      setVal('att-count-half', counts['Half Day'] || 0);
      setVal('att-count-leave', counts.Leave || 0);
      setVal('att-count-absent', counts.Absent || 0);
    },

    renderTable: (records) => {
      const tbody = document.getElementById('attendance-table-body');
      if (!tbody) return;

      if (!records || records.length === 0) {
        tbody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:2rem;color:#94a3b8;">No attendance marked for this date. Click 'Mark Attendance' above to record.</td></tr>`;
        return;
      }

      tbody.innerHTML = records.map(r => {
        const statusBadge = r.status === 'Present' ? 'badge-success'
          : r.status === 'Half Day' ? 'badge-info'
          : r.status === 'Leave' ? 'badge-warning' : 'badge-danger';

        return `
          <tr>
            <td><strong style="color:#2563eb;">${r.employeeId}</strong></td>
            <td><strong>${r.employeeName}</strong></td>
            <td>${r.departmentName}</td>
            <td>${Utils.formatDate(r.attendanceDate)}</td>
            <td><span class="badge ${statusBadge}"><span class="badge-dot"></span>${r.status}</span></td>
            <td>${r.remarks || '-'}</td>
          </tr>
        `;
      }).join('');
    },

    openMarkModal: async () => {
      const select = document.getElementById('att-emp-select');
      if (select && select.children.length <= 1) {
        try {
          const res = await Http.get('/employees?status=Active');
          if (res.success && res.data) {
            select.innerHTML = '<option value="">-- Choose Employee --</option>';
            res.data.forEach(e => {
              select.innerHTML += `<option value="${e.employeeId}">${e.employeeId} - ${e.firstName} ${e.lastName} (${e.departmentName})</option>`;
            });
          }
        } catch (e) {
          console.warn('Could not populate employee dropdown', e);
        }
      }

      const dateField = document.getElementById('att-record-date');
      const mainPicker = document.getElementById('attendance-date-picker');
      if (dateField) dateField.value = mainPicker ? mainPicker.value : Utils.getTodayStr();

      Utils.openModal('attendance-modal');
    },

    handleMarkSubmit: async (e) => {
      e.preventDefault();
      const payload = {
        employeeId: document.getElementById('att-emp-select').value,
        attendanceDate: document.getElementById('att-record-date').value,
        status: document.getElementById('att-status-select').value,
        remarks: document.getElementById('att-remarks').value.trim()
      };

      try {
        await Http.post('/attendance', payload);
        Utils.showToast(`Attendance marked as ${payload.status} for ${payload.employeeId}`, 'success');
        Utils.closeModal('attendance-modal');
        const mainPicker = document.getElementById('attendance-date-picker');
        Attendance.loadDaily(mainPicker ? mainPicker.value : payload.attendanceDate);
      } catch (err) {
        Utils.showToast(err.message, 'error');
      }
    }
  };

  // ------------------------------------------------------------------
  // PAYROLL MODULE
  // ------------------------------------------------------------------
  const Payroll = {
    init: async () => {
      const monthFilter = document.getElementById('payroll-filter-month');
      if (monthFilter) {
        monthFilter.value = Utils.getCurrentMonthStr();
      }

      await Payroll.loadPayrolls();

      const filterMonth = document.getElementById('payroll-filter-month');
      const filterStatus = document.getElementById('payroll-filter-status');
      const applyFilter = () => {
        Payroll.loadPayrolls(
          filterMonth ? filterMonth.value : '',
          filterStatus ? filterStatus.value : ''
        );
      };
      if (filterMonth) filterMonth.addEventListener('change', applyFilter);
      if (filterStatus) filterStatus.addEventListener('change', applyFilter);

      const calcInputs = ['calc-emp-id', 'calc-month', 'calc-bonus', 'calc-overtime', 'calc-allowance', 'calc-deduction'];
      calcInputs.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', debounce(Payroll.previewCalculation, 300));
      });

      const form = document.getElementById('payroll-generate-form');
      if (form) form.addEventListener('submit', Payroll.handleGenerateSubmit);
    },

    loadPayrolls: async (month = '', status = '') => {
      try {
        if (!month) {
          const mf = document.getElementById('payroll-filter-month');
          if (mf && mf.value) month = mf.value;
        }

        let ep = '/payroll?';
        if (month) ep += `month=${encodeURIComponent(month)}&`;
        if (status) ep += `status=${encodeURIComponent(status)}&`;

        const res = await Http.get(ep);
        if (res.success && res.data) {
          Payroll.renderTable(res.data);
        }
      } catch (e) {
        Utils.showToast('Error loading payroll: ' + e.message, 'error');
      }
    },

    renderTable: (payrolls) => {
      const tbody = document.getElementById('payroll-table-body');
      if (!tbody) return;

      if (!payrolls || payrolls.length === 0) {
        tbody.innerHTML = `<tr><td colspan="9" style="text-align:center;padding:2rem;color:#94a3b8;">No payroll records found for this period. Click 'Generate Payroll' to process.</td></tr>`;
        return;
      }

      tbody.innerHTML = payrolls.map(p => {
        const isPaid = p.paymentStatus === 'Paid';
        const statusBadge = isPaid ? 'badge-success' : 'badge-warning';

        return `
          <tr>
            <td><strong style="color:#2563eb;">#${p.payrollId}</strong></td>
            <td>
              <div style="font-weight:600;color:#0f172a;">${p.employeeName}</div>
              <div style="font-size:0.75rem;color:#64748b;">${p.employeeId} • ${p.departmentName}</div>
            </td>
            <td><span class="badge badge-neutral">${p.salaryMonth}</span></td>
            <td>${Utils.formatCurrency(p.basicSalary)}</td>
            <td><strong>${Utils.formatCurrency(p.grossSalary)}</strong></td>
            <td style="color:#ef4444;">-${Utils.formatCurrency(p.totalDeductions)}</td>
            <td style="color:#10b981;font-weight:700;font-size:0.95rem;">${Utils.formatCurrency(p.netSalary)}</td>
            <td><span class="badge ${statusBadge}"><span class="badge-dot"></span>${p.paymentStatus}</span></td>
            <td>
              <div class="action-buttons">
                <a href="payslip.html?id=${p.payrollId}" class="icon-btn" title="View & Print Payslip">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
                </a>
                ${!isPaid ? `
                  <button class="icon-btn" title="Disburse Payment" onclick="App.Payroll.openPaymentModal(${p.payrollId}, '${p.employeeName}', ${p.netSalary})">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><rect x="1" y="4" width="22" height="16" rx="2" ry="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
                  </button>
                ` : ''}
                <button class="icon-btn delete" title="Delete Payroll" onclick="App.Payroll.deletePayroll(${p.payrollId})">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
                </button>
              </div>
            </td>
          </tr>
        `;
      }).join('');
    },

    openGenerateModal: async () => {
      const select = document.getElementById('calc-emp-id');
      if (select && select.children.length <= 1) {
        try {
          const res = await Http.get('/employees?status=Active');
          if (res.success && res.data) {
            select.innerHTML = '<option value="">-- Choose Employee --</option>';
            res.data.forEach(e => {
              select.innerHTML += `<option value="${e.employeeId}">${e.employeeId} - ${e.firstName} ${e.lastName} (${Utils.formatCurrency(e.basicSalary)})</option>`;
            });
          }
        } catch (e) {
          console.warn(e);
        }
      }

      document.getElementById('calc-month').value = Utils.getCurrentMonthStr();
      document.getElementById('calc-bonus').value = '0';
      document.getElementById('calc-overtime').value = '0';
      document.getElementById('calc-allowance').value = '0';
      document.getElementById('calc-deduction').value = '0';

      Payroll.previewCalculation();
      Utils.openModal('payroll-generate-modal');
    },

    previewCalculation: async () => {
      const empId = document.getElementById('calc-emp-id').value;
      const month = document.getElementById('calc-month').value;
      if (!empId || !month) return;

      const payload = {
        employeeId: empId,
        salaryMonth: month,
        bonus: parseFloat(document.getElementById('calc-bonus').value || 0),
        overtimePay: parseFloat(document.getElementById('calc-overtime').value || 0),
        otherAllowances: parseFloat(document.getElementById('calc-allowance').value || 0),
        otherDeductions: parseFloat(document.getElementById('calc-deduction').value || 0)
      };

      try {
        const res = await Http.post('/payroll/calculate', payload);
        if (res.success && res.data) {
          const p = res.data;
          document.getElementById('prev-basic').textContent = Utils.formatCurrency(p.basicSalary);
          document.getElementById('prev-hra').textContent = Utils.formatCurrency(p.hra);
          document.getElementById('prev-da').textContent = Utils.formatCurrency(p.da);
          document.getElementById('prev-gross').textContent = Utils.formatCurrency(p.grossSalary);
          document.getElementById('prev-pf').textContent = Utils.formatCurrency(p.pfDeduction);
          document.getElementById('prev-tax').textContent = Utils.formatCurrency(p.incomeTax);
          document.getElementById('prev-ptax').textContent = Utils.formatCurrency(p.profTax);
          document.getElementById('prev-leave').textContent = Utils.formatCurrency(p.leaveDeduction);
          document.getElementById('prev-deductions').textContent = Utils.formatCurrency(p.totalDeductions);
          document.getElementById('prev-net').textContent = Utils.formatCurrency(p.netSalary);
        }
      } catch (e) {
        console.warn('Preview calculation error:', e);
      }
    },

    handleGenerateSubmit: async (e) => {
      e.preventDefault();
      const payload = {
        employeeId: document.getElementById('calc-emp-id').value,
        salaryMonth: document.getElementById('calc-month').value,
        bonus: parseFloat(document.getElementById('calc-bonus').value || 0),
        overtimePay: parseFloat(document.getElementById('calc-overtime').value || 0),
        otherAllowances: parseFloat(document.getElementById('calc-allowance').value || 0),
        otherDeductions: parseFloat(document.getElementById('calc-deduction').value || 0)
      };

      try {
        const res = await Http.post('/payroll/generate', payload);
        Utils.showToast(res.message || 'Payroll generated successfully', 'success');
        Utils.closeModal('payroll-generate-modal');
        Payroll.loadPayrolls(payload.salaryMonth);
      } catch (err) {
        Utils.showToast(err.message, 'error');
      }
    },

    runBatchPayroll: async () => {
      const month = prompt('Enter Salary Month for Batch Payroll (YYYY-MM):', Utils.getCurrentMonthStr());
      if (!month) return;

      try {
        const res = await Http.post('/payroll/process-batch', { salaryMonth: month });
        Utils.showToast(res.message || 'Batch payroll executed', 'success');
        Payroll.loadPayrolls(month);
      } catch (err) {
        Utils.showToast(err.message, 'error');
      }
    },

    openPaymentModal: (payrollId, employeeName, amount) => {
      document.getElementById('pay-payroll-id').value = payrollId;
      document.getElementById('pay-emp-name').value = employeeName;
      document.getElementById('pay-amount').value = amount;
      document.getElementById('pay-date').value = Utils.getTodayStr();
      document.getElementById('pay-ref').value = `TRX-${Date.now().toString().slice(-8)}`;

      Utils.openModal('payment-record-modal');
    },

    deletePayroll: async (payrollId) => {
      if (!confirm(`Delete payroll record #${payrollId}?`)) return;
      try {
        await Http.delete(`/payroll/${payrollId}`);
        Utils.showToast('Payroll record deleted', 'success');
        Payroll.loadPayrolls();
      } catch (e) {
        Utils.showToast(e.message, 'error');
      }
    }
  };

  // ------------------------------------------------------------------
  // PAYSLIP MODULE
  // ------------------------------------------------------------------
  const Payslip = {
    init: async () => {
      const urlParams = new URLSearchParams(window.location.search);
      const payrollId = urlParams.get('id');

      // Populate selector dropdown for quick navigation
      try {
        const res = await Http.get('/payroll');
        if (res.success && res.data && res.data.length > 0) {
          const selector = document.getElementById('payslip-selector');
          if (selector) {
            selector.innerHTML = '<option value="">-- Switch Payslip --</option>';
            res.data.forEach(p => {
              const isSelected = payrollId && String(p.payrollId) === String(payrollId);
              selector.innerHTML += `<option value="${p.payrollId}" ${isSelected ? 'selected' : ''}>${p.salaryMonth} - ${p.employeeName} (${Utils.formatCurrency(p.netSalary)})</option>`;
            });
            selector.addEventListener('change', (e) => {
              if (e.target.value) Payslip.loadPayslip(e.target.value);
            });
          }

          if (payrollId) {
            await Payslip.loadPayslip(payrollId);
          } else {
            // Auto load first record
            await Payslip.loadPayslip(res.data[0].payrollId);
          }
        }
      } catch (e) {
        console.error(e);
      }
    },

    loadPayslip: async (payrollId) => {
      try {
        const res = await Http.get(`/payslips/${payrollId}`);
        if (res.success && res.data) {
          Payslip.render(res.data.company, res.data.payroll);
        }
      } catch (e) {
        Utils.showToast('Could not load payslip: ' + e.message, 'error');
      }
    },

    render: (company, p) => {
      const setVal = (id, val) => {
        const el = document.getElementById(id);
        if (el) el.textContent = val;
      };

      // Company info
      setVal('ps-company-name', company.companyName || 'Corporate Headquarters');
      setVal('ps-company-address', company.companyAddress || '');
      setVal('ps-company-contact', `${company.companyEmail || ''} | ${company.companyPhone || ''}`);

      // Month & Payslip Meta
      setVal('ps-month-badge', `Salary Month: ${p.salaryMonth}`);
      setVal('ps-emp-name', p.employeeName);
      setVal('ps-emp-id', p.employeeId);
      setVal('ps-department', p.departmentName);
      setVal('ps-designation', p.designation);
      setVal('ps-bank-name', p.bankName || 'HDFC Bank');
      setVal('ps-bank-acc', p.bankAccountNo ? '****' + String(p.bankAccountNo).slice(-4) : '****');
      setVal('ps-bank-ifsc', p.bankIfsc || 'N/A');
      setVal('ps-work-days', `${p.presentDays} / ${p.totalWorkingDays} Days`);

      // Earnings
      setVal('ps-basic', Utils.formatCurrency(p.basicSalary));
      setVal('ps-hra', Utils.formatCurrency(p.hra));
      setVal('ps-da', Utils.formatCurrency(p.da));
      setVal('ps-allowance', Utils.formatCurrency(p.otherAllowances));
      setVal('ps-bonus', Utils.formatCurrency(p.bonus));
      setVal('ps-overtime', Utils.formatCurrency(p.overtimePay));
      setVal('ps-gross', Utils.formatCurrency(p.grossSalary));

      // Deductions
      setVal('ps-pf', Utils.formatCurrency(p.pfDeduction));
      setVal('ps-tax', Utils.formatCurrency(p.incomeTax));
      setVal('ps-ptax', Utils.formatCurrency(p.profTax));
      setVal('ps-leave-deduct', Utils.formatCurrency(p.leaveDeduction));
      setVal('ps-other-deduct', Utils.formatCurrency(p.otherDeductions));
      setVal('ps-total-deductions', Utils.formatCurrency(p.totalDeductions));

      // Net Pay
      setVal('ps-net-pay', Utils.formatCurrency(p.netSalary));
      setVal('ps-net-words', Utils.numberToWords(p.netSalary));

      // Status
      const statusEl = document.getElementById('ps-payment-status');
      if (statusEl) {
        statusEl.className = `badge ${p.paymentStatus === 'Paid' ? 'badge-success' : 'badge-warning'}`;
        statusEl.innerHTML = `<span class="badge-dot"></span>${p.paymentStatus}`;
      }
    }
  };

  // ------------------------------------------------------------------
  // PAYMENT DISBURSEMENT MODULE
  // ------------------------------------------------------------------
  const Payments = {
    init: async () => {
      await Payments.loadPayments();

      const form = document.getElementById('payment-record-form');
      if (form) form.addEventListener('submit', Payments.handlePaymentSubmit);

      const filterMonth = document.getElementById('pay-filter-month');
      const filterMethod = document.getElementById('pay-filter-method');
      const applyFilters = () => {
        Payments.loadPayments(
          filterMonth ? filterMonth.value : '',
          filterMethod ? filterMethod.value : ''
        );
      };
      if (filterMonth) filterMonth.addEventListener('change', applyFilters);
      if (filterMethod) filterMethod.addEventListener('change', applyFilters);
    },

    loadPayments: async (month = '', method = '') => {
      try {
        let ep = '/payments?';
        if (month) ep += `month=${encodeURIComponent(month)}&`;
        if (method) ep += `method=${encodeURIComponent(method)}&`;

        const res = await Http.get(ep);
        if (res.success && res.data) {
          Payments.renderTable(res.data);
        }
      } catch (e) {
        Utils.showToast('Error loading payment history: ' + e.message, 'error');
      }
    },

    renderTable: (payments) => {
      const tbody = document.getElementById('payments-table-body');
      if (!tbody) return;

      if (!payments || payments.length === 0) {
        tbody.innerHTML = `<tr><td colspan="8" style="text-align:center;padding:2rem;color:#94a3b8;">No payment records found.</td></tr>`;
        return;
      }

      tbody.innerHTML = payments.map(p => `
        <tr>
          <td><strong style="color:#2563eb;">#${p.paymentId}</strong></td>
          <td>
            <div style="font-weight:600;color:#0f172a;">${p.employeeName}</div>
            <div style="font-size:0.75rem;color:#64748b;">${p.employeeId} • ${p.departmentName}</div>
          </td>
          <td><span class="badge badge-neutral">${p.salaryMonth}</span></td>
          <td>${Utils.formatDate(p.paymentDate)}</td>
          <td><strong style="color:#10b981;">${Utils.formatCurrency(p.amount)}</strong></td>
          <td><span class="badge badge-info">${p.paymentMethod}</span></td>
          <td><code>${p.transactionReference}</code></td>
          <td><span class="badge badge-success"><span class="badge-dot"></span>${p.status}</span></td>
        </tr>
      `).join('');
    },

    handlePaymentSubmit: async (e) => {
      e.preventDefault();
      const payload = {
        payrollId: parseInt(document.getElementById('pay-payroll-id').value),
        amount: parseFloat(document.getElementById('pay-amount').value),
        paymentDate: document.getElementById('pay-date').value,
        paymentMethod: document.getElementById('pay-method').value,
        transactionReference: document.getElementById('pay-ref').value.trim(),
        status: 'Success',
        notes: document.getElementById('pay-notes') ? document.getElementById('pay-notes').value.trim() : 'Disbursed'
      };

      try {
        const res = await Http.post('/payments', payload);
        Utils.showToast(res.message || 'Payment recorded successfully', 'success');
        Utils.closeModal('payment-record-modal');

        // Robust refresh for both pages
        if (document.getElementById('payroll-table-body')) {
          Payroll.loadPayrolls();
        }
        if (document.getElementById('payments-table-body')) {
          Payments.loadPayments();
        }
      } catch (err) {
        Utils.showToast(err.message, 'error');
      }
    }
  };

  // ------------------------------------------------------------------
  // REPORTS MODULE
  // ------------------------------------------------------------------
  const Reports = {
    init: async () => {
      const typeSelect = document.getElementById('report-type-select');
      const monthInput = document.getElementById('report-month-filter');

      if (monthInput) monthInput.value = Utils.getCurrentMonthStr();

      const loadCurrent = () => {
        const type = typeSelect ? typeSelect.value : 'payroll';
        const month = monthInput ? monthInput.value : '';
        Reports.generateReport(type, month);
      };

      if (typeSelect) typeSelect.addEventListener('change', loadCurrent);
      if (monthInput) monthInput.addEventListener('change', loadCurrent);

      loadCurrent();
    },

    generateReport: async (type, month) => {
      const titleEl = document.getElementById('report-title-display');
      const thead = document.getElementById('report-thead');
      const tbody = document.getElementById('report-tbody');

      try {
        if (type === 'payroll') {
          if (titleEl) titleEl.textContent = `Monthly Payroll Audit Report (${month || 'All'})`;
          thead.innerHTML = `
            <tr>
              <th>Payroll ID</th><th>Employee</th><th>Dept</th><th>Month</th><th>Basic</th><th>Gross</th><th>Deductions</th><th>Net Salary</th><th>Status</th>
            </tr>
          `;
          const res = await Http.get(`/reports/payroll?month=${encodeURIComponent(month)}`);
          if (res.success && res.data) {
            tbody.innerHTML = res.data.map(p => `
              <tr>
                <td>#${p.payrollId}</td>
                <td><strong>${p.employeeName}</strong> (${p.employeeId})</td>
                <td>${p.departmentName}</td>
                <td>${p.salaryMonth}</td>
                <td>${Utils.formatCurrency(p.basicSalary)}</td>
                <td>${Utils.formatCurrency(p.grossSalary)}</td>
                <td style="color:#ef4444;">${Utils.formatCurrency(p.totalDeductions)}</td>
                <td><strong>${Utils.formatCurrency(p.netSalary)}</strong></td>
                <td><span class="badge ${p.paymentStatus === 'Paid' ? 'badge-success' : 'badge-warning'}">${p.paymentStatus}</span></td>
              </tr>
            `).join('');
          }
        } else if (type === 'payments') {
          if (titleEl) titleEl.textContent = `Disbursement & Payment History Report (${month || 'All'})`;
          thead.innerHTML = `
            <tr>
              <th>Payment ID</th><th>Employee</th><th>Month</th><th>Payment Date</th><th>Amount</th><th>Method</th><th>Reference</th><th>Status</th>
            </tr>
          `;
          const res = await Http.get(`/reports/payments?month=${encodeURIComponent(month)}`);
          if (res.success && res.data) {
            tbody.innerHTML = res.data.map(p => `
              <tr>
                <td>#${p.paymentId}</td>
                <td><strong>${p.employeeName}</strong></td>
                <td>${p.salaryMonth}</td>
                <td>${Utils.formatDate(p.paymentDate)}</td>
                <td><strong>${Utils.formatCurrency(p.amount)}</strong></td>
                <td>${p.paymentMethod}</td>
                <td><code>${p.transactionReference}</code></td>
                <td><span class="badge badge-success">${p.status}</span></td>
              </tr>
            `).join('');
          }
        } else if (type === 'employees') {
          if (titleEl) titleEl.textContent = `Employee Compensation Master Report`;
          thead.innerHTML = `
            <tr>
              <th>ID</th><th>Full Name</th><th>Email</th><th>Phone</th><th>Department</th><th>Designation</th><th>Joining Date</th><th>Basic Salary</th><th>Status</th>
            </tr>
          `;
          const res = await Http.get('/reports/employees');
          if (res.success && res.data) {
            tbody.innerHTML = res.data.map(e => `
              <tr>
                <td><strong>${e.employeeId}</strong></td>
                <td>${e.firstName} ${e.lastName}</td>
                <td>${e.email}</td>
                <td>${e.phone}</td>
                <td>${e.departmentName}</td>
                <td>${e.designation}</td>
                <td>${Utils.formatDate(e.joiningDate)}</td>
                <td><strong>${Utils.formatCurrency(e.basicSalary)}</strong></td>
                <td><span class="badge ${e.employmentStatus === 'Active' ? 'badge-success' : 'badge-warning'}">${e.employmentStatus}</span></td>
              </tr>
            `).join('');
          }
        }
      } catch (e) {
        Utils.showToast('Error generating report: ' + e.message, 'error');
      }
    }
  };

  // Debounce helper
  function debounce(func, wait) {
    let timeout;
    return function (...args) {
      clearTimeout(timeout);
      timeout = setTimeout(() => func.apply(this, args), wait);
    };
  }

  // Public API surface
  return {
    Utils,
    Http,
    Auth,
    Dashboard,
    Employees,
    Attendance,
    Payroll,
    Payslip,
    Payments,
    Reports
  };
})();
