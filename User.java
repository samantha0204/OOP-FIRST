import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class User extends Person {
   private String username;
   private String password;
   private int stars;
   private double balance;

   public User() {
   }

   public User(String var1, int var2, String var3, String var4, String var5, String var6, String var7, int var8, double var9) {
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

   public void registerUser(Scanner var1) {
      String var2;
      boolean var3;
      do {
         System.out.print("Enter name: ");
         var2 = var1.nextLine();
         var3 = ErrorHandler.isValidName(var2);
         if (!var3) {
            System.out.println("Invalid! Your name should contain lettersnot be empty, not purely spaces, and be under 30 characters.");
         }
      } while(!var3);

      int var4;
      do {
         System.out.print("Enter age: ");

         while(!var1.hasNextInt()) {
            System.out.println("Invalid! Your age must be a whole number between 17 and 120.");
            var1.nextLine();
            System.out.print("Enter age: ");
         }

         var4 = var1.nextInt();
         var1.nextLine();
         var3 = ErrorHandler.isValidAge(var4);
         if (!var3) {
            System.out.println("Invalid! Your age must be between 17 and 120.");
         }
      } while(!var3);

      String var5;
      do {
         System.out.print("Enter birthday (YYYY-MM-DD): ");
         var5 = var1.nextLine();
         var3 = ErrorHandler.isValidBirthday(var5);
         if (!var3) {
            System.out.println("Invalid birthday format.");
         }
      } while(!var3);

      String var6;
      do {
         System.out.print("Enter email address: ");
         var6 = var1.nextLine();
         var3 = ErrorHandler.isValidEmail(var6);
         if (!var3) {
            System.out.println("Invalid email format.");
         }
      } while(!var3);

      String var7;
      do {
         System.out.print("Enter address: ");
         var7 = var1.nextLine();
         var3 = ErrorHandler.isValidAddress(var7);
         if (!var3) {
            System.out.println("Invalid address: cannot be empty or purely numbers.");
         }
      } while(!var3);

      String var8;
      do {
         System.out.print("Enter username: ");
         var8 = var1.nextLine();
         boolean var9 = ErrorHandler.isValidUsername(var8);
         boolean var10 = doesUserFileExist(var8);
         if (!var9) {
            System.out.println("Invalid! Username must be 5–20 chars. Letters, underscores, hyphens, and periods only.");
         } else if (var10) {
            System.out.println("This username is already taken. Please choose another.");
            var9 = false;
         }
      } while(!ErrorHandler.isValidUsername(var8) || doesUserFileExist(var8));

      String var11;
      do {
         System.out.print("Enter password: ");
         var11 = var1.nextLine();
         var3 = ErrorHandler.isValidPassword(var11);
         if (!var3) {
            System.out.println("Invalid! Your password must be 8-20 chars, include uppercase, lowercase, number, and special character.");
         }
      } while(!var3);

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

   public void saveUserToFile() {
      try {
         File var1 = new File("users");
         if (!var1.exists()) {
            var1.mkdir();
         }

         File var2 = new File("users/" + this.getUsername() + ".txt");
         FileWriter var3 = new FileWriter(var2);
         PrintWriter var4 = new PrintWriter(var3);
         var4.println("Name: " + this.getName());
         var4.println("Age: " + this.getAge());
         var4.println("Birthday: " + this.getBirthDay());
         var4.println("Email: " + this.getEmailAddress());
         var4.println("Address: " + this.getAddress());
         var4.println("Username: " + this.getUsername());
         var4.println("Password: " + this.getPassword());
         var4.println("Stars: " + this.getStars());
         var4.println("Balance: " + this.getBalance());
         var4.println("--");
         var4.close();
      } catch (Exception var5) {
         System.out.println("Error saving user file: " + var5.getMessage());
      }
   }

   public static boolean doesUserFileExist(String var0) {
      File var1 = new File("users/" + var0 + ".txt");
      return var1.exists();
   }

   // New login method added here
   public boolean logInUser(Scanner sc) {
      System.out.print("Enter username: ");
      String username = sc.nextLine();

      File userFile = new File("users/" + username + ".txt");
      if (!userFile.exists()) {
         System.out.println("User not found.");
         return false;
      }

      System.out.print("Enter password: ");
      String password = sc.nextLine();

      try (Scanner fileScanner = new Scanner(userFile)) {
         while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (line.startsWith("Password: ")) {
               String savedPassword = line.substring("Password: ".length());
               if (savedPassword.equals(password)) {
                  System.out.println("Login successful! Welcome back, " + username + "!");
                  return true;
               } else {
                  System.out.println("Incorrect password.");
                  return false;
               }
            }
         }
         System.out.println("Password not found in user file.");
         return false;
      } catch (Exception e) {
         System.out.println("Error reading user file: " + e.getMessage());
         return false;
      }
   }
}
