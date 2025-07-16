package com.example;
import java.util.Comparator;
import java.util.List;
import com.example.models.Subscription;

import com.example.models.User;

public class subscriptions_module {
    public boolean isUserValid(User user) {
        db_module db = new db_module();
        return db.isUserValid(user.getUsername(), user.getPassword());
    }
    public boolean addSubscription(Subscription s) {
        db_module db = new db_module();
        return db.addSubscription(s);
    }
   
    public List<Subscription> sortSubscriptionsByDate() {
        db_module db = new db_module();
        return db.getAllSubscriptionsSortedByDate("asc");
    }



}
    

