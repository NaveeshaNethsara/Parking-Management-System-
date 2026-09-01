package services;

import data.SlotDataStore;
import models.ParkingSlot;
import models.VehicleType;
import utils.IDGenerator;
import utils.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Parking Slot operations (FR-06, FR-07, FR-08, FR-15).
 */
public class ParkingSlotManager {
    private SlotDataStore slotDataStore;

    public ParkingSlotManager(SlotDataStore slotDataStore) {
        this.slotDataStore = slotDataStore;
    }

    /**
     * Adds a new parking slot (FR-06 — Create).
     * @param slotNumber The slot number/label (e.g., "A1").
     * @param slotType   The type of vehicle this slot accommodates.
     * @return The created ParkingSlot.
     */
    public ParkingSlot addSlot(String slotNumber, VehicleType slotType) throws IllegalArgumentException {
        if (!Validator.isValidSlotNumber(slotNumber)) {
            throw new IllegalArgumentException("Invalid slot number. Use alphanumeric characters.");
        }
        if (slotType == null) {
            throw new IllegalArgumentException("Slot type must be selected.");
        }
        if (slotDataStore.isSlotNumberDuplicate(slotNumber.trim())) {
            throw new IllegalArgumentException("Slot number '" + slotNumber + "' already exists.");
        }

        String slotId = IDGenerator.generateSlotId();
        ParkingSlot slot = new ParkingSlot(slotId, slotNumber.trim().toUpperCase(), slotType);
        slotDataStore.add(slot);
        return slot;
    }

    /**
     * Gets all parking slots (FR-06 — Read).
     */
    public List<ParkingSlot> getAllSlots() {
        return slotDataStore.getAll();
    }

    /**
     * Updates a parking slot's information (FR-06 — Update).
     */
    public void updateSlot(String slotId, String slotNumber, VehicleType slotType) throws IllegalArgumentException {
        ParkingSlot existing = slotDataStore.findById(slotId);
        if (existing == null) {
            throw new IllegalArgumentException("Parking slot not found.");
        }
        if (existing.isOccupied()) {
            throw new IllegalArgumentException("Cannot update an occupied slot.");
        }
        if (!Validator.isValidSlotNumber(slotNumber)) {
            throw new IllegalArgumentException("Invalid slot number.");
        }

        // Check for duplicate slot number (excluding current slot)
        ParkingSlot duplicate = slotDataStore.findBySlotNumber(slotNumber.trim());
        if (duplicate != null && !duplicate.getSlotId().equals(slotId)) {
            throw new IllegalArgumentException("Slot number '" + slotNumber + "' already exists.");
        }

        existing.setSlotNumber(slotNumber.trim().toUpperCase());
        existing.setSlotType(slotType);
        slotDataStore.update(existing);
    }

    /**
     * Deletes a parking slot (FR-06 — Delete).
     */
    public void deleteSlot(String slotId) throws IllegalArgumentException {
        ParkingSlot existing = slotDataStore.findById(slotId);
        if (existing == null) {
            throw new IllegalArgumentException("Parking slot not found.");
        }
        if (existing.isOccupied()) {
            throw new IllegalArgumentException("Cannot delete an occupied slot. Release the vehicle first.");
        }
        slotDataStore.delete(slotId);
    }

    /**
     * Gets all available parking slots.
     * Demonstrates: Static Polymorphism (Method Overloading - Base version).
     */
    public List<ParkingSlot> getAvailableSlots() {
        return slotDataStore.getAvailableSlots();
    }

    /**
     * Overloaded method: Gets all available slots filtered by a specific vehicle type.
     * Demonstrates: Static Polymorphism (Method Overloading - Parameterized version).
     * @param type The vehicle type to filter by.
     * @return List of matching available slots.
     */
    public List<ParkingSlot> getAvailableSlots(VehicleType type) {
        List<ParkingSlot> matching = new ArrayList<>();
        for (ParkingSlot s : slotDataStore.getAvailableSlots()) {
            if (s.getSlotType() == type) {
                matching.add(s);
            }
        }
        return matching;
    }

    /**
     * Legacy alias for getAvailableSlots(VehicleType type).
     */
    public List<ParkingSlot> getAvailableSlotsByType(VehicleType type) {
        return getAvailableSlots(type);
    }

    /**
     * Gets all occupied slots (FR-07).
     */
    public List<ParkingSlot> getOccupiedSlots() {
        return slotDataStore.getOccupiedSlots();
    }

    /**
     * Assigns a vehicle to a parking slot (FR-08).
     */
    public void assignSlot(String slotId, String vehicleNumber) throws IllegalArgumentException {
        ParkingSlot slot = slotDataStore.findById(slotId);
        if (slot == null) {
            throw new IllegalArgumentException("Parking slot not found.");
        }
        if (slot.isOccupied()) {
            throw new IllegalArgumentException("Slot '" + slot.getSlotNumber() + "' is already occupied.");
        }
        slot.assignVehicle(vehicleNumber);
        slotDataStore.update(slot);
    }

    /**
     * Releases a parking slot (FR-15).
     */
    public void releaseSlot(String slotId) throws IllegalArgumentException {
        ParkingSlot slot = slotDataStore.findById(slotId);
        if (slot == null) {
            throw new IllegalArgumentException("Parking slot not found.");
        }
        if (!slot.isOccupied()) {
            throw new IllegalArgumentException("Slot '" + slot.getSlotNumber() + "' is already available.");
        }
        slot.releaseSlot();
        slotDataStore.update(slot);
    }

    /**
     * Finds a slot by its unique ID.
     * Demonstrates: Static Polymorphism (Method Overloading - Search by ID).
     */
    public ParkingSlot findSlot(String slotId) {
        return findSlotById(slotId);
    }

    /**
     * Finds a slot by slot number or custom search mode.
     * Demonstrates: Static Polymorphism (Method Overloading - Search by Number with boolean flag).
     * @param identifier The slot ID or slot number.
     * @param byNumber   If true, searches by slot number (e.g. "A1"); if false, searches by slot ID.
     */
    public ParkingSlot findSlot(String identifier, boolean byNumber) {
        return byNumber ? findSlotByNumber(identifier) : findSlotById(identifier);
    }

    /**
     * Finds a slot by its ID.
     */
    public ParkingSlot findSlotById(String slotId) {
        return slotDataStore.findById(slotId);
    }

    /**
     * Finds a slot by its number.
     */
    public ParkingSlot findSlotByNumber(String slotNumber) {
        return slotDataStore.findBySlotNumber(slotNumber);
    }

    /**
     * Gets the data store for direct access.
     */
    public SlotDataStore getDataStore() {
        return slotDataStore;
    }
}
