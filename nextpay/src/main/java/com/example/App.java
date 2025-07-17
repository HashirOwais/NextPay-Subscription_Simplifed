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
                    ui.handleLogin(username, password);
                    break;

                case "2":
                    System.out.println("Exiting... See you next pay!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid input. Please choose 1 or 2.");
            }

            boolean loggedIn = true;
            while (loggedIn) {
                ui.displayMainMenu();
                System.out.print("Enter choice: ");
                String mainChoice = scanner.nextLine();

                switch (mainChoice) {
                    case "1":
                        ui.displayAddSubscriptionMenu();
                        System.out.print("Enter Subscription Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Cost: ");
                        double cost = Double.parseDouble(scanner.nextLine());

                        System.out.print("Is Recurring (true/false): ");
                        boolean isRecurring = Boolean.parseBoolean(scanner.nextLine());

                        System.out.print("Enter Billing Cycle Type: ");
                        String billingType = scanner.nextLine();

                        System.out.print("Enter Billing Date (YYYY-MM-DD): ");
                        String billingDate = scanner.nextLine();

                        System.out.print("Enter User ID: ");
                        int userID = Integer.parseInt(scanner.nextLine());

                        Subscription s = new Subscription(0, name, cost, isRecurring, billingType, java.time.LocalDate.parse(billingDate), userID);

                        boolean success = ui.handleAddSubscription(s);
                        System.out.println(success ? "Subscription added." : "Failed to add.");

                        break;

                    case "2":
                        ui.displayDeleteMenu();
                        System.out.print("Enter ID to delete: ");
                        int deleteId = Integer.parseInt(scanner.nextLine());
                        ui.handleDeleteSubscription(deleteId);
                        break;

                    case "3":
                        ui.displayViewMenu();
                        System.out.print("Enter view choice: ");
                        int viewChoice = Integer.parseInt(scanner.nextLine());
                        ui.handleViewSubscriptions(viewChoice);
                        break;

                    case "4":
                        ui.displayUpdateMenu();
                        System.out.print("Enter ID to update: ");
                        int updateId = Integer.parseInt(scanner.nextLine());
                        ui.handleUpdateSubscription(updateId);
                        break;

                    case "5":
                        System.out.println("Logging out...");
                        loggedIn = false;
                        break;

                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        }
        scanner.close();
    }
}
