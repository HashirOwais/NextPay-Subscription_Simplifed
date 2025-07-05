package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class db_moduleTest {


    @Test
    public void test()
    {
        db_module db_module = new db_module();
        boolean demo = db_module.DBConnection();
        assertTrue(demo);

    }


    
}
