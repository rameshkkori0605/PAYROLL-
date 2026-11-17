package com.payroll.controller;

import com.payroll.util.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthFilter - Enforces session-based authentication for protected API endpoints.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/api/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Set common response headers for REST API
        response.setCharacterEncoding("UTF-8");

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativePath = path.substring(contextPath.length());

        // Whitelist public endpoints (login, auth status check)
        if (relativePath.equals("/api/auth/login") ||
            relativePath.equals("/api/auth/status") ||
            request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(req, res);
            return;
        }

        // Check active session
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("adminUser") != null);

        if (loggedIn) {
            chain.doFilter(req, res);
        } else {
            JsonUtil.sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Access denied. Active administrator session is required. Please sign in.");
        }
    }

    @Override
    public void destroy() {}
}
