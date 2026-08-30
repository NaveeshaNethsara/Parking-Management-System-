package models;

import java.io.Serializable;

/**
 * Represents a customer / vehicle owner.
 * Demonstrates: Encapsulation, Association (linked to Vehicle).
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    // Private fields — Encapsulation
    private String customerId;
    private String name;
    private String contactNumber;
    private String vehicleNumber; // Associated vehicle

    /**
     * Constructor for Customer.
     * @param customerId    Unique customer identifier.
     * @param name          Customer name.
     * @param contactNumber Contact phone number.
     * @param vehicleNumber Associated vehicle registration number.
     */
    public Customer(String customerId, String name, String contactNumber, String vehicleNumber) {
        this.customerId = customerId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.vehicleNumber = vehicleNumber;
    }

    // --- Getters and Setters — Encapsulation ---

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    @Override
    public String toString() {
        return name + " (" + vehicleNumber + ")";
    }

    /**
     * Converts to a delimited string for file storage.
     */
    public String toFileString() {
        return customerId + "|" + name + "|" + contactNumber + "|" + vehicleNumber;
    }
}
