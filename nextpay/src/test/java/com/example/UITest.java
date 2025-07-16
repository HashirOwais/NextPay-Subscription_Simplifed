package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UITest {


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

}
