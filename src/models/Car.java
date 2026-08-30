package models;

/**
 * Represents a Car vehicle.
 * Demonstrates: Inheritance (extends Vehicle),
 *               Polymorphism (overrides calculateParkingFee).
 * Parking Rate: LKR 100 per hour.
 */
public class Car extends Vehicle {
    private static final long serialVersionUID = 1L;
    private static final double HOURLY_RATE = 100.0;

    /**
     * Constructor for Car.
     * @param vehicleNumber The registration number.
     * @param ownerName     The owner's name.
     * @param contactInfo   Contact information.
     */
    public Car(String vehicleNumber, String ownerName, String contactInfo) {
        super(vehicleNumber, ownerName, contactInfo, VehicleType.CAR);
    }

    /**
     * Calculates parking fee for a Car — Polymorphism.
     * Rate: LKR 100 per hour (minimum 1 hour).
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
