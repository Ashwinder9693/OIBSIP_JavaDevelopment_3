import java.util.Scanner;

public class ATMInterface {
    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        // Seed a few demo users (you can replace with your own)
        DataStore.seed();

        System.out.println("===== Welcome to the ATM =====");
        System.out.println("1. Login");
        System.out.println("2. Register New User");
        int choice = readInt("Choose an option: ");

        User currentUser;
        if (choice == 2) {
            currentUser = registerNewUser();
            if (currentUser == null) {
                System.out.println("Registration failed. Goodbye.");
                return;
            }
        } else {
            currentUser = loginPrompt();
        }

        System.out.println("\nHello, " + currentUser.getName() + "!");
        BankAccount account = currentUser.getAccount();

        while (true) {
            printMenu();
            int menuChoice = readInt("Choose an option: ");

            switch (menuChoice) {
                case 1 -> account.viewTransactionHistory();
                case 2 -> {
                    double amt = readDouble("Enter amount to withdraw: ");
                    account.withdraw(amt);
                }
                case 3 -> {
                    double amt = readDouble("Enter amount to deposit: ");
                    account.deposit(amt);
                }
                case 4 -> {
                    String recipientId = readString("Enter recipient user ID: ");
                    double amt = readDouble("Enter amount to transfer: ");
                    account.transfer(recipientId, amt);
                }
                case 5 -> {
                    account.quit();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println(); // spacing
        }
    }

    private static User registerNewUser() {
        System.out.println("\n===== New User Registration =====");
        
        String userId;
        while (true) {
            userId = readString("Choose a User ID: ");
            if (DataStore.findUserById(userId) != null) {
                System.out.println("User ID already exists. Please choose another.");
            } else if (userId.isEmpty()) {
                System.out.println("User ID cannot be empty.");
            } else {
                break;
            }
        }

        String pin;
        while (true) {
            pin = readString("Choose a PIN (4-6 digits): ");
            if (pin.matches("\\d{4,6}")) {
                break;
            }
            System.out.println("PIN must be 4-6 digits.");
        }

        String name = readString("Enter your name: ");
        if (name.isEmpty()) {
            name = "User";
        }

        double initialDeposit;
        while (true) {
            try {
                System.out.print("Enter initial deposit amount (minimum $10): ");
                String s = SC.nextLine().trim();
                initialDeposit = Double.parseDouble(s);
                if (initialDeposit >= 10) {
                    break;
                }
                System.out.println("Initial deposit must be at least $10.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }

        User newUser = new User(userId, pin, name, initialDeposit);
        DataStore.addUser(newUser);
        System.out.println("\nRegistration successful! Welcome, " + name + "!");
        System.out.println("Your User ID is: " + userId);
        
        return newUser;
    }

    private static User loginPrompt() {
        System.out.println("\n===== Login =====");
        for (int attempts = 0; attempts < 3; attempts++) {
            String userId = readString("User ID: ");
            String pin = readString("PIN: ");

            User user = Authentication.authenticate(userId, pin);
            if (user != null) {
                return user;
            }
            System.out.println("Invalid credentials. Try again.");
        }
        System.out.println("Too many failed attempts. Goodbye.");
        System.exit(0);
        return null; // unreachable
    }

    private static void printMenu() {
        System.out.println("\n===== ATM MENU =====");
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    /* ---------- Small I/O helpers (safe parsing) ---------- */
    private static String readString(String prompt) {
        System.out.print(prompt);
        return SC.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = SC.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = SC.nextLine().trim();
                double v = Double.parseDouble(s);
                if (v <= 0) {
                    System.out.println("Amount must be positive.");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (e.g., 250.50).");
            }
        }
    }
}