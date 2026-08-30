package models;

import java.io.Serializable;

/**
 * Abstract base class representing a Vehicle.
 * Demonstrates: Encapsulation (private fields + getters/setters),
 *               Abstraction (abstract method for fee calculation),
 *               Inheritance (base class for Car, Motorcycle, Van).
 */
public abstract class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    // Private fields — Encapsulation
    private String vehicleNumber;
    private String ownerName;
    private String contactInfo;
    private VehicleType vehicleType;
    private boolean isParked; // true if currently parked

    /**
     * Constructor for Vehicle.
     * @param vehicleNumber The registration number of the vehicle.
     * @param ownerName     The name of the vehicle owner.
     * @param contactInfo   Contact information (phone number).
     * @param vehicleType   The type of vehicle (CAR, MOTORCYCLE, VAN).
     */
    public Vehicle(String vehicleNumber, String ownerName, String contactInfo, VehicleType vehicleType) {
        this.vehicleNumber = vehicleNumber;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
        this.vehicleType = vehicleType;
        this.isParked = false;
    }

    // --- Abstract Method — Polymorphism ---
    /**
     * Calculates the parking fee based on the number of hours parked.
     * Each vehicle subclass provides its own implementation (Polymorphism).
     * @param hours The number of hours the vehicle was parked.
     * @return The calculated parking fee.
     */
    public abstract double calculateParkingFee(long hours);

    // --- Getters and Setters — Encapsulation ---

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isParked() {
        return isParked;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    /**
     * Returns a string representation of the vehicle.
     */
    @Override
    public String toString() {
        return vehicleType.getDisplayName() + " [" + vehicleNumber + "] - " + ownerName;
    }

    /**
     * Converts the Vehicle object to a delimited string for file storage.
     * @return A pipe-delimited string representation.
     */
    public String toFileString() {
        return vehicleNumber + "|" + ownerName + "|" + contactInfo + "|" + vehicleType.name() + "|" + isParked;
    }
}
