package com.example;

import com.example.models.Subscription;
import java.util.List;
import java.util.HashMap;

public class subscriptions_module {
    private db_module db;

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
