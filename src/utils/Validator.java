package utils;

/**
 * Utility class for input validation.
 * Provides static methods to validate user input before processing.
 * Demonstrates: Exception handling and validation (FR-18).
 */
public class Validator {

    /**
     * Checks if a string is not null and not empty (after trimming).
     * @param value The string to check.
     * @return true if the string has content, false otherwise.
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates a vehicle registration number.
     * Accepts formats like: ABC-1234, AB-1234, 12-3456, etc.
     * Must be at least 2 characters long.
     * @param vehicleNumber The vehicle number to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidVehicleNumber(String vehicleNumber) {
        if (!isNotEmpty(vehicleNumber)) {
            return false;
        }
        // Allow alphanumeric characters and hyphens, minimum 2 characters
        String trimmed = vehicleNumber.trim();
        return trimmed.length() >= 2 && trimmed.matches("[A-Za-z0-9\\-]+");
    }

    /**
     * Validates a contact number.
     * Must be 10 digits, optionally starting with +.
     * @param contactNumber The contact number to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidContactNumber(String contactNumber) {
        if (!isNotEmpty(contactNumber)) {
            return false;
        }
        String trimmed = contactNumber.trim();
        // Allow digits, optionally starting with +, between 7-15 digits
        return trimmed.matches("\\+?[0-9]{7,15}");
    }

    /**
     * Checks if a string represents a valid positive number.
     * @param value The string to check.
     * @return true if it is a valid positive number, false otherwise.
     */
    public static boolean isPositiveNumber(String value) {
        if (!isNotEmpty(value)) {
            return false;
        }
        try {
            double num = Double.parseDouble(value.trim());
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string represents a valid non-negative integer.
     * @param value The string to check.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidInteger(String value) {
        if (!isNotEmpty(value)) {
            return false;
        }
        try {
            int num = Integer.parseInt(value.trim());
            return num >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates a slot number format (e.g., A1, B2, C10).
     * @param slotNumber The slot number to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidSlotNumber(String slotNumber) {
        if (!isNotEmpty(slotNumber)) {
            return false;
        }
        String trimmed = slotNumber.trim();
        return trimmed.length() >= 1 && trimmed.matches("[A-Za-z0-9]+");
    }

    private Validator() {
        // Prevent instantiation
    }
}
