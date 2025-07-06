package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class db_moduleTest {
    static db_module db_module;

    @BeforeAll
    static void Valid_DBConnection() {
        db_module = new db_module();
        db_module.DBConnection();
    }

    @Test
    public void dbConnection_WithValidConn_True() {
        boolean demo = db_module.DBConnection();
        assertTrue(demo);
    }

    @Test
    public void dbConnection_WithInvalidConn_ThrowsException() {
        db_module testDb = new db_module() {
            @Override
            public boolean DBConnection() {
                try {
                    // Simulate an invalid DB connection
                    throw new SQLException("Simulated DB connection failure");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        assertThrows(RuntimeException.class, () -> {
            testDb.DBConnection();
        });
    }

    @Test
    public void isUserValid_WithCorrectCredentials_True(){
        boolean isValid = db_module.isUserValid("alice", "password123");
        assertTrue(isValid);
    }
    
    @Test
    public void isUserValid_WithIncorrectPassword_False(){
        boolean isValid = db_module.isUserValid("bob", "wrongpassword");
        assertTrue(!isValid);
    }

    @Test
    public void isUserValid_WithEmptyCredentials_False(){
        boolean isValid = db_module.isUserValid("", "");
        assertTrue(!isValid);
    }

    @Test
    public void isUserValid_WithNullCredentials_False(){
        boolean isValid = db_module.isUserValid(null, null);
        assertTrue(!isValid);
    }

    @Test
    public void exportSubscriptions_WithValidUser_True() {
        int userId = 1; 
        boolean rs = db_module.exportSubscriptions(userId);
        assertTrue(rs);

        File file = new File("subscriptions_user_" + userId + ".csv");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void exportSubscriptions_WithInvalidUser_False() {
        int invalidUserId = -9999;
        String fileName = "subscriptions_user_" + invalidUserId + ".csv";
        File file = new File(fileName);

        boolean result = db_module.exportSubscriptions(invalidUserId);
        assertFalse(result, "Should return false for invalid user ID.");

        file.delete();
    }

}
