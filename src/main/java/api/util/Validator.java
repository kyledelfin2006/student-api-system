package api.util;

import api.exceptions.InvalidInputException;

public class Validator {

    private static final double MIN_GWA = 1.0;
    private static final double MAX_GWA = 5.0;
    private static final int MIN_ID = 0;
    private static final int MAX_ID = 1000;
    private static final int MAX_YEARS = 6;
    private static final int MIN_YEARS = 1;

    // small helper (no forced design change)
    private static String requireTrimmed(String input, String field) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException(field + " is required.");
        }
        return input.trim();
    }

    public static double validateGWA(double gwa) {
        if (gwa < MIN_GWA || gwa > MAX_GWA) {
                throw new InvalidInputException(
                        "GWA must be between " + MIN_GWA + " and " + MAX_GWA + "!"
                );
            }
            return gwa;
    }

    public static String validateEmail(String input) {
        String trimmed = requireTrimmed(input, "Email");

        if (!trimmed.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidInputException("Invalid email format! Example: name@domain.com");
        }

        return trimmed.toLowerCase();
    }

    public static String validateName(String input) {
        String trimmed = requireTrimmed(input, "Name");

        if (trimmed.length() < 2 || trimmed.length() > 100) {
            throw new InvalidInputException("Name must be between 2 and 100 characters.");
        }

        if (!trimmed.matches("^[A-Za-z]+([\\s\\-][A-Za-z]+)*$")) {
            throw new InvalidInputException(
                    "Name must contain only letters, with single spaces or hyphens between words."
            );
        }

        return trimmed;
    }

    public static String validateCourse(String input) {
        String trimmed = requireTrimmed(input, "Course");

        if (trimmed.length() < 2 || trimmed.length() > 50) {
            throw new InvalidInputException("Course must be between 2 and 50 characters.");
        }

        if (!trimmed.matches("^[A-Za-z]+([\\s\\-][A-Za-z]+)*$")) {
            throw new InvalidInputException(
                    "Course must contain only letters, with single spaces or hyphens between words."
            );
        }

        return trimmed;
    }

    public static int validateYearLevel(int input) {

        if (input < MIN_YEARS || input > MAX_YEARS) {
            throw new InvalidInputException(
                    "Year level must be between " + MIN_YEARS + " and " + MAX_YEARS + "."
            );
        }

        return input;
    }
}