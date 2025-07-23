package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

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

    //TEST CASES FOR handleViewSubscriptions
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
public void testHandleMainMenuSelection_DeletesSubscription_Returns2() {
    int result = ui.handleMainMenuSelection(2); // 2 = Delete Subscriptions
    assertEquals(2, result, "Selecting option 2 should return 2 for Delete Subscriptions");
}


@Test
public void testHandleViewSubscriptions_SortByAsc_Covered() {
    // Save original System.in
    java.io.InputStream originalIn = System.in;
    try {
        // Simulate user input for Scanner (as if user types "asc" + Enter)
        String input = "asc\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        UIModule ui = new UIModule(); // This uses your default constructor with Scanner(System.in)

        // Seed subscriptions
        ui.getController().addSubscription(new Subscription(0, "A", 10, true, "monthly", LocalDate.now(), userId));
        ui.getController().addSubscription(new Subscription(0, "B", 15, true, "monthly", LocalDate.now().plusDays(1), userId));

        // Test the sort option (case 2)
        boolean result = ui.handleViewSubscriptions(userId, 2);
        assertTrue(result);
    } finally {
        // Restore System.in for other tests
        System.setIn(originalIn);
    }
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

    @Test
    public void testExportToCSV_WithSubscriptions_ReturnsTrue() {
        ui.getController().addSubscription(
            new Subscription(0, "Netflix", 15.99, true, "monthly", LocalDate.now(), userId)
        );
        boolean result = ui.exportToCSV(userId);
        assertTrue(result);
    }

    @Test
    public void testExportToCSV_NoSubscriptions_ReturnsFalse() {
        boolean result = ui.exportToCSV(userId);
        assertFalse(result);
    }

    @Test
    public void testHandleStartSelection_Login_Returns1() {
        int result = ui.handleStartSelection(1);
        assertEquals(1, result);
    }

    @Test
    public void testHandleStartSelection_Quit_Returns0() {
        int result = ui.handleStartSelection(2);
        assertEquals(0, result);
    }

    @Test
    public void testHandleStartSelection_Invalid_ReturnsMinus1() {
        int result = ui.handleStartSelection(99);
        assertEquals(-1, result);
    }

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
    public void testHandleAddSubscription_ValidInput_True() {
        Subscription s = new Subscription(
            0, "Disney+", 12.99, true, "monthly", LocalDate.now().plusDays(10), userId
        );
        // Test through controller instead of UI method that requires user input
        boolean result = ui.getController().addSubscription(s);
        assertTrue(result, "Expected successful addition of subscription.");
        
        // Verify it was added
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertEquals("Disney+", subscriptions.get(0).getSubscriptionsName());
    }
    @Test
    public void testAddSubscription_EmptyName_False() {
        Subscription s = new Subscription(
            0, "", 9.99, true, "monthly", LocalDate.now().plusDays(5), userId
        );
        // Test through controller instead of UI
        boolean result = ui.getController().addSubscription(s);
        assertFalse(result, "Empty name should not be allowed.");
    }
    @Test
    public void testAddSubscription_NegativeCost_False() {
        Subscription s = new Subscription(0, "Netflix", -9.99, true, "monthly", LocalDate.now().plusDays(5), userId);
        // Test through controller instead of UI
        boolean result = ui.getController().addSubscription(s);
        assertFalse(result);
    }

    //sortBy
    @Test
    public void testHandleSortSubscriptions_ValidAscOrder_ReturnsTrue() {
        Subscription s1 = new Subscription(0, "A", 10.0, true, "monthly", LocalDate.of(2025, 7, 1), userId);
        Subscription s2 = new Subscription(0, "B", 15.0, true, "monthly", LocalDate.of(2025, 8, 1), userId);
        ui.getController().addSubscription(s2);
        ui.getController().addSubscription(s1);
        boolean result = ui.handleSortSubscriptions(userId, "asc");
        assertTrue(result);
    }
    @Test
    public void testHandleSortSubscriptions_ValidDescOrder_ReturnsTrue() {
        Subscription s1 = new Subscription(0, "A", 10.0, true, "monthly", LocalDate.of(2025, 7, 1), userId);
        Subscription s2 = new Subscription(0, "B", 15.0, true, "monthly", LocalDate.of(2025, 8, 1), userId);
        ui.getController().addSubscription(s2);
        ui.getController().addSubscription(s1);
        boolean result = ui.handleSortSubscriptions(userId, "desc");
        assertTrue(result);
    }
    @Test
    public void testHandleSortSubscriptions_InvalidOrder_ReturnsFalse() {
        boolean result = ui.handleSortSubscriptions(userId, "random");
        assertFalse(result);
    }


    //handleUpdateSubscription
        @Test
        public void testHandleUpdateSubscription_ValidUpdate_ReturnsTrue() {
        Subscription original = new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now().plusDays(5), userId);
        ui.getController().addSubscription(original);

        int subId = ui.getController().getAllSubscriptionsForUser(userId).get(0).getSubscriptionID();

        Subscription updated = new Subscription(subId, "Netflix Premium", 15.0, true, "monthly", LocalDate.now().plusDays(10), userId);

        boolean result = ui.getController().updateSubscription(updated);
        assertTrue(result);

        Subscription found = ui.getController().findSubscriptionById(subId);
        assertTrue(found.getSubscriptionsName().equals("Netflix Premium"));
        assertTrue(found.getCost() == 15.0);
    }
    @Test
    public void testHandleLogin_EmptyUsername_ReturnsFalse() {
        assertFalse(ui.handleLogin("", "password123"));
    }



}
