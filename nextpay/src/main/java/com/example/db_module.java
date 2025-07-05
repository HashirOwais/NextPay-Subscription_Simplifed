package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


public boolean updateSubscription(Subscription s) {

    return true;

}

public Subscription findSubscrptionById(int id)
{
try (Connection conn = DriverManager.getConnection("jdbc:sqlite:nextpay.db");
             Statement stmt = conn.createStatement()) {


                String sql = ""; // here find the sub by id
                ResultSet rs = stmt.executeQuery(sql);

                if(rs.next())
                {

                }




       
        return true;

    } catch (Exception e) {
         e.printStackTrace();
            return null;
    }
}


}
