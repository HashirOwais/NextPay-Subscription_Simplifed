package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class db_moduleTest {
    static db_module db_module;
    @BeforeAll
    static void setupDatabase(){
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
    public void addSubscriptionTest() {
        db_module db = new db_module();
        Subscription s = new Subscription(0, "Spotify", 8.99, true, "Monthly", "2025-07-05", 1);
        assertTrue(db.addSubscription(s)); 
    }
    //subID, name, cost, recurring, cycletype, cycledate, userid


    //deleting subscription, add a thing first

    
}
