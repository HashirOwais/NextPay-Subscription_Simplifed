package com.example;

import java.util.Scanner;

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
                        ui.handleAddSubscription();
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
