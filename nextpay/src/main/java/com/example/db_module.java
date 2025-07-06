package com.example;
import com.example.models.Subscription;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                    "BillingCycleType TEXT NOT NULL, " +
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

    public boolean isUserValid(String username, String password) {
        String url = "jdbc:sqlite:nextpay.db";
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet result = pstmt.executeQuery();

            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportSubscriptions(int userId) {
        String sql = "SELECT * FROM Subscriptions WHERE UserID = ?";
        String fileName = "subscriptions_user_" + userId + ".csv";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
                PreparedStatement pstmt = conn.prepareStatement(sql);
                CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            // CSV header
            String[] header = { "SubscriptionID", "SubscriptionsName", "Cost", "IsRecurring", "BillingCycleType",
                    "BillingCycleDate", "UserID" };
            writer.writeNext(header);

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String[] row = {
                        String.valueOf(rs.getInt("SubscriptionID")),
                        rs.getString("SubscriptionsName"),
                        String.valueOf(rs.getDouble("Cost")),
                        String.valueOf(rs.getBoolean("IsRecurring")),
                        rs.getString("BillingCycleType"),
                        rs.getString("BillingCycleDate"),
                        String.valueOf(rs.getInt("UserID"))
                };
                writer.writeNext(row);
            }

            return hasResults;

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addSubscription(Subscription s) {
    String url = "jdbc:sqlite:nextpay.db";
    String sql = "INSERT INTO Subscriptions (SubscriptionsName, Cost, IsRecurring, BillingCycleType, BillingCycleDate, UserID) VALUES (?, ?, ?, ?, ?, ?)";
    String sqlLastId = "SELECT last_insert_rowid()";

    if (s.getSubscriptionsName() == null || s.getSubscriptionsName().trim().isEmpty() || s.getCost() < 0) {
        return false;
    }
   

    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setString(1, s.getSubscriptionsName());
        pstmt.setDouble(2, s.getCost());
        pstmt.setBoolean(3, s.isRecurring());
        pstmt.setString(4, s.getBillingCycleType());
        pstmt.setString(5, s.getBillingCycleDate().toString());
        pstmt.setInt(6, s.getUserID());

        int rows = pstmt.executeUpdate();

        // Set the ID on the object
        if (rows > 0) {
            // Fetch the last inserted ID manually
            try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sqlLastId)) {
                if (rs.next()) {
                    s.setSubscriptionID(rs.getInt(1));
                }
            }
        }

        return rows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean deleteSubscription(int subscriptionId) {
        String url = "jdbc:sqlite:nextpay.db";
        String sql = "DELETE FROM Subscriptions WHERE SubscriptionID = ?";

        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, subscriptionId);
            pstmt.executeUpdate(); 
            return true; 

        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    public List<Subscription> viewSubscription(int userId) {
        List<Subscription> subscriptions = new ArrayList<>();
        String sql = "SELECT * FROM Subscriptions WHERE UserID = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Subscription s = new Subscription(
                    rs.getInt("SubscriptionID"),
                    rs.getString("SubscriptionsName"),
                    rs.getDouble("Cost"),
                    rs.getBoolean("IsRecurring"),
                    rs.getString("BillingCycleType"),
                    LocalDate.parse(rs.getString("BillingCycleDate")),
                    rs.getInt("UserID")
                );
                subscriptions.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return subscriptions;
    }

}
