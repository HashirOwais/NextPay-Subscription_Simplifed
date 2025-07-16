package com.example;
import java.util.Scanner;
import com.example.models.User;
import com.example.models.Subscription;
import com.example.models.Subscription;



//test
public class UIModule {
    private Scanner scanner = new Scanner(System.in);

    //   Start and Login  
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

    //   Main Menu  
    public void displayMainMenu() {
        System.out.println("\nNEXT PAY: Menu");
        System.out.println("CHOOSE AN OPTION (1 - 5)");
        System.out.println("1) ADD Subscriptions");
        System.out.println("2) Delete Subscriptions");
        System.out.println("3) View Subscriptions");
        System.out.println("4) UPDATE Subscriptions");
        System.out.println("5) QUIT");
    }

    //   Add Subscriptions  
    public void displayAddSubscriptionMenu() {
        System.out.println("\nNEXT PAY: ADD Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) ADD Subscription -> LET USER ENTER IN ALL THEIR DETAILS");
        System.out.println("2) QUIT");
    }

    //   Delete Subscriptions  
    public void displayDeleteMenu() {
        System.out.println("\nNEXT PAY: DELETE Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) DELETE (DELETE BY ID)");
        System.out.println("2) QUIT");
    }

    //   View Subscriptions  
    public void displayViewMenu() {
        System.out.println("\nNEXT PAY: VIEW Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 4)");
        System.out.println("1) VIEW ALL");
        System.out.println("2) SORT BY (SORT BY SMTH)");
        System.out.println("3) FILTER BY (FILTER BY SMTH)");
        System.out.println("4) QUIT");
    }

    //Update Subscriptions
    public void displayUpdateMenu() {
        System.out.println("\nNEXT PAY: Update Subscriptions");
        System.out.println("CHOOSE AN OPTION (1 - 2)");
        System.out.println("1) Choose BY ID and then UPDATE THE Subscriptions LIKE ADD Subscriptions");
        System.out.println("2) QUIT");
    }
    //   HANDLER PROTOTYPES  

    // Start screen choice handler
    public void handleStartSelection(int choice) {
    }

    // Login handler
    public boolean handleLogin(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        subscriptions_module controller = new subscriptions_module();
        return controller.isUserValid(user);
    }




    // Main menu selection
    public void handleMainMenuSelection(int choice) {
    }

    // Add subscription input
    public boolean handleAddSubscription(Subscription s) {
        subscriptions_module controller = new subscriptions_module();
        return controller.addSubscription(s);
    }

    // Delete subscription by ID
    public void handleDeleteSubscription(int id) {
    }

    // View all, sort, or filter
    public void handleViewSubscriptions(int choice) {
    }

    // Update subscription by ID
    public void handleUpdateSubscription(int id) {
    }

}
