package Utils;

import Exceptions.InvalidInputException;

public class Validator {
    private static final double MIN_GWA = 1.0; // Best grade
    private static final double MAX_GWA = 5.0; // Worst grade
    private static final int MIN_ID = 0;  // Best grade
    private static final int MAX_ID = 1000;

    public static void validateID(String input) throws InvalidInputException {
        if (input == null || input.isEmpty())
            throw new InvalidInputException("ID cannot be empty.");

        try {
            int numericId = Integer.parseInt(input);
            if (numericId < MIN_ID || numericId > MAX_ID)
                throw new InvalidInputException("ID must be between " +
                        String.format("%04d", MIN_ID) + " and " +
                        String.format("%04d", MAX_ID) + ".");
        } catch (NumberFormatException e) {
            throw new InvalidInputException("ID must be a number.");
        }
    }

    public static void validateGWA(String input) {
        if (input == null || input.isEmpty())
            throw new InvalidInputException("GWA cannot be empty.");

        if (!input.matches("\\d+(\\.\\d+)?")) {
            throw new InvalidInputException("Only numbers allowed! Example: 1.25");
        }

        double gwa = Double.parseDouble(input);
        if (gwa < MIN_GWA || gwa > MAX_GWA) {
            throw new InvalidInputException("GWA must be between " + MIN_GWA + " and " + MAX_GWA + "!");
        }
    }

    public static void validateEmail(String input) {
        if (input == null || input.isEmpty())
            throw new InvalidInputException("Email cannot be empty.");

        if (!input.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}"))
            throw new InvalidInputException("Invalid email format! Example: name@domain.com");
    }

    public static void validateName(String input) {
        if (input == null || input.isEmpty())
            throw new InvalidInputException("Name is required.");

        if (!input.matches("[A-Za-z\\s\\-]+"))
            throw new InvalidInputException("Only letters, spaces, and hyphens allowed!");
    }
}