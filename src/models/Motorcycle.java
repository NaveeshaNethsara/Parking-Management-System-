package models;

/**
 * Represents a Motorcycle vehicle.
 * Demonstrates: Inheritance (extends Vehicle),
 *               Polymorphism (overrides calculateParkingFee).
 * Parking Rate: LKR 50 per hour.
 */
public class Motorcycle extends Vehicle {
    private static final long serialVersionUID = 1L;
    private static final double HOURLY_RATE = 50.0;

    /**
     * Constructor for Motorcycle.
     * @param vehicleNumber The registration number.
     * @param ownerName     The owner's name.
     * @param contactInfo   Contact information.
     */
    public Motorcycle(String vehicleNumber, String ownerName, String contactInfo) {
        super(vehicleNumber, ownerName, contactInfo, VehicleType.MOTORCYCLE);
    }

    /**
     * Calculates parking fee for a Motorcycle — Polymorphism.
     * Rate: LKR 50 per hour (minimum 1 hour).
     * @param hours The number of hours parked.
     * @return The parking fee.
     */
    @Override
    public double calculateParkingFee(long hours) {
        if (hours <= 0) {
            hours = 1; // Minimum charge of 1 hour
        }
        return hours * HOURLY_RATE;
    }
}
