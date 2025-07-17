package com.example;
import java.util.Comparator;
import java.util.List;
import com.example.models.Subscription;

import com.example.models.User;

public class subscriptions_module {
        db_module db = new db_module();

    public boolean validateUser(String username, String password) {
        return db.isUserValid(username, password);
    }
    public boolean addSubscription(Subscription s) {
        return db.addSubscription(s);
    }
   
    public List<Subscription> sortSubscriptionsByDate() {
        return db.getAllSubscriptionsSortedByDate("asc");
    }
    public int getUserIdByUsername(String username) {
        return db.getUserIdByUsername(username);
    }


}
    

