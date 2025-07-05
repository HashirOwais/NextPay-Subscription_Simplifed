package com.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class db_moduleTest {
    static db_module db_module;

    @BeforeAll
    static void setupDatabase() {
        db_module = new db_module();
        db_module.DBConnection();
    }
    
    @Test
    public void dbConnectionTest() {
        boolean demo = db_module.DBConnection();
        assertTrue(demo);

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
        boolean result = db_module.exportSubscriptions(1);
        assertTrue(result);

        File file = new File("subscriptions_user_" + userId + ".csv");
        assertTrue(file.exists());
        file.delete();
    }


    @Test
    public void findSubscriptionById_ValidId_ReturnsSubscription() {
        Subscription s = db_module.findSubscriptionById(1);

        assertNotNull(s); 
    }

    @Test
    public void findSubscriptionById_InvalidId_ReturnsNull() {
        Subscription s = db_module.findSubscriptionById(-1);
        assertNull(s); 
    }
}
