package services;

import java.util.List;

/**
 * Service class for aggregating dashboard statistics (FR-17).
 * Provides summary information about the parking facility's current state.
 */
public class DashboardManager {
    private VehicleManager vehicleManager;
    private ParkingSlotManager slotManager;
    private ParkingManager parkingManager;
    private PaymentManager paymentManager;

    /**
     * Constructor — Association with all other managers.
     */
    public DashboardManager(VehicleManager vehicleManager, ParkingSlotManager slotManager,
                           ParkingManager parkingManager, PaymentManager paymentManager) {
        this.vehicleManager = vehicleManager;
        this.slotManager = slotManager;
        this.parkingManager = parkingManager;
        this.paymentManager = paymentManager;
    }

    /**
     * Gets the total number of parking slots.
     */
    public int getTotalSlots() {
        return slotManager.getAllSlots().size();
    }

    /**
     * Gets the number of available (unoccupied) slots.
     */
    public int getAvailableSlots() {
        return slotManager.getAvailableSlots().size();
    }

    /**
     * Gets the number of occupied slots.
     */
    public int getOccupiedSlots() {
        return slotManager.getOccupiedSlots().size();
    }

    /**
     * Gets the number of currently parked vehicles.
     */
    public int getCurrentlyParkedVehicles() {
        return vehicleManager.getParkedVehicles().size();
    }

    /**
     * Gets the total number of registered vehicles.
     */
    public int getTotalRegisteredVehicles() {
        return vehicleManager.getAllVehicles().size();
    }

    /**
     * Gets the total number of parking records (all tickets).
     */
    public int getTotalParkingRecords() {
        return parkingManager.getAllTickets().size();
    }

    /**
     * Gets the total revenue from all payments.
     */
    public double getTotalRevenue() {
        return paymentManager.getTotalRevenue();
    }

    /**
     * Gets the number of active parking sessions.
     */
    public int getActiveSessions() {
        return parkingManager.getActiveTickets().size();
    }
}
