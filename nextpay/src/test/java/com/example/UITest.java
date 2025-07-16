package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class UITest {


    @Test
    public void testHandleLogin_validCredentials_returnsTrue() {
        UIModule ui = new UIModule();
        boolean result = ui.handleLogin("demo", "pass123");
        assertTrue(result);
    }

    
}
