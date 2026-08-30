package models;

/**
 * Represents a Van vehicle.
 * Demonstrates: Inheritance (extends Vehicle),
 *               Polymorphism (overrides calculateParkingFee).
 * Parking Rate: LKR 150 per hour.
 */
public class Van extends Vehicle {
    private static final long serialVersionUID = 1L;
    private static final double HOURLY_RATE = 150.0;

    /**
     * Constructor for Van.
     * @param vehicleNumber The registration number.
     * @param ownerName     The owner's name.
     * @param contactInfo   Contact information.
     */
    public Van(String vehicleNumber, String ownerName, String contactInfo) {
        super(vehicleNumber, ownerName, contactInfo, VehicleType.VAN);
    }

    /**
     * Calculates parking fee for a Van — Polymorphism.
     * Rate: LKR 150 per hour (minimum 1 hour).
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
