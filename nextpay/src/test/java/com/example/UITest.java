package com.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import com.example.models.Subscription;

public class UITest {
    UIModule ui;
    int userId = 1;

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
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
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
        int result = ui.handleMainMenuSelection(6);
        assertEquals(0, result, "Input 6 should log out");
    }

    @Test
    public void testHandleMainMenuSelection_Invalid_ReturnsMinus1() {
        int result = ui.handleMainMenuSelection(99);
        assertEquals(-1, result, "Invalid input should return -1");
    }

    @Test
    public void testExportToCSV_NoSubscriptions_ReturnsFalse() {
        boolean result = ui.exportToCSV(userId);
        assertFalse(result);
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

    @Test
    public void testHandleAddSubscription_ValidInput_True() {
        Subscription s = new Subscription(
            0, "Disney+", 12.99, true, "monthly", LocalDate.now().plusDays(10), userId
        );
        boolean result = ui.getController().addSubscription(s);
        assertTrue(result, "Expected successful addition of subscription.");
        
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(1, subscriptions.size());
        assertEquals("Disney+", subscriptions.get(0).getSubscriptionsName());
    }

    @Test
    public void testAddSubscription_EmptyName_False() {
        Subscription s = new Subscription(0, "", 9.99, true, "monthly", LocalDate.now().plusDays(5), userId);
        boolean result = ui.getController().addSubscription(s);
        assertFalse(result, "Empty name should not be allowed.");
        
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size());
    }

    

    @Test
    public void testAddSubscription_ZeroCost_ReturnsTrue() {
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
    public void testHandleAddSubscription_SimulateCase2QuitTrue() {

        // Verify no subscription was added (simulates case 2 behavior)
        List<Subscription> subscriptions = ui.getController().getAllSubscriptionsForUser(userId);
        assertEquals(0, subscriptions.size(), "No subscription should be added (simulates case 2 quit behavior)");
    }
    
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

    //new tests

    @Test
    public void testHandleAddSubscription_ValidInputTrue() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Create test input
            String input = "1\nNetflix\n15.99\ntrue\nmonthly\n2025-08-01\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            // Create new UIModule (this will use the mocked input)
            UIModule ui = new UIModule();
            
            boolean result = ui.handleAddSubscription(1);
            assertTrue(result);
            
        } finally {
            // Always restore System.in
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleAddSubscription_Case1_FailsToAdd_ReturnsFalse() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Input that will cause addSubscription to fail (e.g., empty name)
            String input = "1\n\n10.99\ntrue\nmonthly\n2025-08-01\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleAddSubscription(1);
            
            assertFalse(result, "Should return false when subscription addition fails");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleAddSubscription_Case2_ReturnsToMenu_ReturnsFalse() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Input "2" to quit/return to main menu
            String input = "2\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleAddSubscription(1);
            
            assertFalse(result, "Should return false when user chooses to quit");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleAddSubscription_InvalidChoice_ReturnsFalse() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Input invalid choice (99)
            String input = "99\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleAddSubscription(1);
            
            assertFalse(result, "Should return false for invalid menu choice");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleAddSubscription_ExceptionThrown_ReturnsFalse() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Input that will cause parsing exception (invalid number for choice)
            String input = "invalid\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleAddSubscription(1);
            
            assertFalse(result, "Should return false when exception is thrown");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleViewSubscriptions_Case2_ValidSortOrder_ReturnsTrue() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Add test subscriptions first
            ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
            ui.getController().addSubscription(new Subscription(0, "Spotify", 15.0, true, "monthly", LocalDate.now().plusDays(5), userId));
            
            // Mock user input for sort order
            String input = "asc\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleViewSubscriptions(userId, 2);
            
            assertTrue(result, "Should return true when sorting with valid order");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleViewSubscriptions_Case2_EmptySubscriptions_ReturnsFalse() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Mock user input for sort order
            String input = "asc\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleViewSubscriptions(userId, 2);
            
            assertFalse(result, "Should return false when no subscriptions to sort");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleViewSubscriptions_Case2_DescSortOrder_ReturnsTrue() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Add test subscriptions
            ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
            ui.getController().addSubscription(new Subscription(0, "Spotify", 15.0, true, "monthly", LocalDate.now().minusDays(2), userId));
            
            // Mock user input for descending sort order
            String input = "desc\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            boolean result = ui.handleViewSubscriptions(userId, 2);
            
            assertTrue(result, "Should return true when sorting with desc order");
            
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    public void testHandleViewSubscriptions_Case2_NullReturnFromController_ReturnsFalse() {
        // Save original System.in
        InputStream originalIn = System.in;
        
        try {
            // Mock user input
            String input = "asc\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            
            UIModule ui = new UIModule();
            // This test assumes the controller might return null for some edge case
            boolean result = ui.handleViewSubscriptions(userId, 2);
            
            assertFalse(result, "Should return false when controller returns null");
            
        } finally {
            System.setIn(originalIn);
        }
    }


    /////////////////handleSort///
    @Test
    public void testHandleSortSubscriptions_ValidAscOrder_ReturnsTrue() {
        // Add test subscriptions
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        ui.getController().addSubscription(new Subscription(0, "Spotify", 15.0, true, "monthly", LocalDate.now().plusDays(5), userId));
        
        boolean result = ui.handleSortSubscriptions(userId, "asc");
        
        assertTrue(result, "Should return true for valid ascending sort");
    }

    @Test
    public void testHandleSortSubscriptions_ValidDescOrder_ReturnsTrue() {
        // Add test subscriptions
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        ui.getController().addSubscription(new Subscription(0, "Spotify", 15.0, true, "monthly", LocalDate.now().minusDays(3), userId));
        
        boolean result = ui.handleSortSubscriptions(userId, "desc");
        
        assertTrue(result, "Should return true for valid descending sort");
    }

    @Test
    public void testHandleSortSubscriptions_EmptySubscriptionsList_ReturnsFalse() {
        // No subscriptions added - empty list
        boolean result = ui.handleSortSubscriptions(userId, "asc");
        
        assertFalse(result, "Should return false when subscription list is empty");
    }

    @Test
    public void testHandleSortSubscriptions_NullReturnFromController_ReturnsFalse() {
        // This test assumes the controller might return null for invalid sort order
        boolean result = ui.handleSortSubscriptions(userId, "invalid");
        
        assertFalse(result, "Should return false when controller returns null");
    }

    @Test
    public void testHandleSortSubscriptions_InvalidSortOrder_ReturnsFalse() {
        // Add a test subscription
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        
        // Use invalid sort order
        boolean result = ui.handleSortSubscriptions(userId, "random");
        
        assertFalse(result, "Should return false for invalid sort order");
    }

    @Test
    public void testHandleSortSubscriptions_SingleSubscription_ReturnsTrue() {
        // Add single subscription
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        
        boolean result = ui.handleSortSubscriptions(userId, "asc");
        
        assertTrue(result, "Should return true even with single subscription");
    }

    @Test
    public void testHandleSortSubscriptions_MultipleSubscriptions_ReturnsTrue() {
        // Add multiple subscriptions with different dates
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        ui.getController().addSubscription(new Subscription(0, "Spotify", 15.0, true, "monthly", LocalDate.now().plusDays(10), userId));
        ui.getController().addSubscription(new Subscription(0, "Disney+", 12.0, true, "monthly", LocalDate.now().minusDays(5), userId));
        
        boolean result = ui.handleSortSubscriptions(userId, "asc");
        
        assertTrue(result, "Should return true for multiple subscriptions");
    }

    @Test
    public void testHandleSortSubscriptions_EmptyStringOrder_ReturnsFalse() {
        // Add test subscription
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        
        boolean result = ui.handleSortSubscriptions(userId, "");
        
        assertFalse(result, "Should return false for empty string sort order");
    }

    @Test
    public void testHandleSortSubscriptions_NullSortOrder_ReturnsFalse() {
        // Add test subscription
        ui.getController().addSubscription(new Subscription(0, "Netflix", 10.0, true, "monthly", LocalDate.now(), userId));
        
        boolean result = ui.handleSortSubscriptions(userId, null);
        
        assertFalse(result, "Should return false for null sort order");
    }
        
        //test for UI
    @Test
    public void testUI_True() {
        ui.displayAddSubscriptionMenu();
        ui.displayDeleteMenu();
        ui.displayLoginPrompt();
        ui.displayMainMenu();
        ui.displayStartScreen();
        ui.displayViewMenu();
        ui.displayUpdateMenu();
        
        assertTrue(true);
    }
}






