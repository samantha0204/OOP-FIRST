import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class User extends Person {
   private String username;
   private String password;
   private int stars;
   private double balance;

   public User() {
   }

   public User(String var1, int var2, String var3, String var4, String var5,
         String var6, String var7, int var8, double var9) {
      super(var1, var2, var3, var4, var5);
      this.username = var6;
      this.password = var7;
      this.stars = var8;
      this.balance = var9;
   }

   public String getUsername() {
      return this.username;
   }

   public String getPassword() {
      return this.password;
   }

   public int getStars() {
      return this.stars;
   }

   public double getBalance() {
      return this.balance;
   }

   public void setUsername(String var1) {
      this.username = var1;
   }

   public void setPassword(String var1) {
      this.password = var1;
   }

   public void setStars(int var1) {
      this.stars = var1;
   }

   public void setBalance(double var1) {
      this.balance = var1;
   }

   // ===============================
   // REGISTER USER
   // ===============================
   public void registerUser(Scanner var1) {
      String var2;
      boolean var3;
      do {
         System.out.print("Enter name: ");
         var2 = var1.nextLine();
         var3 = ErrorHandler.isValidName(var2);
         if (!var3) {
            System.out.println(
                  "Invalid! Your name should contain letters, not be empty, not purely spaces, and be under 30 characters.");
         }
      } while (!var3);

      int var4 = 0;
      boolean validAge = false;

      do {
         System.out.print("Enter age: ");
         String input = var1.nextLine().trim();

         // empty input
         if (input.isEmpty()) {
            System.out.println("Invalid! Age cannot be empty.");
            continue;
         }

         // integer check
         try {
            var4 = Integer.parseInt(input);
         } catch (NumberFormatException e) {
            System.out.println("Invalid! Your age must be a whole number between 17 and 120.");
            continue;
         }

         // range check
         validAge = ErrorHandler.isValidAge(var4);
         if (!validAge) {
            System.out.println("Invalid! Your age must be between 17 and 120.");
         }

      } while (!validAge);

      String var5;
      do {
         System.out.print("Enter birthday (MM-DD-YYYY): ");
         var5 = var1.nextLine();
         var3 = ErrorHandler.isValidBirthday(var5);
         if (!var3) {
            System.out.println("Invalid! Birthday must be in this format (MM-DD-YYYY)");
         }
      } while (!var3);

      String var6;
      do {
         System.out.print("Enter email address: ");
         var6 = var1.nextLine();
         var3 = ErrorHandler.isValidEmail(var6);
         if (!var3) {
            System.out.println("Invalid email format. Please use a valid email format (e.g.,@gmail.com/@yahoo.com).");
         }
      } while (!var3);

      String var7;
      do {
         System.out.print("Enter address: ");
         var7 = var1.nextLine();
         var3 = ErrorHandler.isValidAddress(var7);
         if (!var3) {
            System.out.println("Invalid address: cannot be empty or purely numbers.");
         }
      } while (!var3);

      String var8;
      do {
         System.out.print("Enter username: ");
         var8 = var1.nextLine();
         boolean validU = ErrorHandler.isValidUsername(var8);
         boolean exists = doesUserFileExist(var8);

         if (!validU) {
            System.out
                  .println("Invalid! Username must be 5–20 chars. Letters, underscores, hyphens, and periods only.");
         } else if (exists) {
            System.out.println("This username is already taken. Please choose another.");
            validU = false;
         }
      } while (!ErrorHandler.isValidUsername(var8) || doesUserFileExist(var8));

      String var11;
      do {
         System.out.print("Enter password: ");
         var11 = var1.nextLine();
         var3 = ErrorHandler.isValidPassword(var11);
         if (!var3) {
            System.out.println(
                  "Invalid! Your password must be 8-20 chars, include uppercase, lowercase, number, and special character.");
         }
      } while (!var3);

      this.setName(var2);
      this.setAge(var4);
      this.setBirthDay(var5);
      this.setEmailAddress(var6);
      this.setAddress(var7);
      this.setUsername(var8);
      this.setPassword(var11);
      this.setStars(0);
      this.setBalance(0.0);
      this.saveUserToFile();

      System.out.println("\nRegistration successful!");
      System.out.println("Welcome, " + this.getUsername() + "!");
   }

   // ===============================
   // SAVE USER TO FILE
   // ===============================
   public void saveUserToFile() {
      try {
         File folder = new File("users");
         if (!folder.exists()) {
            folder.mkdir();
         }

         File file = new File("users/" + this.getUsername() + ".txt");
         FileWriter fw = new FileWriter(file);
         PrintWriter pw = new PrintWriter(fw);

         pw.println("Name: " + this.getName());
         pw.println("Age: " + this.getAge());
         pw.println("Birthday: " + this.getBirthDay());
         pw.println("Email: " + this.getEmailAddress());
         pw.println("Address: " + this.getAddress());
         pw.println("Username: " + this.getUsername());
         pw.println("Password: " + this.getPassword());
         pw.println("Stars: " + this.getStars());
         pw.println("Balance: " + this.getBalance());
         pw.println("--");

         pw.close();
      } catch (Exception e) {
         System.out.println("Error saving user file: " + e.getMessage());
      }
   }

   public static boolean doesUserFileExist(String var0) {
      File file = new File("users/" + var0 + ".txt");
      return file.exists();
   }

   // ===============================
   // LOAD USER FROM FILE
   // ===============================
   public boolean loadUserFromFile(String username) {
      File userFile = new File("users/" + username + ".txt");

      if (!userFile.exists())
         return false;

      try (Scanner fileScanner = new Scanner(userFile)) {

         while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();

            if (line.startsWith("Name: "))
               this.setName(line.substring(6));

            else if (line.startsWith("Age: "))
               this.setAge(Integer.parseInt(line.substring(5)));

            else if (line.startsWith("Birthday: "))
               this.setBirthDay(line.substring(10));

            else if (line.startsWith("Email: "))
               this.setEmailAddress(line.substring(7));

            else if (line.startsWith("Address: "))
               this.setAddress(line.substring(9));

            else if (line.startsWith("Username: "))
               this.setUsername(line.substring(10));

            else if (line.startsWith("Password: "))
               this.setPassword(line.substring(10));

            else if (line.startsWith("Stars: "))
               this.setStars(Integer.parseInt(line.substring(7)));

            else if (line.startsWith("Balance: "))
               this.setBalance(Double.parseDouble(line.substring(9)));
         }

         return true;
      } catch (Exception e) {
         System.out.println("Error loading user file: " + e.getMessage());
         return false;
      }
   }

   // ===============================
   // LOGIN USER (UPDATED)
   // ===============================
   public boolean logInUser(Scanner sc) {
      System.out.print("Enter username: ");
      String username = sc.nextLine();

      System.out.print("Enter password: ");
      String password = sc.nextLine();

      File userFile = new File("users/" + username + ".txt");
      if (!userFile.exists()) {
         System.out.println("User not found.");
         return false;
      }

      String savedUsername = null;
      try (Scanner fs = new Scanner(userFile)) {
         while (fs.hasNextLine()) {
            String line = fs.nextLine();
            if (line.startsWith("Username: ")) {
               savedUsername = line.substring("Username: ".length());
               break;
            }
         }
      } catch (Exception e) {
         System.out.println("Error reading user file.");
         return false;
      }

      // **Case-sensitive username check**
      if (!savedUsername.equals(username)) {
         System.out.println("Incorrect username. ");
         return false;
      }

      try (Scanner fileScanner = new Scanner(userFile)) {
         while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();

            if (line.startsWith("Password: ")) {
               String savedPassword = line.substring("Password: ".length());

               if (savedPassword.equals(password)) {
                  System.out.println("Login successful! Welcome back, " + username + "!");
                  loadUserFromFile(username);
                  return true;
               } else {
                  System.out.println("Incorrect password.");
                  return false;
               }
            }
         }

         System.out.println("Password not found.");
         return false;

      } catch (Exception e) {
         System.out.println("Error reading user file.");
         return false;
      }
   }

   public static void viewProfile(User user) {
      Admin.clearScreen();
      System.out.println("\n--- Profile ---");
      System.out.println("Name: " + user.getName());
      System.out.println("Age: " + user.getAge());
      System.out.println("Birthday: " + user.getBirthDay());
      System.out.println("Email: " + user.getEmailAddress());
      System.out.println("Address: " + user.getAddress());
      System.out.println("Username: " + user.getUsername());
      System.out.println("Stars: " + user.getStars());
      System.out.printf("Balance: ₱%,.2f\n", user.getBalance());
   }

   public boolean redeemPoints() {
      if (this.stars < 100) {
         System.out.println("You need at least 100 stars to redeem ₱50.");
         return false;
      }

      int redeemTimes = this.stars / 100; // how many 100s?
      int pesos = redeemTimes * 50;

      // Deduct points
      this.stars -= redeemTimes * 100;

      // Add money to wallet
      this.balance += pesos;

      // Save changes
      this.saveUserToFile();

      System.out.println("Redeemed " + (redeemTimes * 100) + " stars for ₱" + pesos);
      System.out.println("New Balance: ₱" + this.balance);
      System.out.println("Remaining Stars: " + this.stars);

      return true;
   }

   public static void appendTransactionToFile(User user, List<Order.CartItem> cart, double total) {
      File userFile = new File("users/" + user.getUsername() + ".txt");
      try (FileWriter fw = new FileWriter(userFile, true)) {
         DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
         fw.write("TRANSACTION: " + LocalDateTime.now().format(dtf) + System.lineSeparator());
         for (Order.CartItem c : cart) {
            fw.write(String.format("%s %s %s x%d — ₱%.2f%s",
                  c.category.equals("Drink") ? "Drink" : "Item",
                  c.name,
                  (c.size != null ? "(" + c.size + ")" : ""),
                  c.qty,
                  c.subTotal(),
                  System.lineSeparator()));
         }
         fw.write(String.format("Total: ₱%.2f%s", total, System.lineSeparator()));
         fw.write(System.lineSeparator());
      } catch (Exception e) {
         System.out.println("Failed to write transaction: " + e.getMessage());
      }
   }

   // delete transaction file

   public void deleteTransactionHistory() {
      File userFile = new File("users/" + this.username + ".txt");
      if (!userFile.exists()) {
         System.out.println("User file not found.");
         return;
      }

      try {
         StringBuilder profileData = new StringBuilder();
         boolean delimiterFound = false;

         Scanner sc = new Scanner(userFile);

         // Read only until "--"
         while (sc.hasNextLine()) {
            String line = sc.nextLine();
            profileData.append(line).append("\n");

            if (line.trim().equals("--")) {
               delimiterFound = true;
               break;
            }
         }
         sc.close();

         if (!delimiterFound) {
            System.out.println("No transaction history found to delete.");
            return;
         }

         // Rewrite file with profile ONLY
         FileWriter fw = new FileWriter(userFile, false);
         fw.write(profileData.toString());
         fw.close();

         System.out.println("Transaction history successfully deleted!");

      } catch (Exception e) {
         System.out.println("Error deleting transaction history: " + e.getMessage());
      }
   }

   // user?
   public static void viewTransactionHistory(User user) {
      Admin.clearScreen();
      File file = new File("users/" + user.getUsername() + ".txt");

      boolean foundHistory = false;
      boolean afterDelimiter = false;

      try (Scanner fs = new Scanner(file)) {
         System.out.println("--- Transaction History ---\n");

         while (fs.hasNextLine()) {
            String line = fs.nextLine();

            if (line.trim().equals("--")) {
               afterDelimiter = true;
               continue;
            }

            if (afterDelimiter) {
               System.out.println(line);
               foundHistory = true;
            }
         }
      } catch (Exception e) {
         System.out.println("Error reading transaction history.");
         return;
      }

      if (!foundHistory) {
         System.out.println("No transaction history.");
         return;
      }

      System.out.println("\n1. Delete Transaction History");
      System.out.println("2. Back");

      while (true) {

         System.out.print("Enter choice: ");

         String choice = Main.globalScanner.nextLine().trim();

         if (choice.equals("1")) {
            user.deleteTransactionHistory();
            break; // valid → exit loop
         } else if (choice.equals("2")) {
            break; // go back
         } else {
            System.out.println("Invalid input. Please enter 1 or 2 only.");
            continue;
         }
      }
   }

}
