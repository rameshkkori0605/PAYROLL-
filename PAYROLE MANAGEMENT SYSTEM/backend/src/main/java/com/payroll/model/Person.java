package com.payroll.model;

import java.io.Serializable;

/**
 * Person - Abstract parent entity demonstrating Object-Oriented Inheritance & Abstraction.
 * Common parent for Employee and Admin classes.
 */
public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;

    public Person() {
    }

    public Person(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    // Encapsulation: Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Concrete helper method.
     */
    public String getFullName() {
        String fn = (firstName != null) ? firstName.trim() : "";
        String ln = (lastName != null) ? lastName.trim() : "";
        return (fn + " " + ln).trim();
    }

    /**
     * Abstract method demonstrating Polymorphism in subclasses.
     */
    public abstract String getRoleDescription();
}
