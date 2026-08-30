package services;

import data.VehicleDataStore;
import models.*;
import utils.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Vehicle operations.
 * Handles business logic for CRUD operations on vehicles (FR-01 to FR-05).
 */
public class VehicleManager {
    private VehicleDataStore vehicleDataStore;

    public VehicleManager(VehicleDataStore vehicleDataStore) {
        this.vehicleDataStore = vehicleDataStore;
    }

    /**
     * Registers a new vehicle (FR-01).
     * @param vehicleNumber Registration number.
     * @param ownerName     Owner's name.
     * @param contactInfo   Contact information.
     * @param type          Vehicle type.
     * @return The created Vehicle object.
     * @throws IllegalArgumentException if validation fails.
     */
    public Vehicle registerVehicle(String vehicleNumber, String ownerName, String contactInfo, VehicleType type)
            throws IllegalArgumentException {

        // Validation (FR-18)
        if (!Validator.isValidVehicleNumber(vehicleNumber)) {
            throw new IllegalArgumentException("Invalid vehicle number. Use alphanumeric characters and hyphens (minimum 2 characters).");
        }
        if (!Validator.isNotEmpty(ownerName)) {
            throw new IllegalArgumentException("Owner name cannot be empty.");
        }
        if (!Validator.isValidContactNumber(contactInfo)) {
            throw new IllegalArgumentException("Invalid contact number. Use 7-15 digits, optionally starting with +.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type must be selected.");
        }

        // Check for duplicates
        if (vehicleDataStore.isDuplicate(vehicleNumber.trim())) {
            throw new IllegalArgumentException("A vehicle with number '" + vehicleNumber + "' already exists.");
        }

        // Create vehicle using polymorphism
        Vehicle vehicle;
        switch (type) {
            case CAR:
                vehicle = new Car(vehicleNumber.trim().toUpperCase(), ownerName.trim(), contactInfo.trim());
                break;
            case MOTORCYCLE:
                vehicle = new Motorcycle(vehicleNumber.trim().toUpperCase(), ownerName.trim(), contactInfo.trim());
                break;
            case VAN:
                vehicle = new Van(vehicleNumber.trim().toUpperCase(), ownerName.trim(), contactInfo.trim());
                break;
            default:
                throw new IllegalArgumentException("Unknown vehicle type.");
        }

        vehicleDataStore.add(vehicle);
        return vehicle;
    }

    /**
     * Retrieves all registered vehicles (FR-02).
     */
    public List<Vehicle> getAllVehicles() {
        return vehicleDataStore.getAll();
    }

    /**
     * Searches for a vehicle by registration number (FR-05).
     */
    public Vehicle searchVehicle(String vehicleNumber) {
        if (!Validator.isNotEmpty(vehicleNumber)) {
            return null;
        }
        return vehicleDataStore.findByVehicleNumber(vehicleNumber.trim());
    }

    /**
     * Updates vehicle information (FR-03).
     */
    public void updateVehicle(String vehicleNumber, String ownerName, String contactInfo)
            throws IllegalArgumentException {
        Vehicle existing = vehicleDataStore.findByVehicleNumber(vehicleNumber);
        if (existing == null) {
            throw new IllegalArgumentException("Vehicle not found: " + vehicleNumber);
        }
        if (!Validator.isNotEmpty(ownerName)) {
            throw new IllegalArgumentException("Owner name cannot be empty.");
        }
        if (!Validator.isValidContactNumber(contactInfo)) {
            throw new IllegalArgumentException("Invalid contact number.");
        }

        existing.setOwnerName(ownerName.trim());
        existing.setContactInfo(contactInfo.trim());
        vehicleDataStore.update(existing);
    }

    /**
     * Deletes a vehicle record (FR-04).
     */
    public void deleteVehicle(String vehicleNumber) throws IllegalArgumentException {
        Vehicle existing = vehicleDataStore.findByVehicleNumber(vehicleNumber);
        if (existing == null) {
            throw new IllegalArgumentException("Vehicle not found: " + vehicleNumber);
        }
        if (existing.isParked()) {
            throw new IllegalArgumentException("Cannot delete vehicle '" + vehicleNumber + "' — it is currently parked.");
        }
        vehicleDataStore.delete(vehicleNumber);
    }

    /**
     * Gets vehicles that are currently parked.
     */
    public List<Vehicle> getParkedVehicles() {
        List<Vehicle> parked = new ArrayList<>();
        for (Vehicle v : vehicleDataStore.getAll()) {
            if (v.isParked()) {
                parked.add(v);
            }
        }
        return parked;
    }

    /**
     * Gets the underlying data store (for direct access when needed).
     */
    public VehicleDataStore getDataStore() {
        return vehicleDataStore;
    }
}
