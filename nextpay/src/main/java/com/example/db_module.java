package com.example;
import com.example.models.Subscription;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class db_module {

    public boolean DBConnection() {
        String url = "jdbc:sqlite:nextpay.db"; // Database file
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Users table
            String sqlUsers = "CREATE TABLE IF NOT EXISTS Users (" +
                              "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              "Username TEXT NOT NULL, " +
                              "Password TEXT NOT NULL" +
                              ");";
            stmt.executeUpdate(sqlUsers);

            // Subscriptions table
            String sqlSubscriptions = "CREATE TABLE IF NOT EXISTS Subscriptions (" +
                                     "SubscriptionID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                     "SubscriptionsName TEXT NOT NULL, " +
                                     "Cost REAL NOT NULL, " +
                                     "IsRecurring BOOLEAN NOT NULL, " +
                                     "BillingCycleType TEXT NOT NULL, " +  // No CHECK constraint
                                     "BillingCycleDate DATE NOT NULL, " +
                                     "UserID INTEGER NOT NULL, " +
                                     "FOREIGN KEY (UserID) REFERENCES Users(UserID)" +
                                     ");";
            stmt.executeUpdate(sqlSubscriptions);

            return true; // Success!
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean addSubscription(Subscription s) {
        String url = "jdbc:sqlite:nextpay.db";

        String sql = "INSERT INTO Subscriptions " +"(SubscriptionsName, Cost, IsRecurring, BillingCycleType, BillingCycleDate, UserID) " +"VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, s.getSubscriptionsName());
            pstmt.setDouble(2, s.getCost());
            pstmt.setBoolean(3, s.isRecurring());
            pstmt.setString(4, s.getBillingCycleType());
            pstmt.setString(5, s.getBillingCycleDate().toString());  // convert LocalDate to String
            pstmt.setInt(6, s.getUserID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
