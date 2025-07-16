package com.example;

import static org.junit.jupiter.api.Assertions.*;
import com.example.models.Subscription;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class UITest {

    //handleLogin - Positive case
    @Test
    public void testHandleLogin_validCredentials_returnsTrue() {
        UIModule ui = new UIModule();
        boolean result = ui.handleLogin("testuser", "password123");
        assertTrue(result);
    }

    //handleLogin - negative case.\
    //Checks both username and passwords being wrong
    @Test
    public void testHandleLogin_invalidCredentials_returnsFalse() {
        UIModule ui = new UIModule();
        boolean result = ui.handleLogin("testuserFalse", "password123False");
        assertFalse(result);
    }
    //wrong password
    @Test
    public void testHandleLogin_invalidPasswordCredentials_returnsFalse() {
        UIModule ui = new UIModule();
        boolean result = ui.handleLogin("testuser", "password123False");
        assertFalse(result);
    }

    //wrong username
    @Test
    public void testHandleLogin_invalidUsernameCredentials_returnsFalse() {
        UIModule ui = new UIModule();
        boolean result = ui.handleLogin("testuserFalse", "password123");
        assertFalse(result);
    }
    //empty fields
    @Test
    public void testHandleLogin_emptyCredentials_returnsFalse() {
        UIModule ui = new UIModule();
        boolean result = ui.handleLogin("", "");
        assertFalse(result);
    }



    //handleAddSub
    @Test
    public void testHandleAddSubscription_validInput_returnsTrue() {
        UIModule ui = new UIModule();

        Subscription s = new Subscription(0, "Spotify Premium", 9.99, true, "Monthly", LocalDate.of(2025, 8, 1), 1);

        boolean result = ui.handleAddSubscription(s);
        assertTrue(result);
    }
}

