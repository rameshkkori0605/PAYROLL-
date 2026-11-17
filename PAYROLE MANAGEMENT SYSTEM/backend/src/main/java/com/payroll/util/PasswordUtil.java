package com.payroll.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * PasswordUtil - Provides cryptographically secure password hashing and verification using SHA-256 and salt.
 */
public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
        // Prevent instantiation
    }

    /**
     * Generates a 16-byte random cryptographic salt encoded in hex.
     * @return hex string representing salt
     */
    public static String generateSalt() {
        byte[] saltBytes = new byte[16];
        RANDOM.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    /**
     * Hashes a plaintext password combined with salt using SHA-256.
     * @param password raw plaintext password
     * @param salt unique hex salt
     * @return 64-character hex hash string
     */
    public static String hashPassword(String password, String salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("Password and salt must not be null");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String input = password + salt;
            byte[] hashedBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies entered password against stored salt and hash.
     * @param enteredPassword plaintext input from user
     * @param salt stored salt from database
     * @param expectedHash stored hash from database
     * @return true if valid, false otherwise
     */
    public static boolean verifyPassword(String enteredPassword, String salt, String expectedHash) {
        if (enteredPassword == null || salt == null || expectedHash == null) {
            return false;
        }
        String calculatedHash = hashPassword(enteredPassword, salt);
        return calculatedHash.equalsIgnoreCase(expectedHash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
