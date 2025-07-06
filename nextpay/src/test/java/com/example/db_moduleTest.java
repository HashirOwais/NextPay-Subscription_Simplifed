package com.example;
import com.example.models.Subscription;
import java.time.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

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
    public void dbConnectionTest()
    {
        db_module db_module = new db_module();
        boolean demo = db_module.DBConnection();
        assertTrue(demo);

    }

    //addSubscription: Positive Cases
    @Test
    public void addSubscription_ValidSubscription_True() {
        db_module db = new db_module();
        Subscription s = new Subscription(0, "Spotify", 8.99, true, "Monthly", LocalDate.parse("2025-07-05"), 1);
        assertTrue(db.addSubscription(s)); 
    }
    @Test
    public void addSubscription_ValidNonRecurringSubscription_True() 
    {
        db_module db = new db_module();
        Subscription s = new Subscription(0, "Fortnite VBucks", 14.00, false, "Yearly", LocalDate.parse("2025-12-31"), 1);
        assertTrue(db.addSubscription(s));
    }
    //addSubscription: Negative Cases
    @Test
    public void addSubscription_EmptyName_ReturnsFalse() {
        db_module db = new db_module();
        Subscription s = new Subscription(0, "", 8.99, true, "Monthly", LocalDate.parse("2025-07-05"), 1);
        assertFalse(db.addSubscription(s));
    }

    @Test
    public void addSubscription_NegativeCost_ReturnsFalse() {
        Subscription s = new Subscription(0, "Negative Cost Service", -5.00, true, "Monthly", LocalDate.parse("2025-07-05"), 1);
        assertFalse(db_module.addSubscription(s));
    }



    //deleteSubscription:
    @Test
    public void deleteSubscription_ValidId_True() {

        Subscription s = new Subscription(0, "ToDelete", 4.99, false, "Monthly", LocalDate.parse("2025-07-06"), 1);
        db_module.addSubscription(s);
        assertTrue(db_module.deleteSubscription(1)); 
    }

    




    

  
}
