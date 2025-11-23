public class ErrorHandler {

    // NAME
    public static boolean isValidName(String name) {
        if (name == null)
            return false;

        String trimmed = name.trim();

        // cannot be empty or only spaces
        if (trimmed.isEmpty())
            return false;

        // max length
        if (trimmed.length() > 30)
            return false;

        // check characters one by one (letters or spaces only)
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (!Character.isLetter(c) && c != ' ') {
                return false;
            }
        }

        return true;
    }

    // AGE
    public static boolean isValidAge(int age) {
        return age >= 17 && age <= 120;
    }

    // BIRTHDAY: MM-DD-YYYY (strict)
    public static boolean isValidBirthday(String birthday) {
        if (birthday == null)
            return false;

        // Strict pattern: only digits and hyphens allowed
        if (!birthday.matches("\\d{2}-\\d{2}-\\d{4}"))
            return false;

        String[] parts = birthday.split("-");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (month < 1 || month > 12)
            return false;
        if (day < 1 || day > 31)
            return false;
        if (year < 1900 || year > 2025)
            return false;

        return true;
    }

    // EMAIL
    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;

        // Must contain valid domains
        if (!(email.endsWith("@gmail.com") || email.endsWith("@yahoo.com")))
            return false;

        // Cannot start with period or hyphen
        if (email.startsWith(".") || email.startsWith("-"))
            return false;

        // No consecutive periods
        if (email.contains(".."))
            return false;

        // Must have a period before TLD
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
            return false;

        return true;
    }

    // ADDRESS
    public static boolean isValidAddress(String address) {
        if (address == null)
            return false;
        if (address.trim().isEmpty())
            return false; // not empty or spaces
        return !address.matches("\\d+"); // not pure numbers
    }

    // USERNAME
    public static boolean isValidUsername(String username) {
        if (username == null)
            return false;
        if (username.length() < 3 || username.length() > 20)
            return false;
        // Allow Unicode letters as well as digits, dot, underscore and hyphen
        return username.matches("^[\\p{L}0-9._-]+$");
    }

    // PASSWORD (your strict rules)
    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;

        // Length check
        if (password.length() < 8 || password.length() > 20)
            return false;

        // Must contain:
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/].*");

        if (!(hasUpper && hasLower && hasDigit && hasSpecial))
            return false;

        // Cannot be "password" or "12345678"
        String lower = password.toLowerCase();
        if (lower.equals("password") || password.equals("12345678"))
            return false;

        return true;
    }

}
