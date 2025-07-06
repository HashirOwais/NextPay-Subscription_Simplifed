package com.example;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.example.models.Subscription;

public class db_moduleTest {
    static db_module db_module;

@BeforeAll
static void setupDatabase(){
    db_module = new db_module();
    db_module.DBConnection();

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
         Statement stmt = conn.createStatement()) {

        // DROP tables if they exist
        stmt.executeUpdate("DROP TABLE IF EXISTS Subscriptions;");
        stmt.executeUpdate("DROP TABLE IF EXISTS Users;");

        // RECREATE tables (same as your schema)
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Users (" +
                           "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                           "Username TEXT NOT NULL, " +
                           "Password TEXT NOT NULL" +
                           ");");

        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Subscriptions (" +
                           "SubscriptionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                           "SubscriptionsName TEXT NOT NULL, " +
                           "Cost REAL NOT NULL, " +
                           "IsRecurring BOOLEAN NOT NULL, " +
                           "BillingCycleType TEXT NOT NULL, " +
                           "BillingCycleDate DATE NOT NULL, " +
                           "UserID INTEGER NOT NULL, " +
                           "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
                           ");");

        // Seed user
        stmt.executeUpdate("INSERT INTO Users (UserID, Username, Password) VALUES (1, 'testuser', 'password123');");

        // Seed two subscriptions for update tests
        stmt.executeUpdate("INSERT INTO Subscriptions (SubscriptionsName, Cost, IsRecurring, BillingCycleType, BillingCycleDate, UserID) " +
                           "VALUES ('ForNegativeCost', 10.00, 1, 'monthly', '2025-07-06', 1);");

        stmt.executeUpdate("INSERT INTO Subscriptions (SubscriptionsName, Cost, IsRecurring, BillingCycleType, BillingCycleDate, UserID) " +
                           "VALUES ('ForEmptyName', 20.00, 1, 'yearly', '2025-08-10', 1);");

    } catch (Exception e) {
        e.printStackTrace();
    }
}







    @Test
public void updateSubscription_ValidUpdate_ReturnsTrue() { 

    Subscription sub = new Subscription(
        0, // SubscriptionID will be assigned by DB
        "Netflixxx",
        10.99,
        true,
        "monthly",
        LocalDate.now(),
        1
    );
    db_module.addSubscription(sub);

    sub.setSubscriptionsName("UpdatedSub");
    sub.setCost(15.49);

        assertTrue(db_module.updateSubscription(sub));


    }

    @Test
    public void dbConnection_WithValidConn_True() {
        boolean demo = db_module.DBConnection();
        assertTrue(demo);
    }
public void updateSubscription_NegativeCost_ReturnsFalse() {
    // Get subscription with ID 1 (ForNegativeCost)
    Subscription sub = db_module.findSubscriptionById(1);

    // Set to negative cost (invalid)
    sub.setCost(-100.0);

    // Attempt to update, should fail
    assertFalse(db_module.updateSubscription(sub));
}

@Test
public void updateSubscription_EmptyName_ReturnsFalse() {
    // Get subscription with ID 2 (ForEmptyName)
    Subscription sub = db_module.findSubscriptionById(2);

    // Set to empty name (invalid)
    sub.setSubscriptionsName("");

    // Attempt to update, should fail
    assertFalse(db_module.updateSubscription(sub));
}


    @Test
    public void dbConnection_WithInvalidConn_ThrowsException() {
        db_module testDb = new db_module() {
            @Override
            public boolean DBConnection() {
                try {
                    // Simulate an invalid DB connection
                    throw new SQLException("Simulated DB connection failure");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        assertThrows(RuntimeException.class, () -> {
            testDb.DBConnection();
        });
    }

    @Test
    public void isUserValid_WithCorrectCredentials_True(){
        boolean isValid = db_module.isUserValid("testuser", "password123");
        assertTrue(isValid);
    }
    
    @Test
    public void isUserValid_WithIncorrectPassword_False(){
        boolean isValid = db_module.isUserValid("bob", "wrongpassword");
        assertTrue(!isValid);
    }

    @Test
    public void isUserValid_WithEmptyCredentials_False(){
        boolean isValid = db_module.isUserValid("", "");
        assertTrue(!isValid);
    }

    @Test
    public void isUserValid_WithNullCredentials_False(){
        boolean isValid = db_module.isUserValid(null, null);
        assertTrue(!isValid);
    }

    @Test
    public void exportSubscriptions_WithValidUser_True() {
        int userId = 1; 
        boolean rs = db_module.exportSubscriptions(userId);
        assertTrue(rs);

        File file = new File("subscriptions_user_" + userId + ".csv");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void exportSubscriptions_WithInvalidUser_False() {
        int invalidUserId = -9999;
        String fileName = "subscriptions_user_" + invalidUserId + ".csv";
        File file = new File(fileName);

        boolean rs = db_module.exportSubscriptions(invalidUserId);
        assertFalse(rs, "Should return false for invalid user ID.");

        file.delete();
    }
    @Test
public void findSubscriptionById_ValidId_ReturnsSubscription() {
    Subscription s = db_module.findSubscriptionById(1);

    assertNotNull(s); 
}

@Test
public void findSubscriptionById_InvalidId_ReturnsNull() {
    Subscription s = db_module.findSubscriptionById(-1);
    assertNull(s); 
}

@Test
public void getAllSubscriptionsSortedByDate_Asc_ReturnsAscendingOrder() {
    List<Subscription> subs = db_module.getAllSubscriptionsSortedByDate("asc");
    boolean isSorted = true;

    for (int i = 0; i < subs.size() - 1; i++) {
        LocalDate current = subs.get(i).getBillingCycleDate();
        LocalDate next = subs.get(i + 1).getBillingCycleDate();
        if (current.isAfter(next)) {
            isSorted = false;
            break;
        }
    }

    assertTrue(isSorted);
}

@Test
public void getAllSubscriptionsSortedByDate_Desc_ReturnsDescendingOrder() {
    List<Subscription> subs = db_module.getAllSubscriptionsSortedByDate("desc");
    boolean isSorted = true;

    for (int i = 0; i < subs.size() - 1; i++) {
        LocalDate current = subs.get(i).getBillingCycleDate();
        LocalDate next = subs.get(i + 1).getBillingCycleDate();
        if (current.isBefore(next)) {
            isSorted = false;
            break;
        }
    }

    assertTrue(isSorted);
}

@Test
public void getAllSubscriptionsSortedByDate_InvalidOrder_ReturnsNull() {
    List<Subscription> subs = db_module.getAllSubscriptionsSortedByDate("notValid");
    assertNull(subs); // Should be null for any invalid order string
}



}
