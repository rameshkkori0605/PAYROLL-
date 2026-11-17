package com.payroll.controller;

import com.google.gson.JsonObject;
import com.payroll.model.Admin;
import com.payroll.service.AuthService;
import com.payroll.service.impl.AuthServiceImpl;
import com.payroll.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", urlPatterns = {"/api/auth/*"})
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if ("/status".equalsIgnoreCase(pathInfo) || "/me".equalsIgnoreCase(pathInfo)) {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("adminUser") != null) {
                Admin admin = (Admin) session.getAttribute("adminUser");
                Map<String, Object> userData = new HashMap<>();
                userData.put("adminId", admin.getAdminId());
                userData.put("username", admin.getUsername());
                userData.put("fullName", admin.getFullName());
                userData.put("email", admin.getEmail());
                userData.put("role", admin.getRole());
                userData.put("roleDescription", admin.getRoleDescription());

                JsonUtil.sendSuccess(response, userData, "Active session detected");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "No active session");
            }
        } else {
            JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if ("/login".equalsIgnoreCase(pathInfo)) {
            handleLogin(request, response);
        } else if ("/logout".equalsIgnoreCase(pathInfo)) {
            handleLogout(request, response);
        } else {
            JsonUtil.sendError(response, HttpServletResponse.SC_NOT_FOUND, "Unknown auth action");
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            JsonObject json = JsonUtil.fromJson(request, JsonObject.class);
            if (json == null || !json.has("username") || !json.has("password")) {
                JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Username and password are required");
                return;
            }

            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();

            Admin admin = authService.authenticate(username, password);
            if (admin != null) {
                // Invalidate any existing session and establish a clean new authenticated session
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                HttpSession session = request.getSession(true);
                session.setAttribute("adminUser", admin);
                session.setMaxInactiveInterval(60 * 60); // 1 hour session lifetime

                Map<String, Object> userData = new HashMap<>();
                userData.put("adminId", admin.getAdminId());
                userData.put("username", admin.getUsername());
                userData.put("fullName", admin.getFullName());
                userData.put("email", admin.getEmail());
                userData.put("role", admin.getRole());
                userData.put("roleDescription", admin.getRoleDescription());

                JsonUtil.sendSuccess(response, userData, "Authentication successful. Welcome, " + admin.getFullName() + "!");
            } else {
                JsonUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid username or password. Please try again.");
            }
        } catch (SQLException e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error during authentication: " + e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Malformed login request: " + e.getMessage());
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("adminUser");
            session.invalidate();
        }
        JsonUtil.sendSuccess(response, null, "You have been logged out successfully.");
    }
}
