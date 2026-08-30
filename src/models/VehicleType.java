package models;

/**
 * Enum representing different types of vehicles
 * that can use the parking facility.
 */
public enum VehicleType {
    CAR("Car"),
    MOTORCYCLE("Motorcycle"),
    VAN("Van");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
