public class Authentication {

    /**
     * Verifies userId + pin and returns the User object if valid, else null.
     */
    public static User authenticate(String userId, String pin) {
        User u = DataStore.findUserById(userId);
        if (u != null && u.getPin().equals(pin)) {
            return u;
        }
        return null;
    }
}