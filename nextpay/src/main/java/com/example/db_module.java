package com.example;
import com.example.models.Subscription;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class db_module {

    public boolean DBConnection() {
        String url = "jdbc:sqlite:nextpay.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sqlUsers = "CREATE TABLE IF NOT EXISTS Users (" +
                              "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              "Username TEXT NOT NULL, " +
                              "Password TEXT NOT NULL" +
                              ");";
            stmt.executeUpdate(sqlUsers);

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

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSubscription(Subscription s) {
        // Don't update UserID! Only updatable fields:
        String sql = "UPDATE Subscriptions SET " +
                     "SubscriptionsName = ?, " +
                     "Cost = ?, " +
                     "IsRecurring = ?, " +
                     "BillingCycleType = ?, " +
                     "BillingCycleDate = ? " +    // Removed UserID
                     "WHERE SubscriptionID = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (s.getSubscriptionsName() == null || s.getSubscriptionsName().trim().isEmpty()) {
                return false;
            }
            if (s.getCost() < 0) {
                return false;
            }

            pstmt.setString(1, s.getSubscriptionsName());
            pstmt.setDouble(2, s.getCost());
            pstmt.setBoolean(3, s.isRecurring());
            pstmt.setString(4, s.getBillingCycleType());
            pstmt.setString(5, s.getBillingCycleDate().toString());
            pstmt.setInt(6, s.getSubscriptionID());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Subscription> getAllSubscriptionsSortedByDate(String order) {
        String safeOrder;
        if ("desc".equalsIgnoreCase(order)) {
            safeOrder = "DESC";
        } else if ("asc".equalsIgnoreCase(order)) {
            safeOrder = "ASC";
        } else {
            return null; // Invalid input
        }

        List<Subscription> results = new ArrayList<>();
        String sql = "SELECT * FROM Subscriptions ORDER BY BillingCycleDate " + safeOrder;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
                results.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean addSubscription(Subscription s) {
        String url = "jdbc:sqlite:nextpay.db";
        String sql = "INSERT INTO Subscriptions (SubscriptionsName, Cost, IsRecurring, BillingCycleType, BillingCycleDate, UserID) VALUES (?, ?, ?, ?, ?, ?)";

        if (s.getSubscriptionsName() == null || s.getSubscriptionsName().trim().isEmpty()) {
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

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    s.setSubscriptionID(generatedKeys.getInt(1));
                }
            }

            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Subscription findSubscriptionById(int id) {
        String sql = "SELECT * FROM Subscriptions WHERE SubscriptionID = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Subscription s = new Subscription(
                    rs.getInt("SubscriptionID"),
                    rs.getString("SubscriptionsName"),
                    rs.getDouble("Cost"),
                    rs.getBoolean("IsRecurring"),
                    rs.getString("BillingCycleType"),
                    LocalDate.parse(rs.getString("BillingCycleDate")),
                    rs.getInt("UserID")
                );
                return s;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
