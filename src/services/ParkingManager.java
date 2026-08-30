package services;

import data.TicketDataStore;
import models.*;
import utils.IDGenerator;

import java.util.List;

/**
 * Service class for managing parking operations.
 * Coordinates vehicle entry, exit, ticket generation, fee calculation (FR-09 to FR-13, FR-16).
 * This is the central business logic class that coordinates between vehicles, slots, and tickets.
 */
public class ParkingManager {
    private TicketDataStore ticketDataStore;
    private VehicleManager vehicleManager;
    private ParkingSlotManager slotManager;

    /**
     * Constructor — Association with VehicleManager and ParkingSlotManager.
     */
    public ParkingManager(TicketDataStore ticketDataStore, VehicleManager vehicleManager, ParkingSlotManager slotManager) {
        this.ticketDataStore = ticketDataStore;
        this.vehicleManager = vehicleManager;
        this.slotManager = slotManager;
    }

    /**
     * Records a vehicle entry and generates a parking ticket (FR-09, FR-10).
     * Enforces vehicle-to-slot type matching rules.
     * @param vehicleNumber The vehicle's registration number.
     * @param slotId        The ID of the parking slot to assign.
     * @return The generated ParkingTicket.
     */
    public ParkingTicket recordEntry(String vehicleNumber, String slotId) throws IllegalArgumentException {
        // Validate vehicle exists
        Vehicle vehicle = vehicleManager.searchVehicle(vehicleNumber);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle '" + vehicleNumber + "' is not registered. Please register it first.");
        }

        // Check if vehicle is already parked
        if (vehicle.isParked()) {
            throw new IllegalArgumentException("Vehicle '" + vehicleNumber + "' is already parked.");
        }

        // Validate slot
        ParkingSlot slot = slotManager.findSlotById(slotId);
        if (slot == null) {
            throw new IllegalArgumentException("Parking slot not found.");
        }
        if (slot.isOccupied()) {
            throw new IllegalArgumentException("Slot '" + slot.getSlotNumber() + "' is already occupied.");
        }

        // Validate slot type compatibility with vehicle type
        if (slot.getSlotType() != vehicle.getVehicleType()) {
            throw new IllegalArgumentException("Slot type mismatch: Cannot park a " +
                    vehicle.getVehicleType().getDisplayName() + " in a " +
                    slot.getSlotType().getDisplayName() + " slot (" + slot.getSlotNumber() + ").");
        }

        // Check available slots
        if (slotManager.getAvailableSlotsByType(vehicle.getVehicleType()).isEmpty()) {
            throw new IllegalArgumentException("No available parking slots for vehicle type: " +
                    vehicle.getVehicleType().getDisplayName());
        }

        // Assign slot to vehicle
        slotManager.assignSlot(slotId, vehicleNumber);

        // Mark vehicle as parked
        vehicle.setParked(true);
        vehicleManager.getDataStore().update(vehicle);

        // Generate parking ticket
        String ticketId = IDGenerator.generateTicketId();
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicleNumber, slot.getSlotNumber());
        ticketDataStore.add(ticket);

        return ticket;
    }

    /**
     * Records a vehicle exit, calculates fee, and releases the slot (FR-11, FR-12, FR-13, FR-15).
     * @param ticketId The ticket ID for the parking session.
     * @return The updated ParkingTicket with exit info, duration, and fee.
     */
    public ParkingTicket recordExit(String ticketId) throws IllegalArgumentException {
        // Find the active ticket
        ParkingTicket ticket = ticketDataStore.findById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket '" + ticketId + "' not found.");
        }
        if (!ticket.isActive()) {
            throw new IllegalArgumentException("Ticket '" + ticketId + "' has already been processed.");
        }

        // Record exit time and calculate duration
        ticket.recordExit();

        // Calculate fee using polymorphism
        Vehicle vehicle = vehicleManager.searchVehicle(ticket.getVehicleNumber());
        if (vehicle != null) {
            double fee = vehicle.calculateParkingFee(ticket.getDurationHours());
            ticket.setFee(fee);

            // Mark vehicle as not parked
            vehicle.setParked(false);
            vehicleManager.getDataStore().update(vehicle);
        }

        // Release the parking slot
        ParkingSlot slot = slotManager.findSlotByNumber(ticket.getSlotNumber());
        if (slot != null) {
            slotManager.releaseSlot(slot.getSlotId());
        }

        // Update ticket in data store
        ticketDataStore.update(ticket);

        return ticket;
    }

    /**
     * Gets all parking tickets/records (FR-16).
     */
    public List<ParkingTicket> getAllTickets() {
        return ticketDataStore.getAll();
    }

    /**
     * Gets all active tickets (currently parked vehicles).
     */
    public List<ParkingTicket> getActiveTickets() {
        return ticketDataStore.getActiveTickets();
    }

    /**
     * Gets all completed tickets.
     */
    public List<ParkingTicket> getCompletedTickets() {
        return ticketDataStore.getCompletedTickets();
    }

    /**
     * Finds an active ticket for a specific vehicle.
     */
    public ParkingTicket findActiveTicketByVehicle(String vehicleNumber) {
        return ticketDataStore.findActiveTicketByVehicle(vehicleNumber);
    }

    /**
     * Finds a ticket by its ID.
     */
    public ParkingTicket findTicketById(String ticketId) {
        return ticketDataStore.findById(ticketId);
    }

    /**
     * Gets the ticket data store.
     */
    public TicketDataStore getDataStore() {
        return ticketDataStore;
    }
}
