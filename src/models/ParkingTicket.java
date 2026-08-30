package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;

/**
 * Represents a parking ticket/record for a vehicle's parking session.
 * Demonstrates: Association (links Vehicle, ParkingSlot),
 *               Encapsulation (private fields + getters/setters).
 */
public class ParkingTicket implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Private fields — Encapsulation
    private String ticketId;
    private String vehicleNumber;
    private String slotNumber;
    private String entryTime;   // Stored as formatted string for file compatibility
    private String exitTime;    // null if vehicle is still parked
    private long durationHours;
    private double fee;
    private boolean isActive;   // true if vehicle is currently parked

    /**
     * Constructor for ParkingTicket — created when a vehicle enters.
     * @param ticketId      Unique ticket identifier.
     * @param vehicleNumber The vehicle's registration number.
     * @param slotNumber    The assigned parking slot number.
     */
    public ParkingTicket(String ticketId, String vehicleNumber, String slotNumber) {
        this.ticketId = ticketId;
        this.vehicleNumber = vehicleNumber;
        this.slotNumber = slotNumber;
        this.entryTime = LocalDateTime.now().format(FORMATTER);
        this.exitTime = null;
        this.durationHours = 0;
        this.fee = 0.0;
        this.isActive = true;
    }

    // --- Business Logic Methods ---

    /**
     * Records the vehicle exit and calculates the parking duration.
     */
    public void recordExit() {
        this.exitTime = LocalDateTime.now().format(FORMATTER);
        this.isActive = false;
        calculateDuration();
    }

    /**
     * Calculates the parking duration in hours.
     */
    public void calculateDuration() {
        if (entryTime != null && exitTime != null) {
            LocalDateTime entry = LocalDateTime.parse(entryTime, FORMATTER);
            LocalDateTime exit = LocalDateTime.parse(exitTime, FORMATTER);
            Duration duration = Duration.between(entry, exit);
            this.durationHours = duration.toHours();
            if (duration.toMinutes() % 60 > 0) {
                this.durationHours++; // Round up partial hours
            }
            if (this.durationHours <= 0) {
                this.durationHours = 1; // Minimum 1 hour
            }
        }
    }

    // --- Getters and Setters — Encapsulation ---

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public long getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(long durationHours) {
        this.durationHours = durationHours;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Ticket [" + ticketId + "] Vehicle: " + vehicleNumber + " Slot: " + slotNumber;
    }

    /**
     * Converts to a delimited string for file storage.
     */
    public String toFileString() {
        String exit = (exitTime != null) ? exitTime : "ACTIVE";
        return ticketId + "|" + vehicleNumber + "|" + slotNumber + "|" + entryTime + "|"
                + exit + "|" + durationHours + "|" + fee + "|" + isActive;
    }
}
