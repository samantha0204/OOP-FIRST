import java.io.File;
import java.util.Scanner;

public class ProfileMenu {

// Show the profile menu for a logged-in user
// Returns true if the account was deleted (user logged out), false otherwise
public static boolean show(Scanner sc, Main.User user) {
    while (true) {
        Main.clearScreen();
        System.out.println("=== Profile Menu ===");
        System.out.println("1. View Profile");
        System.out.println("2. Edit Profile");
        System.out.println("3. Delete Account");
        System.out.println("4. Back");
        System.out.print("Choose: ");

        int choice;
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            Main.pause(sc);
            continue;
        }
        choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                Main.viewProfile(user);
                Main.pause(sc);
                break;

            case 2:
                break;

            case 3:
                if (deleteCurrentUser(sc, user)) {
                    // log out user by clearing reference
                    user = null;
                    return true; // account deleted, return to main menu
                }
                break;

            case 4:
                return false; // back to previous menu (stay logged in)
                
            default:
                System.out.println("Invalid choice.");
                Main.pause(sc);
                break;
        }
    }
}

// Delete the currently logged-in user's account
private static boolean deleteCurrentUser(Scanner sc, Main.User user) {
    Main.clearScreen();
    System.out.println("WARNING: This action will permanently delete your account.");
    System.out.print("Are you sure you want to continue? (y/n): ");

    String confirm = sc.nextLine().trim().toLowerCase();
    if (!confirm.equals("y")) {
        System.out.println("Deletion cancelled.");
        Main.pause(sc);
        return false;
    }

    File file = new File("users/" + user.getUsername() + ".txt");

    if (file.exists() && file.delete()) {
        System.out.println("Your account has been deleted and you are now logged out.");
    } else {
        System.out.println("Failed to delete account.");
    }

    Main.pause(sc);
    return true; // signal to go back to main register/login menu
}
}