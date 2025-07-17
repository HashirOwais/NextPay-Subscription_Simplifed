package com.example;
import java.util.Comparator;
import java.util.List;
import com.example.models.Subscription;

import com.example.models.User;

import com.example.models.Subscription;
import java.util.List;
import java.util.HashMap;

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


    public subscriptions_module() {
        db = new db_module();
    }

    // 1. View all subscriptions for a user
    public List<Subscription> getAllSubscriptionsForUser(int userId) {
        return db.viewSubscription(userId);
    }

    // 2. View monthly summary for a user
    public String getMonthlySummaryString(int userId) {
        HashMap<String, List<Subscription>> map = db.getMonthlySubscriptionSummary(userId);
        // Always one entry, so grab the key (summary)
        return map.keySet().iterator().next();
    }
}
    

