
//import java.util.ArrayList;
//import java.util.List;
import java.util.Scanner;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.io.BufferedWriter;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter; 

public class Main {

    public static Scanner globalScanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            Admin.clearScreen();
            System.out.println("==============================");
            System.out.println("    Welcome to StarBucks!");
            System.out.println("==============================");
            System.out.println();
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println();

            int choice;
            while (true) {
                System.out.print("Please enter your choice: ");

                String input = Main.globalScanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Invalid! You must enter something.");
                    continue;
                }

                try {
                    choice = Integer.parseInt(input); // 
                } catch (NumberFormatException e) {
                    System.out.println("Invalid! Enter a number only.");
                    continue;
                }

                // must be 1-3
                if (choice < 1 || choice > 3) {
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                    continue;
                }

                break; // valid choice, exit loop
            }

            switch (choice) {
                case 1: {
                    User user = new User();
                    System.out.println("--- User Registration ---");
                    user.registerUser(Main.globalScanner);
                    Admin.pause(Main.globalScanner);
                    break;
                }
                case 2: {
                    User user = new User();
                    Admin.clearScreen();
                    System.out.println("--- User Login ---");
                    boolean success = user.logInUser(Main.globalScanner);
                    if (success) {
                        postLoginMenu(Main.globalScanner, user);
                    } else {
                        System.out.println("Login failed. Please try again.");
                        Admin.pause(Main.globalScanner);
                    }
                    break;
                }
                case 3: {
                    System.out.println("Thank you for using the Starbucks System. Have a Good Day");
                    Main.globalScanner.close();
                    return;
                }
            }
        }
    }

    // poide sa admin
    public static void postLoginMenu(Scanner sc, User user) {
        while (true) {
            Admin.clearScreen();
            System.out.println("Hello, " + user.getUsername() + "!");
            System.out.println("You currently have " + user.getStars() + " points.");
            System.out.println();

            System.out.println("\n--- Starbucks Main Menu ---");
            System.out.println("1. Order");
            System.out.println("2. Redeem Points");
            System.out.println("3. Deposit Wallet");
            System.out.println("4. Transaction History");
            System.out.println("5. Profile");
            System.out.println("6. Logout");
            System.out.println("================================================");
            System.out.println("Note: For every 50 stars you get P50!");
            System.out.println("================================================");
            System.out.println();
            System.out.print("Select an option: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                Admin.pause(sc);
                continue;
            }

            int option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1:
                    Order.orderFlow(sc, user);
                    break;
                case 2:
                    Admin.clearScreen();
                    System.out.println("You currently have " + user.getStars() + " stars.");

                    if (user.getStars() < 100) {
                        System.out.println("You need at least 100 stars to redeem.");
                        Admin.pause(sc);
                        break;
                    }

                    System.out.print("Redeem points now? (y/n): ");
                    String ans = sc.nextLine().trim();

                    if (ans.equalsIgnoreCase("y")) {
                        user.redeemPoints();
                    } else {
                        System.out.println("Redemption cancelled.");
                    }

                    Admin.pause(sc);
                    break;

                case 3:
                    depositWallet(sc, user);
                    Admin.pause(sc);
                    break;
                case 4:
                    User.viewTransactionHistory(user);

                    Admin.pause(sc);
                    break;
                case 5:
                    viewProfile(user);
                    pause(sc);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    Admin.pause(sc);
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1 to 6.");
                    Admin.pause(sc);
                    break;
            }
        }
    }

    // user

    // new class?
    public static void depositWallet(Scanner sc, User user) {
        Admin.clearScreen();
        System.out.print("Enter amount to deposit: ");
        while (!sc.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
            System.out.print("Enter amount to deposit: ");
        }
        double amount = sc.nextDouble();
        sc.nextLine();

        if (amount > 0) {
            user.setBalance(user.getBalance() + amount);
            user.saveUserToFile();
            System.out.printf("Successfully deposited %.2f. New balance: %.2f\n", amount, user.getBalance());
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    // admin

    // --- Supporting Classes ---
}
