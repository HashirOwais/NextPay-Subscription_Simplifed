package com.example;

import com.example.models.Subscription;
import java.util.List;
import java.util.Scanner;
import com.example.models.User;
import com.example.models.Subscription;



//test
public class UIModule {
    private Scanner scanner = new Scanner(System.in);
    private subscriptions_module controller = new subscriptions_module();
    private int currentUserId = -1; // Set on login

    // --- Display Menus ---

    public void displayStartScreen() {
        System.out.println("NEXT PAY START");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) LOGIN");
        System.out.println("2) QUIT");
    }

    public void displayLoginPrompt() {
        System.out.println("NEXTPAY: LOGIN");
        System.out.println("ENTER USER NAME:");
        System.out.println("ENTER PASSWORD:");
        System.out.println("OR PRESS 'q' TO QUIT");
    }

    public void displayMainMenu() {
        System.out.println("\nNEXT PAY: Menu");
        System.out.println("CHOOSE AN OPTION (1 - 5)");
        System.out.println("1) ADD Subscriptions");
        System.out.println("2) Delete Subscriptions");
        System.out.println("3) View Subscriptions");
        System.out.println("4) UPDATE Subscriptions");
        System.out.println("5) QUIT");
    }

    public void displayAddSubscriptionMenu() {
        System.out.println("\nNEXT PAY: ADD Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) ADD Subscription -> LET USER ENTER IN ALL THEIR DETAILS");
        System.out.println("2) QUIT");
    }

    public void displayDeleteMenu() {
        System.out.println("\nNEXT PAY: DELETE Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) DELETE (DELETE BY ID)");
        System.out.println("2) QUIT");
    }

    public void displayViewMenu() {
        System.out.println("\nNEXT PAY: VIEW Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 4)");
        System.out.println("1) VIEW ALL");
        System.out.println("2) SORT BY (SORT BY SMTH)");
        System.out.println("3) FILTER BY (FILTER BY SMTH)");
        System.out.println("4) QUIT");
    }

    public void displayUpdateMenu() {
        System.out.println("\nNEXT PAY: Update Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) Choose BY ID and then UPDATE THE Subscriptions LIKE ADD Subscriptions");
        System.out.println("2) QUIT");
    }


    /** Handles user login. Returns true if login successful. */
    public boolean handleLogin(String username, String password) {
        return false; // logic to be added
    }

    /** Handles adding a subscription for the given user. */
    public boolean handleAddSubscription(Subscription subscription, int userId) {
        return false; // logic to be added
    }

    /** Handles deleting a subscription by ID for the given user. */
    public boolean handleDeleteSubscription(int userId, int subscriptionId) {
        return false; // logic to be added
    }

    /** Handles viewing subscriptions menu for the given user. */
    public boolean handleViewSubscriptions(int userId, int viewChoice) {
        return false; // logic to be added
    }

    /** Handles updating a subscription by ID for the given user. */
    public boolean handleUpdateSubscription(int userId, int subscriptionId) {
        return false; // logic to be added
    }

    /** Handles viewing the user's monthly subscription summary. */
    public boolean handleViewSummary(int userId) {
        return false; // logic to be added
    }

    // --- User ID Getters/Setters ---

    public int getCurrentUserId() {
        return currentUserId;
    }
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }
}




