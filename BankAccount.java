import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BankAccount implements ATMOperations, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String accountNumber;
    private double balance;
    private final List<Transaction> history = new ArrayList<>();
    private final String ownerUserId;

    public BankAccount(String accountNumber, double initialBalance, String ownerUserId) {
        this.accountNumber = accountNumber;
        this.balance = Math.max(0, initialBalance);
        this.ownerUserId = ownerUserId;
    }

    public String getAccountNumber() { 
        return accountNumber; 
    }
    
    public double getBalance() { 
        return balance; 
    }

    @Override
    public void viewTransactionHistory() {
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            System.out.printf("Current balance: $%.2f%n", balance);
            return;
        }
        System.out.println("\n--- Transaction History ---");
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%2d) %s%n", i + 1, history.get(i));
        }
        System.out.printf("Current balance: $%.2f%n", balance);
    }

    @Override
    public void withdraw(double amount) {
        if (!positive(amount)) return;

        if (amount > balance) {
            System.out.println("Insufficient balance!");
            return;
        }
        
        balance -= amount;
        history.add(Transaction.withdraw(amount));
        DataStore.saveToFile(); // Save after transaction
        
        System.out.printf("Withdrawal successful. New balance: $%.2f%n", balance);
    }

    @Override
    public void deposit(double amount) {
        if (!positive(amount)) return;

        balance += amount;
        history.add(Transaction.deposit(amount));
        DataStore.saveToFile(); // Save after transaction
        
        System.out.printf("Deposit successful. New balance: $%.2f%n", balance);
    }

    @Override
    public void transfer(String recipientUserId, double amount) {
        if (!positive(amount)) return;

        if (recipientUserId.equalsIgnoreCase(ownerUserId)) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        User recipient = DataStore.findUserById(recipientUserId);
        if (recipient == null) {
            System.out.println("Recipient not found.");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient balance!");
            return;
        }

        // Debit sender
        this.balance -= amount;
        this.history.add(Transaction.transferOut(amount, recipientUserId));

        // Credit recipient
        BankAccount target = recipient.getAccount();
        target.balance += amount;
        target.history.add(Transaction.transferIn(amount, this.ownerUserId));

        DataStore.saveToFile(); // Save after transaction

        System.out.printf("Transferred $%.2f to %s successfully. New balance: $%.2f%n",
                amount, recipientUserId, this.balance);
    }

    @Override
    public void quit() {
        System.out.println("Thank you for using the ATM. Goodbye!");
        System.out.printf("Final balance: $%.2f%n", balance);
    }

    private boolean positive(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return false;
        }
        return true;
    }
}