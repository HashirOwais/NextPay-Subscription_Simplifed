package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.models.Subscription;
import com.example.models.User;

public class subscriptions_moduleTest {
    subscriptions_module controller;

    @BeforeEach
    void setup() {
        controller = new subscriptions_module();
        // Reset DB and seed ONLY user
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS Subscriptions;");
            stmt.executeUpdate("DROP TABLE IF EXISTS Users;");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users (UserID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT NOT NULL, Password TEXT NOT NULL);");
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Subscriptions (SubscriptionID INTEGER PRIMARY KEY AUTOINCREMENT, SubscriptionsName TEXT NOT NULL, Cost REAL NOT NULL, IsRecurring BOOLEAN NOT NULL, BillingCycleType TEXT NOT NULL, BillingCycleDate DATE NOT NULL, UserID INTEGER NOT NULL, FOREIGN KEY (UserID) REFERENCES Users(UserID));");
            stmt.executeUpdate("INSERT INTO Users (UserID, Username, Password) VALUES (1, 'testuser', 'password123');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. Test: View all subscriptions (none present)
    @Test
    public void getAllSubscriptions_NoSubs_ReturnsEmptyList() {
        List<Subscription> subs = controller.getAllSubscriptionsForUser(1);
        assertTrue(subs.isEmpty());
    }

    // 2. Test: View all after adding two
    @Test
    public void getAllSubscriptions_AfterAddingTwo_ReturnsTwo() {
        db_module db = new db_module();
        db.addSubscription(new Subscription(0, "Netflix", 10.00, true, "monthly", LocalDate.now(), 1));
        db.addSubscription(new Subscription(0, "Spotify", 7.99, true, "monthly", LocalDate.now(), 1));
        List<Subscription> subs = controller.getAllSubscriptionsForUser(1);
        assertTrue(subs.size() == 2);
    }

    // 3. Test: View summary with none
    @Test
    public void getMonthlySummaryString_NoSubs_ZeroSummary() {
        String summary = controller.getMonthlySummaryString(1);
        assertTrue(summary.contains("You have 0 monthly subscriptions") && summary.contains("0.00"));
    }

    // 4. Test: View summary with two added
    @Test
    public void getMonthlySummaryString_TwoMonthly_CorrectSummary() {
        db_module db = new db_module();
        db.addSubscription(new Subscription(0, "Netflix", 10.00, true, "monthly", LocalDate.now(), 1));
        db.addSubscription(new Subscription(0, "Spotify", 7.99, true, "monthly", LocalDate.now(), 1));
        String summary = controller.getMonthlySummaryString(1);
        assertTrue(summary.contains("You have 2 monthly subscriptions") && summary.contains("17.99"));
    }

    

}
