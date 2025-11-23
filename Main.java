import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      while (true) {
         System.out.println("==============================");
         System.out.println("    Welcome to StarBucks!");
         System.out.println("==============================");
         System.out.println();

         System.out.println("1. Register");
         System.out.println("2. Login");
         System.out.println("3. Exit");
         System.out.println();

         System.out.print("Please enter your choice: ");

         if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
            continue;
         }

         int choice = sc.nextInt();
         sc.nextLine();
         System.out.println();

         switch (choice) {
            case 1: {
               User user = new User();
               user.registerUser(sc);
               break;
            }
            case 2: {
               User user = new User();
               boolean success = user.logInUser(sc);
               if (success) {
                  postLoginMenu(sc, user);
               } else {
                  System.out.println("Login failed. Please try again.");
               }
               break;
            }
            case 3: {
               System.out.println("Thank you for using the Starbucks System. Have a Good Day");
               sc.close();
               return;
            }
            default: {
               System.out.println("Invalid choice. Please select 1, 2, or 3");
               break;
            }
         }
         System.out.println();
      }
   }

   
   public static void postLoginMenu(Scanner sc, User user) {
      while (true) {
        System.out.println("==============================");
         System.out.println("     Starbucks Main Menu  ");
         System.out.println("==============================");
         System.out.println("1. Order");
         System.out.println("2. View Points");
         System.out.println("3. Deposit Wallet");
         System.out.println("4. Transaction History");
         System.out.println("5. Profile");
         System.out.println("6. Logout");
         System.out.print("Select an option: ");

         if (!sc.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            sc.nextLine();
            continue;
         }

         int option = sc.nextInt();
         sc.nextLine();

         switch (option) {
            case 1:
               orderMenu(sc, user);
               break;
            case 2:
               System.out.println("You have " + user.getStars() + " stars.");
               break;
            case 3:
               depositWallet(sc, user);
               break;
            case 4:
               viewTransactionHistory(user);
               break;
            case 5:
               viewProfile(user);
               break;
            case 6:
               System.out.println("Logging out...");
               return;
            default:
               System.out.println("Invalid choice. Please select 1 to 6.");
               break;
         }
      }
   }

   public static void orderMenu(Scanner sc, User user) {
      System.out.println("way pa code sis");
   }

   public static void depositWallet(Scanner sc, User user) {
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

   public static void viewTransactionHistory(User user) {
      System.out.println("Transaction history feature coming soon...");
   }

   public static void viewProfile(User user) {
      System.out.println("--- Profile ---");
      System.out.println("Name: " + user.getName());
      System.out.println("Age: " + user.getAge());
      System.out.println("Birthday: " + user.getBirthDay());
      System.out.println("Email: " + user.getEmailAddress());
      System.out.println("Address: " + user.getAddress());
      System.out.println("Username: " + user.getUsername());
      System.out.println("Stars: " + user.getStars());
      System.out.println("Balance: " + user.getBalance());
   }
}
