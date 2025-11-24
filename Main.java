import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Main {

    static Drink[] drinks = new Drink[] {
        new Drink("Caramel Macchiato", 150, 170, 190),
        new Drink("Caff\u00e8 Latte", 140, 160, 180),
        new Drink("White Chocolate Mocha", 160, 180, 200),
        new Drink("Matcha Cream Frappuccino", 170, 190, 210)
    };

    static Item[] bakery = new Item[] {
        new Item("Ham & Cheese Croissant", 120, 10),
        new Item("Chocolate Croissant", 110, 8),
        new Item("Cinnamon Roll", 95, 5),
        new Item("Spanish Latte Bun", 85, 12)
    };

    static Item[] desserts = new Item[] {
        new Item("Classic Chocolate Cake Slice", 110, 6),
        new Item("Blueberry Cheesecake Slice", 130, 4),
        new Item("Oatmeal Raisin Cookie", 65, 15),
        new Item("Triple Chocolate Cookie", 75, 20)
    };

   
    static final int PESOS_PER_STAR = 50;

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
            System.out.println("\n--- Starbucks Main Menu ---");
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
                    orderFlow(sc, user);
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

   
    public static void orderFlow(Scanner sc, User user) {
        List<CartItem> cart = new ArrayList<>();

        while (true) {
            System.out.println("\n--- Order Menu ---");
            System.out.println("1. Drinks");
            System.out.println("2. Bake-in");
            System.out.println("3. Desserts");
            System.out.println("4. View Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Cancel Order & Return");
            System.out.print("Choose category: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input.");
                sc.nextLine();
                continue;
            }

            int cat = sc.nextInt();
            sc.nextLine();

            switch (cat) {
                case 1:
                    chooseDrink(sc, cart);
                    break;
                case 2:
                    chooseItemWithStock(sc, cart, bakery);
                    break;
                case 3:
                    chooseItemWithStock(sc, cart, desserts);
                    break;
                case 4:
                    viewCartMenu(sc, cart);
                    break;
                case 5:
                    if (cart.isEmpty()) {
                        System.out.println("Your cart is empty.");
                        break;
                    }
                    boolean paid = checkout(sc, user, cart);
                    if (paid) {
                        return; 
                    }
                    break;
                case 6:
                    System.out.println("Order canceled. Returning to main menu.");
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    // View Cart menu with Edit/Delete feature inside Order
    public static void viewCartMenu(Scanner sc, List<CartItem> cart) {
        while (true) {
            if (cart.isEmpty()) {
                System.out.println("Your cart is empty.");
                return;
            }

            System.out.println("\n--- YOUR CART ---");
            for (int i = 0; i < cart.size(); i++) {
                CartItem c = cart.get(i);
                System.out.printf("%d. %s %s x%d — ₱%,.2f\n",
                    i + 1,
                    c.name,
                    c.size == null ? "" : "(" + c.size + ")",
                    c.qty,
                    c.subTotal());
            }
            System.out.println((cart.size() + 1) + ". Back to Order Menu");
            System.out.print("Select an item to Edit/Delete or " + (cart.size() + 1) + " to return: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == cart.size() + 1) {
                return; // back to order menu
            }

            if (choice < 1 || choice > cart.size()) {
                System.out.println("Invalid choice.");
                continue;
            }

            editCartItem(sc, cart, choice - 1);
        }
    }

    public static void editCartItem(Scanner sc, List<CartItem> cart, int index) {
        CartItem item = cart.get(index);
        System.out.println("\nEditing item: " + item.name + (item.size != null ? " (" + item.size + ")" : ""));
        System.out.println("1. Change Quantity");
        System.out.println("2. Delete Item");
        System.out.println("3. Back");
        System.out.print("Choose an option: ");

        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                System.out.print("Enter new quantity (must be > 0): ");
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input.");
                    sc.nextLine();
                    return;
                }
                int newQty = sc.nextInt();
                sc.nextLine();
                if (newQty <= 0) {
                    System.out.println("Quantity must be at least 1.");
                    return;
                }
                item.qty = newQty;
                System.out.println("Quantity updated.");
                break;
            case 2:
                cart.remove(index);
                System.out.println("Item removed from cart.");
                break;
            case 3:
                // Back
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    public static void chooseDrink(Scanner sc, List<CartItem> cart) {
        System.out.println("\n--- DRINKS ---");
        for (int i = 0; i < drinks.length; i++) {
            Drink d = drinks[i];
            System.out.printf("%d. %s — Tall %,.0f | Grande %,.0f | Venti %,.0f\n",
                i+1, d.name, d.priceTall, d.priceGrande, d.priceVenti);
        }
        System.out.print("Choose drink (number) or 0 to return: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice == 0) return;
        if (choice < 1 || choice > drinks.length) {
            System.out.println("Invalid choice.");
            return;
        }

        Drink selected = drinks[choice - 1];

        System.out.println("Choose size: 1. Tall  2. Grande  3. Venti");
        System.out.print("Size: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }
        int size = sc.nextInt();
        sc.nextLine();
        double unitPrice;
        String sizeName;
        switch (size) {
            case 1:
                unitPrice = selected.priceTall;
                sizeName = "Tall";
                break;
            case 2:
                unitPrice = selected.priceGrande;
                sizeName = "Grande";
                break;
            case 3:
                unitPrice = selected.priceVenti;
                sizeName = "Venti";
                break;
            default:
                System.out.println("Invalid size.");
                return;
        }

        System.out.print("Quantity: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }
        int qty = sc.nextInt();
        sc.nextLine();
        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        CartItem ci = new CartItem("Drink", selected.name, sizeName, unitPrice, qty);
        cart.add(ci);
        System.out.printf("%d x %s (%s) added to cart.\n", qty, selected.name, sizeName);
    }

    
    public static void chooseItemWithStock(Scanner sc, List<CartItem> cart, Item[] list) {
        System.out.println("\n--- MENU ---");
        for (int i = 0; i < list.length; i++) {
            System.out.printf("%d. %s — ₱%,.0f (Stock: %d)\n", i+1, list[i].name, list[i].price, list[i].stock);
        }
        System.out.print("Choose item (number) or 0 to return: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }

        int choice = sc.nextInt();
        sc.nextLine();
        if (choice == 0) return;
        if (choice < 1 || choice > list.length) {
            System.out.println("Invalid choice.");
            return;
        }

        Item selected = list[choice - 1];

        System.out.print("Quantity: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input.");
            sc.nextLine();
            return;
        }
        int qty = sc.nextInt();
        sc.nextLine();
        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (qty > selected.stock) {
            System.out.println("Not enough stock. Available: " + selected.stock);
            return;
        }

        CartItem ci = new CartItem("Item", selected.name, null, selected.price, qty);
        cart.add(ci);
        System.out.printf("%d x %s added to cart.\n", qty, selected.name);
    }

    
    public static void viewCart(List<CartItem> cart) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }
        System.out.println("\n--- YOUR CART ---");
        double total = 0.0;
        for (int i = 0; i < cart.size(); i++) {
            CartItem c = cart.get(i);
            System.out.printf("%d. %s %s x%d — ₱%,.2f\n",
                i+1,
                c.name,
                c.size == null ? "" : "(" + c.size + ")",
                c.qty,
                c.subTotal());
            total += c.subTotal();
        }
        System.out.printf("Subtotal: ₱%,.2f\n", total);
    }

 
    public static boolean checkout(Scanner sc, User user, List<CartItem> cart) {
        viewCart(cart);
        double total = 0.0;
        for (CartItem c : cart) total += c.subTotal();

        System.out.printf("\nTotal to pay: ₱%,.2f\n", total);
        System.out.println("Your wallet balance: ₱" + String.format("%,.2f", user.getBalance()));
        System.out.print("Confirm checkout? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            System.out.println("Checkout canceled.");
            return false;
        }

        if (user.getBalance() < total) {
            System.out.println("Insufficient balance. Please deposit or remove items from cart.");
            return false;
        }

       
        user.setBalance(user.getBalance() - total);

      
        int earnedStars = (int) (total / PESOS_PER_STAR);
        user.setStars(user.getStars() + earnedStars);


        for (CartItem c : cart) {
            if (c.category.equals("Item")) {
                // find in bakery or desserts
                boolean found = deductStock(bakery, c.name, c.qty);
                if (!found) {
                    found = deductStock(desserts, c.name, c.qty);
                }
               
            }
        }


        user.saveUserToFile();

      
        appendTransactionToFile(user, cart, total);

        System.out.printf("Payment successful! ₱%,.2f deducted. New balance: ₱%,.2f\n", total, user.getBalance());
        System.out.println("Stars earned this purchase: " + earnedStars);
        System.out.println("Total stars: " + user.getStars());

  
        cart.clear();
        return true;
    }

    
    public static boolean deductStock(Item[] list, String name, int qty) {
        for (Item it : list) {
            if (it.name.equals(name)) {
                it.stock -= qty;
                return true;
            }
        }
        return false;
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
        File userFile = new File("users/" + user.getUsername() + ".txt");
        if (!userFile.exists()) {
            System.out.println("No transactions found (user file missing).");
            return;
        }

        try (Scanner fileScanner = new Scanner(userFile)) {
            boolean afterSep = false;
            List<String> txLines = new ArrayList<>();
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (afterSep) {
                    txLines.add(line);
                }
                if (line.equals("--")) {
                    afterSep = true;
                }
            }
            if (txLines.isEmpty()) {
                System.out.println("No transactions yet.");
            } else {
                System.out.println("\n--- Transaction History ---");
                for (String l : txLines) {
                    System.out.println(l);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading transaction history: " + e.getMessage());
        }
    }

   
    public static void viewProfile(User user) {
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

    public static void appendTransactionToFile(User user, List<CartItem> cart, double total) {
        File userFile = new File("users/" + user.getUsername() + ".txt");
        try (FileWriter fw = new FileWriter(userFile, true)) { 
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            fw.write("TRANSACTION: " + LocalDateTime.now().format(dtf) + System.lineSeparator());
            for (CartItem c : cart) {
                fw.write(String.format("%s %s %s x%d — ₱%.2f%s",
                    c.category.equals("Drink") ? "[Drink]" : "[Item]",
                    c.name,
                    (c.size == null ? "" : "(" + c.size + ")"),
                    c.qty,
                    c.subTotal(),
                    System.lineSeparator()));
            }
            fw.write(String.format("Total: ₱%.2f%s", total, System.lineSeparator()));
            fw.write(String.format("Balance after: ₱%.2f%s", user.getBalance(), System.lineSeparator()));
            fw.write("--" + System.lineSeparator()); // keep separator marker for history reading
        } catch (Exception e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }

    
    static class Item {
        String name;
        double price;
        int stock;
        Item(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }

    static class Drink {
        String name;
        double priceTall;
        double priceGrande;
        double priceVenti;
        Drink(String name, double t, double g, double v) {
            this.name = name;
            this.priceTall = t;
            this.priceGrande = g;
            this.priceVenti = v;
        }
    }

    static class CartItem {
        String category; 
        String name;
        String size; 
        double unitPrice;
        int qty;
        CartItem(String category, String name, String size, double unitPrice, int qty) {
            this.category = category;
            this.name = name;
            this.size = size;
            this.unitPrice = unitPrice;
            this.qty = qty;
        }
        double subTotal() {
            return unitPrice * qty;
        }
    }
}               User user = new User();
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
