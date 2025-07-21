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

    @Test
    public void testDeleteSubscription_ValidDeletion_True() {
        ui.getController().addSubscription(
                new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        int subscriptionId = ui.getController().getAllSubscriptionsForUser(userId).get(0).getSubscriptionID();

        boolean result = ui.handleDeleteSubscription(userId, subscriptionId);

        assertTrue(result, "Valid subscription should be deleted");
    }

    @Test
    public void testDeleteSubscription_NonExistentSubscription_False() {
        boolean result = ui.handleDeleteSubscription(userId, 99999);
        assertFalse(result, "Deleting a non-existent subscription should return false");
    }

    @Test
    public void testDeleteSubscription_NotOwnedByUser_False() {
        Subscription otherSub = new Subscription(0, "Spotify", 7.0, true, "monthly", LocalDate.now(), 2);
        ui.getController().addSubscription(otherSub);

        int otherUserSubId = ui.getController().getAllSubscriptionsForUser(2).get(0).getSubscriptionID();

        boolean result = ui.handleDeleteSubscription(userId, otherUserSubId);

        assertFalse(result, "User should not delete another user's subscription");
    }

    @Test
    public void testHandleMainMenuSelection_Add_Returns1() {
        int result = ui.handleMainMenuSelection(1);
        assertEquals(1, result, "Input 1 should trigger Add Subscriptions");
    }

    @Test
    public void testHandleMainMenuSelection_Delete_Returns2() {
        int result = ui.handleMainMenuSelection(2);
        assertEquals(2, result, "Input 2 should trigger Delete Subscriptions");
    }

    @Test
    public void testHandleMainMenuSelection_View_Returns3() {
        int result = ui.handleMainMenuSelection(3);
        assertEquals(3, result, "Input 3 should trigger View Subscriptions");
    }

    @Test
    public void testHandleMainMenuSelection_Update_Returns4() {
        int result = ui.handleMainMenuSelection(4);
        assertEquals(4, result, "Input 4 should trigger Update Subscriptions");
    }

    @Test
    public void testHandleMainMenuSelection_Quit_Returns0() {
        int result = ui.handleMainMenuSelection(5);
        assertEquals(0, result, "Input 5 should log out");
    }

    @Test
    public void testHandleMainMenuSelection_Invalid_ReturnsMinus1() {
        int result = ui.handleMainMenuSelection(99);
        assertEquals(-1, result, "Invalid input should return -1");
    }

    // NOTE: Do NOT test sort here (choice 2) since you don't have that implemented.
}
