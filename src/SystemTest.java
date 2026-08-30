import data.*;
import models.*;
import services.*;

/**
 * Automated functional test suite verifying OOP requirements, CRUD operations,
 * fee calculations, slot-to-vehicle type compatibility, and the full parking lifecycle.
 */
public class SystemTest {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      RUNNING PARKING SYSTEM AUTOMATED TESTS      ");
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        // Initialize stores
        VehicleDataStore vehicleStore = new VehicleDataStore();
        SlotDataStore slotStore = new SlotDataStore();
        TicketDataStore ticketStore = new TicketDataStore();
        PaymentDataStore paymentStore = new PaymentDataStore();

        VehicleManager vehicleManager = new VehicleManager(vehicleStore);
        ParkingSlotManager slotManager = new ParkingSlotManager(slotStore);
        ParkingManager parkingManager = new ParkingManager(ticketStore, vehicleManager, slotManager);
        PaymentManager paymentManager = new PaymentManager(paymentStore);
        DashboardManager dashboardManager = new DashboardManager(vehicleManager, slotManager, parkingManager, paymentManager);

        // Test 1: Polymorphic Fee Calculation
        try {
            Vehicle car = new Car("TEST-CAR-1", "Owner A", "0771234567");
            Vehicle bike = new Motorcycle("TEST-BIKE-1", "Owner B", "0771234568");
            Vehicle van = new Van("TEST-VAN-1", "Owner C", "0771234569");

            double carFee = car.calculateParkingFee(3);
            double bikeFee = bike.calculateParkingFee(3);
            double vanFee = van.calculateParkingFee(3);

            assert carFee == 300.0 : "Car fee should be 300 for 3 hrs";
            assert bikeFee == 150.0 : "Bike fee should be 150 for 3 hrs";
            assert vanFee == 450.0 : "Van fee should be 450 for 3 hrs";

            System.out.println("[PASS] Test 1: Polymorphism Fee Calculations (Car=" + carFee + ", Bike=" + bikeFee + ", Van=" + vanFee + ")");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 1: " + t.getMessage());
            failed++;
        }

        // Test 2: Vehicle CRUD Operations
        try {
            String testPlate = "TEST-PLATE-99";
            // Clean up if exists
            try { vehicleManager.deleteVehicle(testPlate); } catch (Exception ignored) {}

            Vehicle v = vehicleManager.registerVehicle(testPlate, "Test User", "0771112233", VehicleType.CAR);
            assert v != null : "Vehicle registration returned null";

            Vehicle found = vehicleManager.searchVehicle(testPlate);
            assert found != null && found.getOwnerName().equals("Test User") : "Vehicle search failed";

            vehicleManager.updateVehicle(testPlate, "Updated User", "0779998877");
            found = vehicleManager.searchVehicle(testPlate);
            assert found.getOwnerName().equals("Updated User") : "Vehicle update failed";

            System.out.println("[PASS] Test 2: Vehicle CRUD operations succeeded");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 2: " + t.getMessage());
            failed++;
        }

        // Test 3: Slot CRUD and Availability
        try {
            String testSlotNum = "Z99";
            // Clean up
            ParkingSlot existing = slotManager.findSlotByNumber(testSlotNum);
            if (existing != null) {
                slotManager.deleteSlot(existing.getSlotId());
            }

            ParkingSlot newSlot = slotManager.addSlot(testSlotNum, VehicleType.CAR);
            assert !newSlot.isOccupied() : "New slot should be available";

            System.out.println("[PASS] Test 3: Slot Management & Availability succeeded");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 3: " + t.getMessage());
            failed++;
        }

        // Test 4: Full Parking Lifecycle (Entry -> Parked -> Exit -> Fee -> Payment -> Release Slot)
        try {
            String testPlate = "TEST-PLATE-99";
            ParkingSlot testSlot = slotManager.findSlotByNumber("Z99");

            // Entry
            ParkingTicket ticket = parkingManager.recordEntry(testPlate, testSlot.getSlotId());
            assert ticket.isActive() : "Ticket should be active";
            assert testSlot.isOccupied() : "Slot should be marked occupied";

            Vehicle parkedV = vehicleManager.searchVehicle(testPlate);
            assert parkedV.isParked() : "Vehicle should be marked parked";

            // Exit
            ParkingTicket exitTicket = parkingManager.recordExit(ticket.getTicketId());
            assert !exitTicket.isActive() : "Ticket should be inactive after exit";
            assert exitTicket.getFee() >= 100.0 : "Fee should be at least 1 hr charge (100 LKR)";
            assert !testSlot.isOccupied() : "Slot should be released and available";
            assert !parkedV.isParked() : "Vehicle should no longer be marked parked";

            // Payment
            Payment payment = paymentManager.recordPayment(exitTicket.getTicketId(), exitTicket.getFee(), "Cash");
            assert payment != null : "Payment record created";
            assert paymentManager.getTotalRevenue() > 0 : "Total revenue should reflect payment";

            System.out.println("[PASS] Test 4: Full Parking Lifecycle (Entry, Exit, Fee Calc, Payment, Slot Release) succeeded");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 4: " + t.getMessage());
            failed++;
        }

        // Test 5: Slot Type Matching Validation (Cannot park Car in Bike slot)
        try {
            // Register a Car and a Bike slot
            String carPlate = "TEST-CAR-TYPE-1";
            String bikeSlotNum = "BK99";
            try { vehicleManager.deleteVehicle(carPlate); } catch (Exception ignored) {}
            ParkingSlot oldSlot = slotManager.findSlotByNumber(bikeSlotNum);
            if (oldSlot != null) slotManager.deleteSlot(oldSlot.getSlotId());

            vehicleManager.registerVehicle(carPlate, "Car Driver", "0771234999", VehicleType.CAR);
            ParkingSlot bikeSlot = slotManager.addSlot(bikeSlotNum, VehicleType.MOTORCYCLE);

            boolean threwException = false;
            try {
                // Should throw IllegalArgumentException due to slot type mismatch
                parkingManager.recordEntry(carPlate, bikeSlot.getSlotId());
            } catch (IllegalArgumentException e) {
                threwException = true;
            }

            assert threwException : "System should block parking a Car in a Motorcycle slot";
            System.out.println("[PASS] Test 5: Vehicle-to-Slot type compatibility enforcement succeeded");
            passed++;

            // Clean up
            vehicleManager.deleteVehicle(carPlate);
            slotManager.deleteSlot(bikeSlot.getSlotId());
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 5: " + t.getMessage());
            failed++;
        }

        // Test 6: Dashboard aggregation
        try {
            assert dashboardManager.getTotalSlots() > 0 : "Dashboard should count slots";
            assert dashboardManager.getTotalRegisteredVehicles() > 0 : "Dashboard should count vehicles";
            System.out.println("[PASS] Test 6: Dashboard statistics aggregation succeeded");
            passed++;
        } catch (Throwable t) {
            System.err.println("[FAIL] Test 6: " + t.getMessage());
            failed++;
        }

        System.out.println("==================================================");
        System.out.println("TEST SUMMARY: " + passed + " Passed, " + failed + " Failed");
        System.out.println("==================================================");

        // Clean up test records
        try {
            vehicleManager.deleteVehicle("TEST-PLATE-99");
            ParkingSlot s = slotManager.findSlotByNumber("Z99");
            if (s != null) slotManager.deleteSlot(s.getSlotId());
        } catch (Exception ignored) {}
    }
}
