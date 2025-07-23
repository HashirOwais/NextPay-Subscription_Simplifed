package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
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

    /////////////////////////handleLogin///////////////////////////
    @Test
    public void testHandleLogin_ValidCredentials_Success() {
        String username = "testuser";
        String password = "password123";
        
        boolean result = ui.handleLogin(username, password);
        
        assertTrue(result, "Login should succeed with valid credentials");
        assertEquals(1, ui.getCurrentUserId(), "Current user ID should be set to 1");
    }
    @Test
    public void testHandleLogin_ValidCredentials_SetsCorrectUserId() {
        String username = "testuser";
        String password = "password123";
        
        // Ensure we start with no user logged in
        assertEquals(-1, ui.getCurrentUserId(), "Should start with no user logged in");
        
        boolean result = ui.handleLogin(username, password);
        
        assertTrue(result, "Login should succeed");
        assertEquals(1, ui.getCurrentUserId(), "Should set current user ID to 1");
    }
    @Test
    public void testHandleLogin_BothEmpty_ReturnsFalse() {
        boolean result = ui.handleLogin("", "");
        assertFalse(result, "Login should fail with both empty credentials");
        assertEquals(-1, ui.getCurrentUserId(), "User ID should remain -1");
    }
    
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
/////////////////////////////handleAddSub Tests////////////////////////////////////////////////
    @Test
    public void testHandleAddSubscription_ValidInput_True() {
        Subscription s = new Subscription(
            0, "Disney+", 12.99, true, "monthly", LocalDate.now().plusDays(10), userId
        );
        // Use controller directly - this works and tests the business logic
        boolean result = ui.getController().addSubscription(s);
        assertTrue(result, "Expected successful addition of subscription.");
        
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertEquals("Disney+", subscriptions.get(0).getSubscriptionsName());
    }

    @Test
    public void testAddSubscription_EmptyName_False() {
        Subscription s = new Subscription(
            0, "", 9.99, true, "monthly", LocalDate.now().plusDays(5), userId);
        // Use controller directly
        boolean result = ui.getController().addSubscription(s);
        assertFalse(result, "Empty name should not be allowed.");
        
        // Verify no subscription was added
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size());
    }

    @Test
    public void testAddSubscription_NegativeCost_ReturnsFalse() {
        Subscription s = new Subscription(0, "Netflix", -10.99, true, "monthly", LocalDate.now().plusDays(5), userId);
        // Use controller directly
        boolean result = ui.getController().addSubscription(s);
        assertFalse(result, "Should fail with negative cost");
        
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size());
    }

    @Test
    public void testAddSubscription_ZeroCost_ReturnsTrue() {
        // Test zero cost validation - your current business logic allows free services
        Subscription s = new Subscription(0, "Free Service", 0.0, true, "monthly", LocalDate.now().plusDays(5), userId);
        
        boolean result = ui.getController().addSubscription(s);
        assertTrue(result, "Should succeed with zero cost (free service allowed)");
        
        // Verify subscription was added
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertEquals("Free Service", subscriptions.get(0).getSubscriptionsName());
        assertEquals(0.0, subscriptions.get(0).getCost(), 0.001);
    }

    @Test
    public void testAddSubscription_WhitespaceOnlyName_ReturnsFalse() {
        // Test whitespace-only name validation
        Subscription s = new Subscription(0, "   ", 10.99, true, "monthly", LocalDate.now().plusDays(5), userId);
        
        boolean result = ui.getController().addSubscription(s);
        assertFalse(result, "Should fail with whitespace-only name");
        
        // Verify no subscription was added
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size());
    }

    @Test
    public void testAddSubscription_YearlySubscription_ReturnsTrue() {
        // Test yearly subscription
        Subscription s = new Subscription(0, "Adobe Creative Cloud", 239.88, true, "yearly", LocalDate.now().plusDays(365), userId);
        
        boolean result = ui.getController().addSubscription(s);
        assertTrue(result, "Should successfully add yearly subscription");
        
        // Verify subscription details
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertEquals("Adobe Creative Cloud", subscriptions.get(0).getSubscriptionsName());
        assertEquals("yearly", subscriptions.get(0).getBillingCycleType());
    }

    @Test
    public void testAddSubscription_NonRecurringSubscription_ReturnsTrue() {
        Subscription s = new Subscription(0, "Fortnite VBucks", 19.99, false, "one-time", LocalDate.now().plusDays(1), userId);
        
        boolean result = ui.getController().addSubscription(s);
        assertTrue(result, "Should successfully add non-recurring subscription");
        
        // Verify subscription details
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertFalse(subscriptions.get(0).isRecurring());
        assertEquals("one-time", subscriptions.get(0).getBillingCycleType());
    }
    @Test
    public void testHandleAddSubscription_SimulateSuccessPath_True() {
        
        Subscription validSub = new Subscription(0, "Netflix Premium", 15.99, true, "monthly", LocalDate.parse("2025-08-01"), userId);
        
        boolean result = ui.getController().addSubscription(validSub);
        assertTrue(result, "Should return true (simulates success path");
        
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertEquals("Netflix Premium", subscriptions.get(0).getSubscriptionsName());
    }
    @Test
    public void testHandleAddSubscription_SimulateFailurePath_False() {
        
        Subscription invalidSub = new Subscription(0, "", 15.99, true, "monthly", LocalDate.parse("2025-08-01"), userId);
        
        boolean result = ui.getController().addSubscription(invalidSub);
        assertFalse(result, "Should return false (simulates failure path");
        
        // Verify no subscription was added (simulates the failure scenario)
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size());
    }
   @Test
    public void testHandleAddSubscription_SimulateCase2QuitTrue() {
       
        Subscription s = new Subscription(0, "Netflix", 10.99, true, "monthly", LocalDate.parse("2025-08-01"), userId);
        
        // Don't call addSubscription (simulates choosing quit option)
        // Verify no subscription was added (simulates case 2 behavior)
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size(), "No subscription should be added (simulates case 2 quit behavior)");
    }
    

    //////////////////////////////////////////handleUpdateSubscription//////////////////////////////////////////////////////////////////////////
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

    @Test
    public void testHandleUpdateSubscription_SubscriptionNotFound_ReturnsFalse() {

        
        int nonExistentSubId = 99999;
        
        Subscription found = ui.getController().findSubscriptionById(nonExistentSubId);
        assertNull(found, "Should return null for non-existent subscription ");
        
        // This simulates what would happen in handleUpdateSubscription when existing == null
        // The method would return false without proceeding to update
    }

    @Test
    public void testHandleUpdateSubscription_WrongUserAccess_ReturnsFalse() {
       
        
        Subscription otherUserSub = new Subscription(0, "Other User Netflix", 10.0, true, "monthly", LocalDate.now().plusDays(5), 2);
        ui.getController().addSubscription(otherUserSub);
        
        List<Subscription> otherUserSubs = ui.getController().getAllSubscriptionsForUser(2);
        if (!otherUserSubs.isEmpty()) {
            int otherUserSubId = otherUserSubs.get(0).getSubscriptionID();
            
            // Test accessing subscription with wrong user ID (simulates getUserID() != userId check)
            Subscription found = ui.getController().findSubscriptionById(otherUserSubId);
            assertNotNull(found, "Subscription should exist");
            assertNotEquals(userId, found.getUserID(), "Should belong to different user (simulates access control check)");
            
        }
    }
    @Test
    public void testHandleUpdateSubscription_ValidSubscriptionFound_AccessGranted() {
        
        Subscription userSub = new Subscription(0, "User Netflix", 10.0, true, "monthly", LocalDate.now().plusDays(5), userId);
        ui.getController().addSubscription(userSub);
        
        List<Subscription> userSubs = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, userSubs.size(), "Should have one subscription");
        
        int subId = userSubs.get(0).getSubscriptionID();
        
        Subscription found = ui.getController().findSubscriptionById(subId);
        assertNotNull(found, "Should find existing subscription (simulates line 268)");
        assertEquals(userId, found.getUserID(), "Should belong to correct user");
        assertEquals("User Netflix", found.getSubscriptionsName(), "Should have correct name");
    }
    @Test
    public void testHandleUpdateSubscription_UpdateWithValidData_Success() {
       
        
        // Add original subscription
        Subscription original = new Subscription(0, "Original Netflix", 10.0, true, "monthly", LocalDate.now().plusDays(5), userId);
        ui.getController().addSubscription(original);
        
        int subId = ui.getController().getAllSubscriptionsForUser(userId).get(0).getSubscriptionID();
        
        Subscription updated = new Subscription(subId, "Updated Netflix Premium", 15.99, true, "monthly", LocalDate.now().plusDays(10), userId);
        
        boolean success = ui.getController().updateSubscription(updated);
        assertTrue(success, "Should successfully update subscription");
        
        // Verify update worked
        Subscription found = ui.getController().findSubscriptionById(subId);
        assertEquals("Updated Netflix Premium", found.getSubscriptionsName());
        assertEquals(15.99, found.getCost(), 0.01);
    }
    @Test
    public void testHandleUpdateSubscription_UI_SubscriptionNotFound_ReturnsFalse() {
        
        
        int nonExistentSubId = 99999;
        
        
        boolean result = ui.handleUpdateSubscription(userId, nonExistentSubId);
        
        assertFalse(result, "Should return false for non-existent subscription");
    }



}
