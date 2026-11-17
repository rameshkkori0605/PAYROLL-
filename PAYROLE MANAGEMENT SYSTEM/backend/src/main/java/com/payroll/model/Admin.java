package com.payroll.model;

import java.sql.Timestamp;

/**
 * Admin - Represents system administrators, extending Person (Inheritance).
 */
public class Admin extends Person {

    private static final long serialVersionUID = 1L;

    private int adminId;
    private String username;
    private transient String passwordHash; // Avoid accidental serialization
    private transient String salt;
    private String role;
    private Timestamp createdAt;

    public Admin() {
        super();
    }

    public Admin(int adminId, String username, String fullName, String email, String role) {
        super(fullName != null ? fullName.split(" ")[0] : "", fullName != null && fullName.contains(" ") ? fullName.substring(fullName.indexOf(" ") + 1) : "", email, "");
        this.adminId = adminId;
        this.username = username;
        this.role = role;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getRoleDescription() {
        return (role != null ? role : "ADMIN") + " System Administrator";
    }
}
