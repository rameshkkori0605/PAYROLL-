import sys
import os
import json
import mimetypes
from datetime import datetime, date
from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import urlparse, parse_qs

PORT = 8080
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
FRONTEND_DIR = os.path.join(BASE_DIR, "frontend")

# ---------------------------------------------------------------------
# IN-MEMORY DATABASE INITIALIZED WITH SAMPLE DATA
# ---------------------------------------------------------------------
DB = {
    "admins": [
        {
            "adminId": 1,
            "username": "admin",
            "password": "admin123",
            "fullName": "System Administrator",
            "email": "admin@payrollapp.com",
            "role": "SUPER_ADMIN",
            "roleDescription": "SUPER_ADMIN System Administrator"
        },
        {
            "adminId": 2,
            "username": "hr_admin",
            "password": "hr123",
            "fullName": "HR Executive Officer",
            "email": "hr@payrollapp.com",
            "role": "HR_ADMIN",
            "roleDescription": "HR_ADMIN System Administrator"
        }
    ],
    "departments": [
        {"departmentId": 1, "departmentName": "Engineering", "description": "Software QA & Infrastructure", "employeeCount": 3},
        {"departmentId": 2, "departmentName": "Human Resources", "description": "Talent & Relations", "employeeCount": 1},
        {"departmentId": 3, "departmentName": "Finance & Accounts", "description": "Budgeting & Audits", "employeeCount": 1},
        {"departmentId": 4, "departmentName": "Marketing & Sales", "description": "Outreach & Growth", "employeeCount": 1},
        {"departmentId": 5, "departmentName": "Operations", "description": "Logistics & Facilities", "employeeCount": 1}
    ],
    "company": {
        "settingId": 1,
        "companyName": "Apex Global Solutions Inc.",
        "companyAddress": "104 Innovate Boulevard, Tech Hub Park, Sector 5, Bangalore - 560100",
        "companyEmail": "payroll@apexsolutions.com",
        "companyPhone": "+91 80 4123 4567",
        "currencySymbol": "₹",
        "standardWorkingDays": 30,
        "defaultHraPct": 20.0,
        "defaultDaPct": 10.0,
        "defaultPfPct": 12.0,
        "defaultPtax": 200.0
    },
    "employees": [
        {
            "employeeId": "EMP1001",
            "firstName": "Aarav",
            "lastName": "Sharma",
            "email": "aarav.sharma@apexsolutions.com",
            "phone": "+91 9876543210",
            "gender": "Male",
            "address": "45 Indiranagar, 100ft Road, Bangalore",
            "departmentId": 1,
            "departmentName": "Engineering",
            "designation": "Senior Software Engineer",
            "joiningDate": "2023-03-15",
            "basicSalary": 75000.0,
            "bankName": "HDFC Bank",
            "bankAccountNo": "50100234891102",
            "maskedBankAccount": "**********1102",
            "bankIfsc": "HDFC0001234",
            "employmentStatus": "Active"
        },
        {
            "employeeId": "EMP1002",
            "firstName": "Priya",
            "lastName": "Nair",
            "email": "priya.nair@apexsolutions.com",
            "phone": "+91 9876543211",
            "gender": "Female",
            "address": "12 Koramangala 4th Block, Bangalore",
            "departmentId": 2,
            "departmentName": "Human Resources",
            "designation": "HR Business Partner",
            "joiningDate": "2023-06-01",
            "basicSalary": 58000.0,
            "bankName": "ICICI Bank",
            "bankAccountNo": "002101569842",
            "maskedBankAccount": "********9842",
            "bankIfsc": "ICIC0000021",
            "employmentStatus": "Active"
        },
        {
            "employeeId": "EMP1003",
            "firstName": "Rohan",
            "lastName": "Verma",
            "email": "rohan.verma@apexsolutions.com",
            "phone": "+91 9876543212",
            "gender": "Male",
            "address": "78 Whitefield Main Road, Bangalore",
            "departmentId": 1,
            "departmentName": "Engineering",
            "designation": "Full Stack Developer",
            "joiningDate": "2024-01-10",
            "basicSalary": 50000.0,
            "bankName": "State Bank of India",
            "bankAccountNo": "30491823901",
            "maskedBankAccount": "*******3901",
            "bankIfsc": "SBIN0004567",
            "employmentStatus": "Active"
        },
        {
            "employeeId": "EMP1004",
            "firstName": "Sneha",
            "lastName": "Patel",
            "email": "sneha.patel@apexsolutions.com",
            "phone": "+91 9876543213",
            "gender": "Female",
            "address": "22 HSR Layout Sector 2, Bangalore",
            "departmentId": 3,
            "departmentName": "Finance & Accounts",
            "designation": "Financial Analyst",
            "joiningDate": "2023-09-20",
            "basicSalary": 52000.0,
            "bankName": "Axis Bank",
            "bankAccountNo": "918020038910234",
            "maskedBankAccount": "***********0234",
            "bankIfsc": "UTIB0000123",
            "employmentStatus": "Active"
        },
        {
            "employeeId": "EMP1005",
            "firstName": "Vikram",
            "lastName": "Rao",
            "email": "vikram.rao@apexsolutions.com",
            "phone": "+91 9876543214",
            "gender": "Male",
            "address": "15 Jayanagar 9th Block, Bangalore",
            "departmentId": 4,
            "departmentName": "Marketing & Sales",
            "designation": "Growth Marketing Lead",
            "joiningDate": "2023-11-05",
            "basicSalary": 62000.0,
            "bankName": "Kotak Mahindra Bank",
            "bankAccountNo": "6712398401",
            "maskedBankAccount": "******8401",
            "bankIfsc": "KKBK0000456",
            "employmentStatus": "Active"
        },
        {
            "employeeId": "EMP1006",
            "firstName": "Ananya",
            "lastName": "Iyer",
            "email": "ananya.iyer@apexsolutions.com",
            "phone": "+91 9876543215",
            "gender": "Female",
            "address": "88 Malleshwaram 18th Cross, Bangalore",
            "departmentId": 1,
            "departmentName": "Engineering",
            "designation": "QA Automation Engineer",
            "joiningDate": "2024-04-01",
            "basicSalary": 45000.0,
            "bankName": "HDFC Bank",
            "bankAccountNo": "50100456789012",
            "maskedBankAccount": "**********9012",
            "bankIfsc": "HDFC0001234",
            "employmentStatus": "Active"
        },
        {
            "employeeId": "EMP1007",
            "firstName": "Karan",
            "lastName": "Mehta",
            "email": "karan.mehta@apexsolutions.com",
            "phone": "+91 9876543216",
            "gender": "Male",
            "address": "34 Bellandur Green Glen, Bangalore",
            "departmentId": 5,
            "departmentName": "Operations",
            "designation": "Operations Specialist",
            "joiningDate": "2024-02-15",
            "basicSalary": 40000.0,
            "bankName": "ICICI Bank",
            "bankAccountNo": "002105432198",
            "maskedBankAccount": "********2198",
            "bankIfsc": "ICIC0000021",
            "employmentStatus": "Active"
        }
    ],
    "attendance": [
        {"attendanceId": 1, "employeeId": "EMP1001", "employeeName": "Aarav Sharma", "departmentName": "Engineering", "attendanceDate": "2026-11-17", "status": "Present", "remarks": "Regular duty"},
        {"attendanceId": 2, "employeeId": "EMP1002", "employeeName": "Priya Nair", "departmentName": "Human Resources", "attendanceDate": "2026-11-17", "status": "Present", "remarks": "Regular duty"},
        {"attendanceId": 3, "employeeId": "EMP1003", "employeeName": "Rohan Verma", "departmentName": "Engineering", "attendanceDate": "2026-11-17", "status": "Half Day", "remarks": "Doctor appointment"},
        {"attendanceId": 4, "employeeId": "EMP1004", "employeeName": "Sneha Patel", "departmentName": "Finance & Accounts", "attendanceDate": "2026-11-17", "status": "Present", "remarks": "Regular duty"},
        {"attendanceId": 5, "employeeId": "EMP1005", "employeeName": "Vikram Rao", "departmentName": "Marketing & Sales", "attendanceDate": "2026-11-17", "status": "Leave", "remarks": "Casual leave approved"},
        {"attendanceId": 6, "employeeId": "EMP1006", "employeeName": "Ananya Iyer", "departmentName": "Engineering", "attendanceDate": "2026-11-17", "status": "Present", "remarks": "Regular duty"},
        {"attendanceId": 7, "employeeId": "EMP1007", "employeeName": "Karan Mehta", "departmentName": "Operations", "attendanceDate": "2026-11-17", "status": "Absent", "remarks": "Uninformed"}
    ],
    "payroll": [
        {
            "payrollId": 1,
            "employeeId": "EMP1001",
            "employeeName": "Aarav Sharma",
            "departmentName": "Engineering",
            "designation": "Senior Software Engineer",
            "bankName": "HDFC Bank",
            "bankAccountNo": "50100234891102",
            "bankIfsc": "HDFC0001234",
            "salaryMonth": "2026-11",
            "totalWorkingDays": 30,
            "presentDays": 30.0,
            "paidLeaves": 0.0,
            "unpaidAbsentDays": 0.0,
            "basicSalary": 75000.0,
            "hra": 15000.0,
            "da": 7500.0,
            "otherAllowances": 3000.0,
            "bonus": 5000.0,
            "overtimePay": 0.0,
            "grossSalary": 100500.0,
            "pfDeduction": 9000.0,
            "profTax": 200.0,
            "incomeTax": 5025.0,
            "leaveDeduction": 0.0,
            "otherDeductions": 0.0,
            "totalDeductions": 14225.0,
            "netSalary": 86275.0,
            "paymentStatus": "Paid"
        },
        {
            "payrollId": 2,
            "employeeId": "EMP1002",
            "employeeName": "Priya Nair",
            "departmentName": "Human Resources",
            "designation": "HR Business Partner",
            "bankName": "ICICI Bank",
            "bankAccountNo": "002101569842",
            "bankIfsc": "ICIC0000021",
            "salaryMonth": "2026-11",
            "totalWorkingDays": 30,
            "presentDays": 29.5,
            "paidLeaves": 0.0,
            "unpaidAbsentDays": 0.5,
            "basicSalary": 58000.0,
            "hra": 11600.0,
            "da": 5800.0,
            "otherAllowances": 2500.0,
            "bonus": 0.0,
            "overtimePay": 0.0,
            "grossSalary": 77900.0,
            "pfDeduction": 6960.0,
            "profTax": 200.0,
            "incomeTax": 3895.0,
            "leaveDeduction": 966.67,
            "otherDeductions": 0.0,
            "totalDeductions": 12021.67,
            "netSalary": 65878.33,
            "paymentStatus": "Paid"
        },
        {
            "payrollId": 3,
            "employeeId": "EMP1003",
            "employeeName": "Rohan Verma",
            "departmentName": "Engineering",
            "designation": "Full Stack Developer",
            "bankName": "State Bank of India",
            "bankAccountNo": "30491823901",
            "bankIfsc": "SBIN0004567",
            "salaryMonth": "2026-11",
            "totalWorkingDays": 30,
            "presentDays": 29.0,
            "paidLeaves": 0.0,
            "unpaidAbsentDays": 1.0,
            "basicSalary": 50000.0,
            "hra": 10000.0,
            "da": 5000.0,
            "otherAllowances": 2000.0,
            "bonus": 0.0,
            "overtimePay": 1500.0,
            "grossSalary": 68500.0,
            "pfDeduction": 6000.0,
            "profTax": 200.0,
            "incomeTax": 3425.0,
            "leaveDeduction": 1666.67,
            "otherDeductions": 0.0,
            "totalDeductions": 11291.67,
            "netSalary": 57208.33,
            "paymentStatus": "Paid"
        },
        {
            "payrollId": 4,
            "employeeId": "EMP1004",
            "employeeName": "Sneha Patel",
            "departmentName": "Finance & Accounts",
            "designation": "Financial Analyst",
            "bankName": "Axis Bank",
            "bankAccountNo": "918020038910234",
            "bankIfsc": "UTIB0000123",
            "salaryMonth": "2026-11",
            "totalWorkingDays": 30,
            "presentDays": 30.0,
            "paidLeaves": 0.0,
            "unpaidAbsentDays": 0.0,
            "basicSalary": 52000.0,
            "hra": 10400.0,
            "da": 5200.0,
            "otherAllowances": 2000.0,
            "bonus": 2500.0,
            "overtimePay": 0.0,
            "grossSalary": 72100.0,
            "pfDeduction": 6240.0,
            "profTax": 200.0,
            "incomeTax": 3605.0,
            "leaveDeduction": 0.0,
            "otherDeductions": 0.0,
            "totalDeductions": 10045.0,
            "netSalary": 62055.0,
            "paymentStatus": "Pending"
        },
        {
            "payrollId": 5,
            "employeeId": "EMP1005",
            "employeeName": "Vikram Rao",
            "departmentName": "Marketing & Sales",
            "designation": "Growth Marketing Lead",
            "bankName": "Kotak Mahindra Bank",
            "bankAccountNo": "6712398401",
            "bankIfsc": "KKBK0000456",
            "salaryMonth": "2026-11",
            "totalWorkingDays": 30,
            "presentDays": 29.0,
            "paidLeaves": 1.0,
            "unpaidAbsentDays": 0.0,
            "basicSalary": 62000.0,
            "hra": 12400.0,
            "da": 6200.0,
            "otherAllowances": 3000.0,
            "bonus": 4000.0,
            "overtimePay": 0.0,
            "grossSalary": 87600.0,
            "pfDeduction": 7440.0,
            "profTax": 200.0,
            "incomeTax": 4380.0,
            "leaveDeduction": 0.0,
            "otherDeductions": 0.0,
            "totalDeductions": 12020.0,
            "netSalary": 75580.0,
            "paymentStatus": "Pending"
        }
    ],
    "payments": [
        {
            "paymentId": 1,
            "payrollId": 1,
            "employeeId": "EMP1001",
            "employeeName": "Aarav Sharma",
            "departmentName": "Engineering",
            "salaryMonth": "2026-11",
            "paymentDate": "2026-11-15",
            "amount": 86275.0,
            "paymentMethod": "Bank Transfer",
            "transactionReference": "NEFT-HDFC-9842104921",
            "status": "Success",
            "notes": "November Salary credited via NEFT"
        },
        {
            "paymentId": 2,
            "payrollId": 2,
            "employeeId": "EMP1002",
            "employeeName": "Priya Nair",
            "departmentName": "Human Resources",
            "salaryMonth": "2026-11",
            "paymentDate": "2026-11-15",
            "amount": 65878.33,
            "paymentMethod": "Bank Transfer",
            "transactionReference": "IMPS-ICIC-8723419042",
            "status": "Success",
            "notes": "November Salary credited via IMPS"
        },
        {
            "paymentId": 3,
            "payrollId": 3,
            "employeeId": "EMP1003",
            "employeeName": "Rohan Verma",
            "departmentName": "Engineering",
            "salaryMonth": "2026-11",
            "paymentDate": "2026-11-15",
            "amount": 57208.33,
            "paymentMethod": "UPI",
            "transactionReference": "UPI-SBIN-5412984120",
            "status": "Success",
            "notes": "November Salary credited via UPI"
        }
    ],
    "currentSessionUser": {
        "adminId": 1,
        "username": "admin",
        "fullName": "System Administrator",
        "email": "admin@payrollapp.com",
        "role": "SUPER_ADMIN",
        "roleDescription": "SUPER_ADMIN System Administrator"
    }
}

class PayrollHandler(BaseHTTPRequestHandler):

    def send_json(self, status_code, success, message, data=None):
        self.send_response(status_code)
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")
        self.end_headers()
        payload = {"success": success, "message": message}
        if data is not None:
            payload["data"] = data
        self.wfile.write(json.dumps(payload, default=str).encode("utf-8"))

    def do_OPTIONS(self):
        self.send_response(200)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")
        self.end_headers()

    def do_GET(self):
        parsed = urlparse(self.path)
        path = parsed.path
        query = parse_qs(parsed.query)

        # Normalize path if prefixed with /payroll
        api_path = path
        if api_path.startswith("/payroll/"):
            api_path = api_path[len("/payroll"):]

        # Handle API routes
        if api_path.startswith("/api/"):
            self.handle_api_get(api_path, query)
            return

        # Handle static files
        self.serve_static_file(api_path)

    def do_POST(self):
        parsed = urlparse(self.path)
        api_path = parsed.path
        if api_path.startswith("/payroll/"):
            api_path = api_path[len("/payroll"):]

        content_length = int(self.headers.get("Content-Length", 0))
        body = self.rfile.read(content_length).decode("utf-8") if content_length > 0 else "{}"
        try:
            data = json.loads(body) if body else {}
        except Exception:
            data = {}

        self.handle_api_post(api_path, data)

    def do_PUT(self):
        parsed = urlparse(self.path)
        api_path = parsed.path
        if api_path.startswith("/payroll/"):
            api_path = api_path[len("/payroll"):]

        content_length = int(self.headers.get("Content-Length", 0))
        body = self.rfile.read(content_length).decode("utf-8") if content_length > 0 else "{}"
        try:
            data = json.loads(body) if body else {}
        except Exception:
            data = {}

        self.handle_api_put(api_path, data)

    def do_DELETE(self):
        parsed = urlparse(self.path)
        api_path = parsed.path
        if api_path.startswith("/payroll/"):
            api_path = api_path[len("/payroll"):]

        self.handle_api_delete(api_path)

    # -----------------------------------------------------------------
    # STATIC FILE SERVER
    # -----------------------------------------------------------------
    def serve_static_file(self, rel_path):
        if rel_path in ["", "/", "/payroll", "/payroll/"]:
            rel_path = "/index.html"

        if rel_path.startswith("/"):
            rel_path = rel_path[1:]

        file_path = os.path.join(FRONTEND_DIR, rel_path)

        if not os.path.exists(file_path) or os.path.isdir(file_path):
            file_path = os.path.join(FRONTEND_DIR, "index.html")

        mime_type, _ = mimetypes.guess_type(file_path)
        if not mime_type:
            mime_type = "application/octet-stream"

        try:
            with open(file_path, "rb") as f:
                content = f.read()
            self.send_response(200)
            self.send_header("Content-Type", f"{mime_type}; charset=utf-8")
            self.send_header("Content-Length", str(len(content)))
            self.end_headers()
            self.wfile.write(content)
        except Exception as e:
            self.send_response(500)
            self.end_headers()
            self.wfile.write(str(e).encode())

    # -----------------------------------------------------------------
    # API GET ROUTING
    # -----------------------------------------------------------------
    def handle_api_get(self, path, query):
        if path in ["/api/auth/status", "/api/auth/me"]:
            if DB["currentSessionUser"]:
                self.send_json(200, True, "Active session detected", DB["currentSessionUser"])
            else:
                self.send_json(401, False, "No active session")
            return

        if path == "/api/departments":
            self.send_json(200, True, "Departments loaded", DB["departments"])
            return

        if path == "/api/dashboard":
            # Compute dynamic statistics
            total_emp = len(DB["employees"])
            active_emp = len([e for e in DB["employees"] if e["employmentStatus"] == "Active"])
            total_payroll = sum(p["netSalary"] for p in DB["payroll"])
            paid_salaries = sum(p["netSalary"] for p in DB["payroll"] if p["paymentStatus"] == "Paid")
            pending_amount = sum(p["netSalary"] for p in DB["payroll"] if p["paymentStatus"] == "Pending")
            pending_count = len([p for p in DB["payroll"] if p["paymentStatus"] == "Pending"])

            att_today = [a for a in DB["attendance"] if a.get("attendanceDate") == "2026-11-17"]
            att_summary = {
                "present": len([a for a in att_today if a["status"] == "Present"]),
                "halfDay": len([a for a in att_today if a["status"] == "Half Day"]),
                "leave": len([a for a in att_today if a["status"] == "Leave"]),
                "absent": len([a for a in att_today if a["status"] == "Absent"])
            }

            dept_stats = []
            for d in DB["departments"]:
                cnt = len([e for e in DB["employees"] if e["departmentId"] == d["departmentId"]])
                dept_stats.append({"department": d["departmentName"], "count": cnt})

            payroll_trend = [
                {"month": "2026-08", "gross": 340000.0, "net": 298000.0, "count": 6},
                {"month": "2026-09", "gross": 380000.0, "net": 332000.0, "count": 7},
                {"month": "2026-10", "gross": 406600.0, "net": 346998.0, "count": 7},
                {"month": "2026-11", "gross": sum(p["grossSalary"] for p in DB["payroll"]), "net": total_payroll, "count": len(DB["payroll"])}
            ]

            recent_activities = []
            for pay in sorted(DB["payments"], key=lambda x: x["paymentDate"], reverse=True)[:5]:
                recent_activities.append({
                    "title": f"Salary Disbursed to {pay['employeeName']}",
                    "subtitle": f"Month: {pay['salaryMonth']} via {pay['paymentMethod']}",
                    "amount": pay["amount"],
                    "date": pay["paymentDate"]
                })

            data = {
                "totalEmployees": total_emp,
                "activeEmployees": active_emp,
                "currentMonth": "2026-11",
                "activePayrollMonth": "2026-11",
                "totalMonthlyPayroll": round(total_payroll, 2),
                "totalPaidSalaries": round(paid_salaries, 2),
                "pendingAmount": round(pending_amount, 2),
                "pendingPaymentsCount": pending_count,
                "attendanceSummary": att_summary,
                "departmentStats": dept_stats,
                "payrollTrend": payroll_trend,
                "recentActivities": recent_activities
            }
            self.send_json(200, True, "Dashboard loaded", data)
            return

        if path == "/api/employees":
            q = query.get("query", [""])[0].lower()
            dept_id = query.get("departmentId", [""])[0]
            status = query.get("status", [""])[0]

            filtered = DB["employees"]
            if q:
                filtered = [e for e in filtered if q in e["employeeId"].lower() or q in e["firstName"].lower() or q in e["lastName"].lower() or q in e["email"].lower() or q in e["designation"].lower()]
            if dept_id:
                filtered = [e for e in filtered if str(e["departmentId"]) == str(dept_id)]
            if status and status.lower() != "all":
                filtered = [e for e in filtered if e["employmentStatus"].lower() == status.lower()]

            self.send_json(200, True, "Employees loaded", filtered)
            return

        if path == "/api/employees/next-id":
            ids = [int(e["employeeId"][3:]) for e in DB["employees"] if e["employeeId"].startswith("EMP")]
            next_num = max(ids) + 1 if ids else 1001
            self.send_json(200, True, "Next ID", {"nextEmployeeId": f"EMP{next_num:04d}"})
            return

        if path.startswith("/api/employees/"):
            emp_id = path.split("/")[-1]
            emp = next((e for e in DB["employees"] if e["employeeId"] == emp_id), None)
            if emp:
                self.send_json(200, True, "Employee found", emp)
            else:
                self.send_json(404, False, "Employee not found")
            return

        if path == "/api/attendance":
            dt = query.get("date", ["2026-11-17"])[0]
            emp_id = query.get("employeeId", [""])[0]
            month = query.get("month", [""])[0]

            if emp_id and month:
                recs = [a for a in DB["attendance"] if a["employeeId"] == emp_id and a["attendanceDate"].startswith(month)]
                self.send_json(200, True, "Monthly attendance", recs)
            else:
                recs = [a for a in DB["attendance"] if a["attendanceDate"] == dt]
                counts = {
                    "Present": len([a for a in recs if a["status"] == "Present"]),
                    "Half Day": len([a for a in recs if a["status"] == "Half Day"]),
                    "Leave": len([a for a in recs if a["status"] == "Leave"]),
                    "Absent": len([a for a in recs if a["status"] == "Absent"])
                }
                self.send_json(200, True, "Daily attendance", {"date": dt, "records": recs, "counts": counts})
            return

        if path == "/api/attendance/summary":
            emp_id = query.get("employeeId", [""])[0]
            month = query.get("month", [""])[0]
            recs = [a for a in DB["attendance"] if a["employeeId"] == emp_id and a["attendanceDate"].startswith(month)]
            present = len([a for a in recs if a["status"] == "Present"])
            half = len([a for a in recs if a["status"] == "Half Day"])
            leave = len([a for a in recs if a["status"] == "Leave"])
            absent = len([a for a in recs if a["status"] == "Absent"])
            effective = present + (half * 0.5)
            self.send_json(200, True, "Summary", {
                "effectivePresentDays": effective,
                "paidLeaves": leave,
                "unpaidAbsentDays": absent + (half * 0.5),
                "attendancePercentage": round((effective / 30.0) * 100, 2)
            })
            return

        if path == "/api/payroll":
            month = query.get("month", [""])[0]
            status = query.get("status", [""])[0]
            filtered = DB["payroll"]
            if month:
                filtered = [p for p in filtered if p["salaryMonth"] == month]
            if status and status.lower() != "all":
                filtered = [p for p in filtered if p["paymentStatus"].lower() == status.lower()]
            self.send_json(200, True, "Payrolls loaded", filtered)
            return

        if path.startswith("/api/payroll/"):
            p_id = int(path.split("/")[-1])
            p = next((x for x in DB["payroll"] if x["payrollId"] == p_id), None)
            if p:
                self.send_json(200, True, "Payroll loaded", p)
            else:
                self.send_json(404, False, "Payroll not found")
            return

        if path.startswith("/api/payslips/"):
            p_id = int(path.split("/")[-1])
            p = next((x for x in DB["payroll"] if x["payrollId"] == p_id), None)
            if p:
                self.send_json(200, True, "Payslip loaded", {"company": DB["company"], "payroll": p})
            else:
                self.send_json(404, False, "Payslip not found")
            return

        if path == "/api/payments":
            month = query.get("month", [""])[0]
            method = query.get("method", [""])[0]
            filtered = DB["payments"]
            if month:
                filtered = [pay for pay in filtered if pay["salaryMonth"] == month]
            if method and method.lower() != "all":
                filtered = [pay for pay in filtered if pay["paymentMethod"].lower() == method.lower()]
            self.send_json(200, True, "Payments loaded", filtered)
            return

        if path == "/api/reports/payroll":
            month = query.get("month", [""])[0]
            filtered = DB["payroll"]
            if month:
                filtered = [p for p in filtered if p["salaryMonth"] == month]
            self.send_json(200, True, "Payroll report", filtered)
            return

        if path == "/api/reports/payments":
            month = query.get("month", [""])[0]
            filtered = DB["payments"]
            if month:
                filtered = [p for p in filtered if p["salaryMonth"] == month]
            self.send_json(200, True, "Payment report", filtered)
            return

        if path == "/api/reports/employees":
            self.send_json(200, True, "Employee report", DB["employees"])
            return

        self.send_json(404, False, "Endpoint not found")

    # -----------------------------------------------------------------
    # API POST ROUTING
    # -----------------------------------------------------------------
    def handle_api_post(self, path, data):
        if path == "/api/auth/login":
            u = data.get("username", "").strip()
            p = data.get("password", "")
            admin = next((a for a in DB["admins"] if a["username"] == u and a["password"] == p), None)
            if admin:
                DB["currentSessionUser"] = {
                    "adminId": admin["adminId"],
                    "username": admin["username"],
                    "fullName": admin["fullName"],
                    "email": admin["email"],
                    "role": admin["role"],
                    "roleDescription": admin["roleDescription"]
                }
                self.send_json(200, True, f"Welcome, {admin['fullName']}!", DB["currentSessionUser"])
            else:
                self.send_json(401, False, "Invalid username or password.")
            return

        if path == "/api/auth/logout":
            DB["currentSessionUser"] = None
            self.send_json(200, True, "Logged out successfully")
            return

        if path == "/api/employees":
            emp_id = data.get("employeeId")
            if not emp_id:
                ids = [int(e["employeeId"][3:]) for e in DB["employees"] if e["employeeId"].startswith("EMP")]
                emp_id = f"EMP{max(ids)+1:04d}" if ids else "EMP1001"
            data["employeeId"] = emp_id

            # Join department name
            dept = next((d for d in DB["departments"] if d["departmentId"] == int(data.get("departmentId", 1))), None)
            data["departmentName"] = dept["departmentName"] if dept else "General"
            data["basicSalary"] = float(data.get("basicSalary", 50000))

            acc = str(data.get("bankAccountNo", "0000"))
            data["maskedBankAccount"] = ("*" * max(0, len(acc) - 4)) + acc[-4:]

            DB["employees"].append(data)
            self.send_json(201, True, f"Employee {emp_id} created successfully", data)
            return

        if path.startswith("/api/employees/"):
            # Update employee via POST
            emp_id = path.split("/")[-1]
            for i, emp in enumerate(DB["employees"]):
                if emp["employeeId"] == emp_id:
                    data["employeeId"] = emp_id
                    dept = next((d for d in DB["departments"] if d["departmentId"] == int(data.get("departmentId", emp["departmentId"]))), None)
                    data["departmentName"] = dept["departmentName"] if dept else emp["departmentName"]
                    acc = str(data.get("bankAccountNo", emp["bankAccountNo"]))
                    data["maskedBankAccount"] = ("*" * max(0, len(acc) - 4)) + acc[-4:]
                    DB["employees"][i].update(data)
                    self.send_json(200, True, f"Employee {emp_id} updated", DB["employees"][i])
                    return
            self.send_json(404, False, "Employee not found")
            return

        if path == "/api/attendance":
            emp_id = data.get("employeeId")
            dt = data.get("attendanceDate")
            status = data.get("status")
            remarks = data.get("remarks", "")

            emp = next((e for e in DB["employees"] if e["employeeId"] == emp_id), None)
            emp_name = f"{emp['firstName']} {emp['lastName']}" if emp else emp_id
            dept_name = emp["departmentName"] if emp else "Engineering"

            # Upsert
            existing = next((a for a in DB["attendance"] if a["employeeId"] == emp_id and a["attendanceDate"] == dt), None)
            if existing:
                existing["status"] = status
                existing["remarks"] = remarks
                self.send_json(200, True, f"Attendance updated for {emp_id}", existing)
            else:
                new_id = max([a["attendanceId"] for a in DB["attendance"]] or [0]) + 1
                new_att = {
                    "attendanceId": new_id,
                    "employeeId": emp_id,
                    "employeeName": emp_name,
                    "departmentName": dept_name,
                    "attendanceDate": dt,
                    "status": status,
                    "remarks": remarks
                }
                DB["attendance"].append(new_att)
                self.send_json(200, True, f"Attendance marked for {emp_id}", new_att)
            return

        if path in ["/api/payroll/calculate", "/api/payroll/generate"]:
            emp_id = data.get("employeeId")
            month = data.get("salaryMonth", "2026-11")
            emp = next((e for e in DB["employees"] if e["employeeId"] == emp_id), None)
            if not emp:
                self.send_json(400, False, "Employee not found")
                return

            basic = float(emp["basicSalary"])
            hra = round(basic * 0.20, 2)
            da = round(basic * 0.10, 2)
            bonus = float(data.get("bonus", 0.0))
            ot = float(data.get("overtimePay", 0.0))
            allow = float(data.get("otherAllowances", 0.0))
            gross = round(basic + hra + da + bonus + ot + allow, 2)

            pf = round(basic * 0.12, 2)
            ptax = 200.0
            tax = round(gross * 0.05, 2)
            deduct = float(data.get("otherDeductions", 0.0))
            leave_ded = 0.0

            total_ded = round(pf + ptax + tax + deduct + leave_ded, 2)
            net = round(gross - total_ded, 2)

            res_p = {
                "payrollId": max([p["payrollId"] for p in DB["payroll"]] or [0]) + 1,
                "employeeId": emp_id,
                "employeeName": f"{emp['firstName']} {emp['lastName']}",
                "departmentName": emp["departmentName"],
                "designation": emp["designation"],
                "bankName": emp["bankName"],
                "bankAccountNo": emp["bankAccountNo"],
                "bankIfsc": emp["bankIfsc"],
                "salaryMonth": month,
                "totalWorkingDays": 30,
                "presentDays": 30.0,
                "paidLeaves": 0.0,
                "unpaidAbsentDays": 0.0,
                "basicSalary": basic,
                "hra": hra,
                "da": da,
                "otherAllowances": allow,
                "bonus": bonus,
                "overtimePay": ot,
                "grossSalary": gross,
                "pfDeduction": pf,
                "profTax": ptax,
                "incomeTax": tax,
                "leaveDeduction": leave_ded,
                "otherDeductions": deduct,
                "totalDeductions": total_ded,
                "netSalary": net,
                "paymentStatus": "Pending"
            }

            if path == "/api/payroll/calculate":
                self.send_json(200, True, "Preview calculated", res_p)
            else:
                # Check duplicate
                if any(p["employeeId"] == emp_id and p["salaryMonth"] == month for p in DB["payroll"]):
                    self.send_json(400, False, f"Payroll record already exists for {emp_id} in {month}")
                    return
                DB["payroll"].append(res_p)
                self.send_json(201, True, f"Payroll generated for {res_p['employeeName']}", res_p)
            return

        if path == "/api/payroll/process-batch":
            month = data.get("salaryMonth", "2026-11")
            count = 0
            for emp in DB["employees"]:
                if emp["employmentStatus"] == "Active":
                    if not any(p["employeeId"] == emp["employeeId"] and p["salaryMonth"] == month for p in DB["payroll"]):
                        basic = float(emp["basicSalary"])
                        hra = round(basic * 0.20, 2)
                        da = round(basic * 0.10, 2)
                        gross = round(basic + hra + da, 2)
                        pf = round(basic * 0.12, 2)
                        ptax = 200.0
                        tax = round(gross * 0.05, 2)
                        tot_ded = round(pf + ptax + tax, 2)
                        net = round(gross - tot_ded, 2)

                        new_p = {
                            "payrollId": max([p["payrollId"] for p in DB["payroll"]] or [0]) + 1,
                            "employeeId": emp["employeeId"],
                            "employeeName": f"{emp['firstName']} {emp['lastName']}",
                            "departmentName": emp["departmentName"],
                            "designation": emp["designation"],
                            "bankName": emp["bankName"],
                            "bankAccountNo": emp["bankAccountNo"],
                            "bankIfsc": emp["bankIfsc"],
                            "salaryMonth": month,
                            "totalWorkingDays": 30,
                            "presentDays": 30.0,
                            "paidLeaves": 0.0,
                            "unpaidAbsentDays": 0.0,
                            "basicSalary": basic,
                            "hra": hra,
                            "da": da,
                            "otherAllowances": 0.0,
                            "bonus": 0.0,
                            "overtimePay": 0.0,
                            "grossSalary": gross,
                            "pfDeduction": pf,
                            "profTax": ptax,
                            "incomeTax": tax,
                            "leaveDeduction": 0.0,
                            "otherDeductions": 0.0,
                            "totalDeductions": tot_ded,
                            "netSalary": net,
                            "paymentStatus": "Pending"
                        }
                        DB["payroll"].append(new_p)
                        count += 1
            self.send_json(200, True, f"Batch payroll processed. Created {count} records.", {"generatedCount": count})
            return

        if path == "/api/payments":
            payroll_id = int(data.get("payrollId"))
            pr = next((p for p in DB["payroll"] if p["payrollId"] == payroll_id), None)
            if not pr:
                self.send_json(400, False, "Payroll not found")
                return

            new_id = max([pay["paymentId"] for pay in DB["payments"]] or [0]) + 1
            pay_record = {
                "paymentId": new_id,
                "payrollId": payroll_id,
                "employeeId": pr["employeeId"],
                "employeeName": pr["employeeName"],
                "departmentName": pr["departmentName"],
                "salaryMonth": pr["salaryMonth"],
                "paymentDate": data.get("paymentDate", "2026-11-17"),
                "amount": float(data.get("amount", pr["netSalary"])),
                "paymentMethod": data.get("paymentMethod", "Bank Transfer"),
                "transactionReference": data.get("transactionReference", f"TRX-{new_id:06d}"),
                "status": "Success",
                "notes": data.get("notes", "Disbursed")
            }
            DB["payments"].append(pay_record)
            pr["paymentStatus"] = "Paid"
            self.send_json(201, True, f"Payment of ₹{pay_record['amount']} recorded. Payroll marked as Paid.", pay_record)
            return

        self.send_json(404, False, "Endpoint not found")

    # -----------------------------------------------------------------
    # API PUT ROUTING
    # -----------------------------------------------------------------
    def handle_api_put(self, path, data):
        if path.startswith("/api/employees/"):
            emp_id = path.split("/")[-1]
            for i, emp in enumerate(DB["employees"]):
                if emp["employeeId"] == emp_id:
                    data["employeeId"] = emp_id
                    dept = next((d for d in DB["departments"] if d["departmentId"] == int(data.get("departmentId", emp["departmentId"]))), None)
                    data["departmentName"] = dept["departmentName"] if dept else emp["departmentName"]
                    acc = str(data.get("bankAccountNo", emp["bankAccountNo"]))
                    data["maskedBankAccount"] = ("*" * max(0, len(acc) - 4)) + acc[-4:]
                    DB["employees"][i].update(data)
                    self.send_json(200, True, f"Employee {emp_id} updated", DB["employees"][i])
                    return
            self.send_json(404, False, "Employee not found")
            return

        self.send_json(404, False, "Endpoint not found")

    # -----------------------------------------------------------------
    # API DELETE ROUTING
    # -----------------------------------------------------------------
    def handle_api_delete(self, path):
        if path.startswith("/api/employees/"):
            emp_id = path.split("/")[-1]
            orig_len = len(DB["employees"])
            DB["employees"] = [e for e in DB["employees"] if e["employeeId"] != emp_id]
            if len(DB["employees"]) < orig_len:
                self.send_json(200, True, f"Employee {emp_id} deleted")
            else:
                self.send_json(404, False, "Employee not found")
            return

        if path.startswith("/api/payroll/"):
            p_id = int(path.split("/")[-1])
            orig_len = len(DB["payroll"])
            DB["payroll"] = [p for p in DB["payroll"] if p["payrollId"] != p_id]
            if len(DB["payroll"]) < orig_len:
                self.send_json(200, True, f"Payroll {p_id} deleted")
            else:
                self.send_json(404, False, "Payroll not found")
            return

        self.send_json(404, False, "Endpoint not found")


if __name__ == "__main__":
    server_address = ("", PORT)
    httpd = HTTPServer(server_address, PayrollHandler)
    print(f"===========================================================")
    print(f" PAYROLL MANAGEMENT SYSTEM - LIVE SERVER RUNNING")
    print(f" Access URL: http://localhost:{PORT}/")
    print(f" Or:        http://localhost:{PORT}/payroll/")
    print(f"===========================================================")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\nStopping server...")
        httpd.server_close()
