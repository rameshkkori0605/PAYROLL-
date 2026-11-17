package com.payroll.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBConnection - Database Connection Utility class using JDBC.
 * Implements thread-safe singleton connection factory and configuration management.
 */
public class DBConnection {

    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());
    private static final Properties props = new Properties();

    private static String url = "jdbc:mysql://localhost:3306/payroll_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static String user = "root";
    private static String password = "root";
    private static String driver = "com.mysql.cj.jdbc.Driver";

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                if (props.getProperty("db.url") != null) url = props.getProperty("db.url");
                if (props.getProperty("db.user") != null) user = props.getProperty("db.user");
                if (props.getProperty("db.password") != null) password = props.getProperty("db.password");
                if (props.getProperty("db.driver") != null) driver = props.getProperty("db.driver");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "db.properties not found or unreadable, using default settings", ex);
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found in classpath!", e);
        }
    }

    private DBConnection() {
        // Private constructor to prevent instantiation (Utility class)
    }

    /**
     * Obtains a new database connection from DriverManager.
     * @return active java.sql.Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Safely closes one or more database resources.
     * @param closeables variable number of AutoCloseable objects (Connection, Statement, ResultSet)
     */
    public static void closeQuietly(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error closing resource", e);
                }
            }
        }
    }

    /**
     * Helper to rollback a transaction safely.
     * @param conn active Connection
     */
    public static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to rollback transaction", e);
            }
        }
    }
}
