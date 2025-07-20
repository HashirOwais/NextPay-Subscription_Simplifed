package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;

import com.example.models.Subscription;

public class UITest {
    UIModule ui;
    int userId = 1; // This matches seeded user

    @BeforeEach
    void setup() {
        // Reset DB: Clean and seed user
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
        ui = new UIModule();
    }

    @Test
    public void testViewAllSubscriptions_NoSubscriptions_ReturnsFalse() {
        boolean result = ui.handleViewSubscriptions(userId, 1); // 1 = VIEW ALL
        assertFalse(result);
    }

    @Test
    public void testViewAllSubscriptions_WithSubscriptions_ReturnsTrue() {
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        boolean result = ui.handleViewSubscriptions(userId, 1); // 1 = VIEW ALL
        assertTrue(result);
    }

    @Test
    public void testMonthlySummary_NoSubscriptions_ReturnsFalse() {
        boolean result = ui.handleViewSubscriptions(userId, 3); // 3 = MONTHLY SUMMARY
        assertFalse(result);
    }

    @Test
    public void testMonthlySummary_WithSubscriptions_ReturnsTrue() {
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        ui.getController().addSubscription(new Subscription(0, "Spotify", 7.0, true, "monthly", LocalDate.now(), userId));
        boolean result = ui.handleViewSubscriptions(userId, 3); // 3 = MONTHLY SUMMARY
        assertTrue(result);
    }

    @Test
    public void testViewSubscriptions_InvalidChoice_ReturnsFalse() {
        boolean result = ui.handleViewSubscriptions(userId, 99); // Invalid menu option
        assertFalse(result);
    }

    // NOTE: Do NOT test sort here (choice 2) since you don't have that implemented.

    //handleLogin
    @Test
    public void testHandleLogin_inValidCredentialsUsername_fail() {
        String username = "testuserFail";
        String password = "password123";

        boolean result = ui.handleLogin(username, password);
        assertFalse(result); 
        assertEquals(-1, ui.getCurrentUserId());
    }

    @Test
    public void testHandleLogin_inValidCredentialsPass_fail() {
        String username = "testuser";
        String password = "password123Fail";

        boolean result = ui.handleLogin(username, password);
        assertFalse(result); 
        assertEquals(-1, ui.getCurrentUserId());
    }

    @Test
    public void testHandleLogin_inValidCredentialsPassANDUser_fail() {
        String username = "testuseFailr";
        String password = "password123Fail";

        boolean result = ui.handleLogin(username, password);
        assertFalse(result); 
        assertEquals(-1, ui.getCurrentUserId());
    }

    //handleAddSub Tests
    @Test
    public void testHandleAddSubscription_ValidInput_ShouldSucceed() {
        Subscription s = new Subscription(
            0, "Disney+", 12.99, true, "monthly", LocalDate.now().plusDays(10), userId
        );
        boolean result = ui.handleAddSubscription(s);
        assertTrue(result, "Expected successful addition of subscription.");
    }
    @Test
    public void testAddSubscription_EmptyName_ShouldFail() {
        Subscription s = new Subscription(
            0, "", 9.99, true, "monthly", LocalDate.now().plusDays(5), 1
        );
        boolean result = ui.handleAddSubscription(s);
        assertFalse(result, "Empty name should not be allowed.");
    }


    
}
