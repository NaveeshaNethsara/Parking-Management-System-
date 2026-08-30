import data.*;
import gui.MainFrame;
import models.VehicleType;
import services.*;

import javax.swing.*;

/**
 * Entry point for the Parking Management System application.
 * Initializes the layers, seeds default sample data if needed,
 * and launches the Swing GUI.
 */
public class ParkingApp {

    public static void main(String[] args) {
        // Set Look and Feel for modern appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        // Initialize Data Stores
        VehicleDataStore vehicleDataStore = new VehicleDataStore();
        SlotDataStore slotDataStore = new SlotDataStore();
        TicketDataStore ticketDataStore = new TicketDataStore();
        PaymentDataStore paymentDataStore = new PaymentDataStore();

        // Seed initial sample parking slots and vehicles if empty (for coursework presentation)
        seedSampleDataIfEmpty(vehicleDataStore, slotDataStore);

        // Initialize Service / Manager Layer
        VehicleManager vehicleManager = new VehicleManager(vehicleDataStore);
        ParkingSlotManager slotManager = new ParkingSlotManager(slotDataStore);
        ParkingManager parkingManager = new ParkingManager(ticketDataStore, vehicleManager, slotManager);
        PaymentManager paymentManager = new PaymentManager(paymentDataStore);
        DashboardManager dashboardManager = new DashboardManager(vehicleManager, slotManager, parkingManager, paymentManager);

        // Launch GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(
                    vehicleManager,
                    slotManager,
                    parkingManager,
                    paymentManager,
                    dashboardManager
            );
            mainFrame.setVisible(true);
        });
    }

    /**
     * Seeds initial slots and sample vehicles if the database files are freshly initialized.
     * Provides an immediate ready-to-test environment for the examiner/user.
     */
    private static void seedSampleDataIfEmpty(VehicleDataStore vehicleStore, SlotDataStore slotStore) {
        // Seed default slots if none exist
        if (slotStore.getAll().isEmpty()) {
            slotStore.add(new models.ParkingSlot("SLT-001", "A1", VehicleType.CAR));
            slotStore.add(new models.ParkingSlot("SLT-002", "A2", VehicleType.CAR));
            slotStore.add(new models.ParkingSlot("SLT-003", "A3", VehicleType.CAR));
            slotStore.add(new models.ParkingSlot("SLT-004", "A4", VehicleType.CAR));
            slotStore.add(new models.ParkingSlot("SLT-005", "A5", VehicleType.CAR));

            slotStore.add(new models.ParkingSlot("SLT-006", "B1", VehicleType.MOTORCYCLE));
            slotStore.add(new models.ParkingSlot("SLT-007", "B2", VehicleType.MOTORCYCLE));
            slotStore.add(new models.ParkingSlot("SLT-008", "B3", VehicleType.MOTORCYCLE));

            slotStore.add(new models.ParkingSlot("SLT-009", "C1", VehicleType.VAN));
            slotStore.add(new models.ParkingSlot("SLT-010", "C2", VehicleType.VAN));
        }

        // Seed default sample vehicles if none exist
        if (vehicleStore.getAll().isEmpty()) {
            vehicleStore.add(new models.Car("CAB-1234", "Kamal Perera", "0771234567"));
            vehicleStore.add(new models.Car("WP-CAJ-5678", "Nimal Silva", "0719876543"));
            vehicleStore.add(new models.Motorcycle("BIKE-9012", "Sunil Fernando", "0755551234"));
            vehicleStore.add(new models.Van("VAN-3456", "Ruwan Jayasuriya", "0764449876"));
        }
    }
}
