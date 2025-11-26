import java.util.Scanner;


public class Admin {
    public static void pause(Scanner sc) {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    public static void clearScreen() {
        // Cross-platform way to simulate clearing screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}