package com.example;

import com.example.models.Subscription;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("3) VIEW MONTHLY SUMMARY");
        System.out.println("4) QUIT");
    }

    public void displayUpdateMenu() {
        System.out.println("\nNEXT PAY: Update Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) Choose BY ID and then UPDATE THE Subscriptions LIKE ADD Subscriptions");
        System.out.println("2) QUIT");
    }

    // --- Handler Prototypes ---

    public int handleStartSelection(int input) {
        switch (input) {
            case 1:
                return 1; // Proceed to login
            case 2:
                return 0; // Quit application
            default:
                return -1; // Invalid input
        }
    }

    
    public int handleMainMenuSelection(int input) {
        switch (input) {
            case 1:
                System.out.println("Selected: Add Subscriptions");
                return 1;
            case 2:
                System.out.println("Selected: Delete Subscriptions");
                return 2;
            case 3:
                System.out.println("Selected: View Subscriptions");
                return 3;
            case 4:
                System.out.println("Selected: Update Subscriptions");
                return 4;
            case 5:
                System.out.println("Logging out...");
                return 0;
            default:
                System.out.println("Invalid choice. Try again.");
                return -1;
        }
    }

    public boolean handleLogin(String username, String password) {
        boolean valid = controller.validateUser(username, password);
        if (valid) {
            int userId = controller.getUserIdByUsername(username);
            setCurrentUserId(userId);
            System.out.println("Login successful! Welcome, " + username + ".");
            return true;
        } else {
            System.out.println("Invalid username or password. Try again.");
            return false;
        }
    }

    public boolean handleAddSubscription(int userId) {
        return true;
   
    }

    public boolean handleDeleteSubscription(int userId, int subscriptionId) {
        return getController().handleDeleteSubscription(userId, subscriptionId);
    }

    /**
     * Handles viewing subscriptions menu for the given user.
     * viewChoice: 1 = all, 2 = sort, 3 = monthly summary, 4 = quit
     */
    public boolean handleViewSubscriptions(int userId, int viewChoice) {
        switch (viewChoice) {
            case 1: // VIEW ALL
                List<Subscription> allSubs = controller.getAllSubscriptionsForUser(userId);
                if (allSubs.isEmpty()) {
                    System.out.println("You have no subscriptions.");
                    return false;
                }
                System.out.println("\n--- All Subscriptions ---");
                for (Subscription sub : allSubs) {
                    System.out.println(
                        "ID: " + sub.getSubscriptionID() +
                        ", Name: " + sub.getSubscriptionsName() +
                        ", Cost: $" + sub.getCost() +
                        ", Recurring: " + sub.isRecurring() +
                        ", Cycle: " + sub.getBillingCycleType() +
                        ", Next Bill: " + sub.getBillingCycleDate()
                    );
                }
                return true;

case 2: // SORT BY (asc/desc)
    System.out.print("Sort by date (asc/desc): ");
    String sortOrder = scanner.nextLine();
    // Just call the controller method as specified in the API you agreed on:
    List<Subscription> sortedSubs = controller.sortSubscriptionsByDate(sortOrder);
    if (sortedSubs == null || sortedSubs.isEmpty()) {
        System.out.println("No subscriptions to sort or invalid sort order.");
        return false;
    }
    System.out.println("\n--- Subscriptions Sorted By Date (" + sortOrder + ") ---");
    for (Subscription sub : sortedSubs) {
        // Print as usual
        System.out.println(
            "ID: " + sub.getSubscriptionID() +
            ", Name: " + sub.getSubscriptionsName() +
            ", Cost: $" + sub.getCost() +
            ", Recurring: " + sub.isRecurring() +
            ", Cycle: " + sub.getBillingCycleType() +
            ", Next Bill: " + sub.getBillingCycleDate()
        );
    }
    return true;

            case 3: // MONTHLY SUMMARY
                String summary = controller.getMonthlySummaryString(userId);
                System.out.println("\n--- Monthly Subscription Summary ---");
                System.out.println(summary);
                return !summary.contains("0 monthly subscriptions");

            case 4: // QUIT
                System.out.println("Returning to main menu...");
                return false;

            default:
                System.out.println("Invalid choice. Try again.");
                return false;
        }
    }

    public boolean exportToCSV(int userId) {
        return controller.exportToCSV(userId);
    }

    /**
     * Handles sorting subscriptions for a user by date.
     * If you want a separate function.
     */
    public boolean handleSortSubscriptions(int userId, String sortOrder) {
        return true;

    }

    public boolean handleUpdateSubscription(int userId, int subscriptionId) {
        return true;
     
    }

    // ---Getters/Setters ---
    public int getCurrentUserId() {
        return currentUserId;
    }
    public void setCurrentUserId(int userId) {
        this.currentUserId = userId;
    }

    public subscriptions_module getController() {
    return controller;
}

}
