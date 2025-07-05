package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

        //create a subsciption

        Subscription subscription = new Subscription();
        //first add a subscrfiption

        // then update them

        //assert true


        assertTrue(db_module.updateSubscription(subscription));

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
