package models;

import java.io.Serializable;

/**
 * Represents a parking slot in the parking facility.
 * Demonstrates: Encapsulation (private fields + getters/setters),
 *               Association (linked to a Vehicle when occupied).
 */
public class ParkingSlot implements Serializable {
    private static final long serialVersionUID = 1L;

    // Private fields — Encapsulation
    private String slotId;
    private String slotNumber;       // e.g., "A1", "A2", "B1"
    private VehicleType slotType;    // Type of vehicle this slot accommodates
    private boolean isOccupied;
    private String assignedVehicleNumber; // null if available

    /**
     * Constructor for ParkingSlot.
     * @param slotId     Unique identifier for the slot.
     * @param slotNumber Display number/label (e.g., "A1").
     * @param slotType   The type of vehicle this slot can accommodate.
     */
    public ParkingSlot(String slotId, String slotNumber, VehicleType slotType) {
        this.slotId = slotId;
        this.slotNumber = slotNumber;
        this.slotType = slotType;
        this.isOccupied = false;
        this.assignedVehicleNumber = null;
    }

    // --- Getters and Setters — Encapsulation ---

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public VehicleType getSlotType() {
        return slotType;
    }

    public void setSlotType(VehicleType slotType) {
        this.slotType = slotType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getAssignedVehicleNumber() {
        return assignedVehicleNumber;
    }

    public void setAssignedVehicleNumber(String assignedVehicleNumber) {
        this.assignedVehicleNumber = assignedVehicleNumber;
    }

    /**
     * Assigns a vehicle to this slot.
     * @param vehicleNumber The vehicle registration number to assign.
     */
    public void assignVehicle(String vehicleNumber) {
        this.assignedVehicleNumber = vehicleNumber;
        this.isOccupied = true;
    }

    /**
     * Releases this slot, making it available again.
     */
    public void releaseSlot() {
        this.assignedVehicleNumber = null;
        this.isOccupied = false;
    }

    /**
     * Returns the availability status as a display string.
     */
    public String getStatusDisplay() {
        return isOccupied ? "Occupied" : "Available";
    }

    @Override
    public String toString() {
        return slotNumber + " (" + slotType.getDisplayName() + ") - " + getStatusDisplay();
    }

    /**
     * Converts to a delimited string for file storage.
     */
    public String toFileString() {
        String vehicleNum = (assignedVehicleNumber != null) ? assignedVehicleNumber : "NONE";
        return slotId + "|" + slotNumber + "|" + slotType.name() + "|" + isOccupied + "|" + vehicleNum;
    }
}
