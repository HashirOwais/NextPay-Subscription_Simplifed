package com.example;

import java.util.Scanner;
import com.example.models.Subscription;
public class App {
    public static void main(String[] args) {
        UIModule ui = new UIModule();
        runApp(ui);
    }

    public static void runApp(UIModule ui) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            ui.displayStartScreen();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    ui.displayLoginPrompt();
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();

                    boolean loggedIn = ui.handleLogin(username, password);
                    if (loggedIn) {
                        // If login succeeds, enter main menu loop
                        boolean inMainMenu = true;
                        while (inMainMenu) {
                            ui.displayMainMenu();
                            System.out.print("Enter choice: ");
                            String mainChoice = scanner.nextLine();

                            switch (mainChoice) {
                                case "1":
                                    ui.displayAddSubscriptionMenu();
                                    System.out.print("Enter choice ");
                                   ui.handleAddSubscription(ui.getCurrentUserId());
                                    break;

                                case "2":
                                    ui.displayDeleteMenu();
                                    System.out.print("Enter ID to delete: ");
                                    int deleteId = Integer.parseInt(scanner.nextLine());
                                    ui.handleDeleteSubscription(ui.getCurrentUserId(), deleteId);
                                    break;

                                case "3":
                                    ui.displayViewMenu();
                                    System.out.print("Enter view choice: ");
                                    int viewChoice = Integer.parseInt(scanner.nextLine());
                                    ui.handleViewSubscriptions(ui.getCurrentUserId(), viewChoice);
                                    // Add view summary option here if you want (e.g., if choice == 5)
                                    break;

                                case "4":
                                    ui.displayUpdateMenu();
                                    System.out.print("Enter ID to update: ");
                                    int updateId = Integer.parseInt(scanner.nextLine());
                                    ui.handleUpdateSubscription(ui.getCurrentUserId(), updateId);
                                    break;

                                case "5":
                                    System.out.println("Logging out...");
                                    inMainMenu = false;
                                    // Optionally reset current user
                                    ui.setCurrentUserId(-1);
                                    break;

                                default:
                                    System.out.println("Invalid option. Try again.");
                            }
                        }
                    }
                    // If login fails, prompt is handled in UI. Go back to start screen.
                    break;

                case "2":
                    System.out.println("Exiting... See you next pay!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid input. Please choose 1 or 2.");
            }
        }
        scanner.close();
    }
}
