import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type { DEPOSIT, WITHDRAW, TRANSFER_IN, TRANSFER_OUT }

    private final Type type;
    private final double amount;
    private final String note;
    private final LocalDateTime time;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Transaction(Type type, double amount, String note) {
        this.type = type;
        this.amount = amount;
        this.note = note;
        this.time = LocalDateTime.now();
    }

    public static Transaction deposit(double amount) {
        return new Transaction(Type.DEPOSIT, amount, null);
    }

    public static Transaction withdraw(double amount) {
        return new Transaction(Type.WITHDRAW, amount, null);
    }

    public static Transaction transferOut(double amount, String toUserId) {
        return new Transaction(Type.TRANSFER_OUT, amount, "to " + toUserId);
    }

    public static Transaction transferIn(double amount, String fromUserId) {
        return new Transaction(Type.TRANSFER_IN, amount, "from " + fromUserId);
    }

    @Override
    public String toString() {
        String base = String.format("[%s] %-12s $%.2f",
                FMT.format(time), type, amount);
        if (note != null) base += " (" + note + ")";
        return base;
    }
}