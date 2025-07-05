package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

import com.example.models.Subscription;

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

}
