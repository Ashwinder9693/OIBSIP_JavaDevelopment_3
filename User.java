import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String userId;
    private final String pin;
    private final String name;
    private final BankAccount account;

    public User(String userId, String pin, String name, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.name = name;
        this.account = new BankAccount("ACCT-" + userId, initialBalance, userId);
    }

    public String getUserId() { return userId; }
    public String getPin() { return pin; }
    public String getName() { return name; }
    public BankAccount getAccount() { return account; }
}