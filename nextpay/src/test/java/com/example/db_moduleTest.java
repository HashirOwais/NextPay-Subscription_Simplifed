package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void test()
    {
        db_module db_module = new db_module();
        boolean demo = db_module.DBConnection();
        assertTrue(demo);
    }

    @Test
    public void Vaild_User_True(){
        boolean isValid = db_module.isUserValid("alice", "password123");
        assertTrue(isValid);
    }
    
    @Test
    public void Vaild_User_False(){
        boolean isValid = db_module.isUserValid("bob", "wrongpassword");
        assertTrue(!isValid);
    }

    
}
