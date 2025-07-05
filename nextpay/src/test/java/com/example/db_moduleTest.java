package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.example.models.Subscription;

public class db_moduleTest {
    static db_module db_module;

    @BeforeAll
    static void setupDatabase(){
        db_module = new db_module();
        db_module.DBConnection();        
    }

    



    @Test
    public void updateSubscription_()
    {
        Subscription sub = new Subscription();


        assertTrue(db_module.updateSubscription(sub));

    }






    @Test
    public void test()
    {
        db_module db_module = new db_module();
        boolean demo = db_module.DBConnection();
        assertTrue(demo);

    }


    
}
