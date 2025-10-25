import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private static final Map<String, User> USERS = new HashMap<>();
    private static final String DATA_FILE = "atm_users.dat";

    public static void seed() {
        // Try to load from file first
        if (loadFromFile()) {
            System.out.println("Loaded existing user data.");
            return;
        }

        // If no file exists, create demo users
        if (USERS.isEmpty()) {
            addUser(new User("u1", "1111", "Alice", 1000));
            addUser(new User("u2", "2222", "Bob", 750));
            addUser(new User("u3", "3333", "Charlie", 500));
            saveToFile();
            System.out.println("Created demo users.");
        }
    }

    public static void addUser(User u) {
        USERS.put(u.getUserId(), u);
        saveToFile(); // Save immediately when user is added
    }

    public static User findUserById(String id) {
        return USERS.get(id);
    }

    public static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(USERS);
            System.out.println("[Data saved successfully]");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean loadFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            Map<String, User> loadedUsers = (Map<String, User>) ois.readObject();
            USERS.putAll(loadedUsers);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return false;
        }
    }
}