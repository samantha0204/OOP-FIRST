import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
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
            clearScreen();
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
                pause(sc);
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();
            System.out.println();

            switch (choice) {
                case 1: {
                    User user = new User();
                    user.registerUser(sc);
                    pause(sc);
                    break;
                }
                case 2: {
                    User user = new User();
                    boolean success = user.logInUser(sc);
                    if (success) {
                        postLoginMenu(sc, user);
                    } else {
                        System.out.println("Login failed. Please try again.");
                        pause(sc);
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
                    pause(sc);
                    break;
                }
            }
        }
    }

    public static void postLoginMenu(Scanner sc, User user) {
        while (true) {
            clearScreen();
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
            System.out.print("Select an option: ");

            if (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
                pause(sc);
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
                    pause(sc);
                    break;
                case 3:
                    depositWallet(sc, user);
                    pause(sc);
                    break;
                case 4:
                    viewTransactionHistory(user);
                    pause(sc);
                    break;
                case 5:
                    viewProfile(user);
                    pause(sc);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    pause(sc);
                    return;
                default:
                    System.out.println("Invalid choice. Please select 1 to 6.");
                    pause(sc);
                    break;
            }
        }
    }

    public static void orderFlow(Scanner sc, User user) {
        List<CartItem> cart = new ArrayList<>();

        while (true) {
            clearScreen();
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
                pause(sc);
                continue;
            }

            int cat = sc.nextInt();
            sc.nextLine();

            switch (cat) {
                case 1:
                    chooseDrink(sc, cart);
                    pause(sc);
                    break;
                case 2:
                    chooseItemWithStock(sc, cart, bakery);
                    pause(sc);
                    break;
                case 3:
                    chooseItemWithStock(sc, cart, desserts);
                    pause(sc);
                    break;
                case 4:
                    viewCartMenu(sc, cart);
                    pause(sc);
                    break;
                case 5:
                    if (cart.isEmpty()) {
                        System.out.println("Your cart is empty.");
                        pause(sc);
                        break;
                    }
                    boolean paid = checkout(sc, user, cart);
                    pause(sc);
                    if (paid) {
                        return; 
                    }
                    break;
                case 6:
                    System.out.println("Order canceled. Returning to main menu.");
                    pause(sc);
                    return;
                default:
                    System.out.println("Invalid choice.");
                    pause(sc);
                    break;
            }
        }
    }

    public static void viewCartMenu(Scanner sc, List<CartItem> cart) {
        while (true) {
            clearScreen();
            if (cart.isEmpty()) {
                System.out.println("Your cart is empty.");
                pause(sc);
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
                pause(sc);
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == cart.size() + 1) {
                return; // back to order menu
            }

            if (choice < 1 || choice > cart.size()) {
                System.out.println("Invalid choice.");
                pause(sc);
                continue;
            }

            editCartItem(sc, cart, choice - 1);
            pause(sc);
        }
    }

    public static void editCartItem(Scanner sc, List<CartItem> cart, int index) {
        clearScreen();
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
        clearScreen();
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
        clearScreen();
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
        clearScreen();
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
        clearScreen();
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
        clearScreen();
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
        clearScreen();
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

    public static void pause(Scanner sc) {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    public static void clearScreen() {
        // Cross-platform way to simulate clearing screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // --- Supporting Classes ---

    public static class Drink {
        String name;
        double priceTall, priceGrande, priceVenti;

        public Drink(String name, double priceTall, double priceGrande, double priceVenti) {
            this.name = name;
            this.priceTall = priceTall;
            this.priceGrande = priceGrande;
            this.priceVenti = priceVenti;
        }
    }

    public static class Item {
        String name;
        double price;
        int stock;

        public Item(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }

    public static class CartItem {
        String category;
        String name;
        String size; // nullable
        double price;
        int qty;

        public CartItem(String category, String name, String size, double price, int qty) {
            this.category = category;
            this.name = name;
            this.size = size;
            this.price = price;
            this.qty = qty;
        }

        public double subTotal() {
            return price * qty;
        }
    }

    public static class User {
        private String username;
        private String password;
        private String name;
        private int age;
        private String birthday;
        private String email;
        private String address;
        private int stars;
        private double balance;

        public String getUsername() { return username; }
        public String getName() { return name; }
        public int getAge() { return age; }
        public String getBirthDay() { return birthday; }
        public String getEmailAddress() { return email; }
        public String getAddress() { return address; }
        public int getStars() { return stars; }
        public double getBalance() { return balance; }

        public void setStars(int stars) { this.stars = stars; }
        public void setBalance(double balance) { this.balance = balance; }

        public void registerUser(Scanner sc) {
            System.out.print("Enter username: ");
            username = sc.nextLine().trim();

            File userFile = new File("users/" + username + ".txt");
            if (userFile.exists()) {
                System.out.println("Username already exists. Try logging in.");
                return;
            }

            System.out.print("Enter password: ");
            password = sc.nextLine();

            System.out.print("Enter full name: ");
            name = sc.nextLine();

            System.out.print("Enter age: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                sc.nextLine();
            }
            age = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter birthday (e.g., YYYY-MM-DD): ");
            birthday = sc.nextLine();

            System.out.print("Enter email address: ");
            email = sc.nextLine();

            System.out.print("Enter address: ");
            address = sc.nextLine();

            stars = 0;
            balance = 0.0;

            saveUserToFile();

            System.out.println("Registration successful! You can now login.");
        }

        public void saveUserToFile() {
            try {
                File folder = new File("users");
                if (!folder.exists()) folder.mkdir();

                File userFile = new File("users/" + username + ".txt");
                try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(userFile)))) {
                    pw.println("Username: " + username);
                    pw.println("Password: " + password);
                    pw.println("Name: " + name);
                    pw.println("Age: " + age);
                    pw.println("Birthday: " + birthday);
                    pw.println("Email: " + email);
                    pw.println("Address: " + address);
                    pw.println("Stars: " + stars);
                    pw.printf("Balance: %.2f\n", balance);
                    pw.println("--");  // Separator for transactions
                }
            } catch (Exception e) {
                System.out.println("Error saving user file: " + e.getMessage());
            }
        }

        private boolean loadUserFromFile() {
            File userFile = new File("users/" + username + ".txt");
            if (!userFile.exists()) return false;

            try (Scanner fileScanner = new Scanner(userFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();

                    if (line.startsWith("Password: ")) {
                        password = line.substring("Password: ".length());
                    } else if (line.startsWith("Name: ")) {
                        name = line.substring("Name: ".length());
                    } else if (line.startsWith("Age: ")) {
                        age = Integer.parseInt(line.substring("Age: ".length()));
                    } else if (line.startsWith("Birthday: ")) {
                        birthday = line.substring("Birthday: ".length());
                    } else if (line.startsWith("Email: ")) {
                        email = line.substring("Email: ".length());
                    } else if (line.startsWith("Address: ")) {
                        address = line.substring("Address: ".length());
                    } else if (line.startsWith("Stars: ")) {
                        stars = Integer.parseInt(line.substring("Stars: ".length()));
                    } else if (line.startsWith("Balance: ")) {
                        balance = Double.parseDouble(line.substring("Balance: ".length()));
                    }
                }
                return true;
            } catch (Exception e) {
                System.out.println("Error reading user file: " + e.getMessage());
                return false;
            }
        }

        public boolean logInUser(Scanner sc) {
            System.out.print("Enter username: ");
            username = sc.nextLine().trim();

            File userFile = new File("users/" + username + ".txt");
            if (!userFile.exists()) {
                System.out.println("User not found.");
                return false;
            }

            System.out.print("Enter password: ");
            String inputPassword = sc.nextLine();

            if (!loadUserFromFile()) {
                System.out.println("Error loading user data.");
                return false;
            }

            if (!inputPassword.equals(password)) {
                System.out.println("Incorrect password.");
                return false;
            }

            System.out.println("Login successful! Welcome back, " + username + "!");
            System.out.println("You have " + stars + " stars.");

            return true;
        }
    }
}
