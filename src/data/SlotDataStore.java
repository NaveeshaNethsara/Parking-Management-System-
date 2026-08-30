package data;

import models.ParkingSlot;
import models.VehicleType;
import utils.Constants;
import utils.IDGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based data store for ParkingSlot objects.
 * Implements the DataStore interface — demonstrates Abstraction.
 */
public class SlotDataStore implements DataStore<ParkingSlot> {
    private List<ParkingSlot> slots;
    private final String filePath;

    public SlotDataStore() {
        this.slots = new ArrayList<>();
        this.filePath = Constants.DATA_DIR + File.separator + Constants.SLOTS_FILE;
        ensureDataDirectory();
        loadAll();
    }

    private void ensureDataDirectory() {
        File dir = new File(Constants.DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void add(ParkingSlot slot) {
        slots.add(slot);
        saveAll();
    }

    @Override
    public List<ParkingSlot> getAll() {
        return new ArrayList<>(slots);
    }

    @Override
    public void update(ParkingSlot updatedSlot) {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getSlotId().equals(updatedSlot.getSlotId())) {
                slots.set(i, updatedSlot);
                break;
            }
        }
        saveAll();
    }

    @Override
    public void delete(String slotId) {
        slots.removeIf(s -> s.getSlotId().equals(slotId));
        saveAll();
    }

    /**
     * Finds a parking slot by its ID.
     */
    public ParkingSlot findById(String slotId) {
        for (ParkingSlot s : slots) {
            if (s.getSlotId().equals(slotId)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Finds a parking slot by its slot number.
     */
    public ParkingSlot findBySlotNumber(String slotNumber) {
        for (ParkingSlot s : slots) {
            if (s.getSlotNumber().equalsIgnoreCase(slotNumber)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Returns all available (unoccupied) parking slots.
     */
    public List<ParkingSlot> getAvailableSlots() {
        List<ParkingSlot> available = new ArrayList<>();
        for (ParkingSlot s : slots) {
            if (!s.isOccupied()) {
                available.add(s);
            }
        }
        return available;
    }

    /**
     * Returns all occupied parking slots.
     */
    public List<ParkingSlot> getOccupiedSlots() {
        List<ParkingSlot> occupied = new ArrayList<>();
        for (ParkingSlot s : slots) {
            if (s.isOccupied()) {
                occupied.add(s);
            }
        }
        return occupied;
    }

    /**
     * Checks if a slot number already exists.
     */
    public boolean isSlotNumberDuplicate(String slotNumber) {
        return findBySlotNumber(slotNumber) != null;
    }

    @Override
    public void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ParkingSlot s : slots) {
                writer.write(s.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving slots: " + e.getMessage());
        }
    }

    @Override
    public void loadAll() {
        slots.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                ParkingSlot slot = parseSlot(line);
                if (slot != null) {
                    slots.add(slot);
                    // Track highest ID for counter
                    try {
                        int idNum = Integer.parseInt(slot.getSlotId().replace("SLT-", ""));
                        if (idNum > maxId) maxId = idNum;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading slots: " + e.getMessage());
        }
        IDGenerator.setSlotCounter(maxId);
    }

    /**
     * Parses a pipe-delimited string into a ParkingSlot.
     * Format: slotId|slotNumber|slotType|isOccupied|assignedVehicleNumber
     */
    private ParkingSlot parseSlot(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 5) return null;

            String slotId = parts[0];
            String slotNumber = parts[1];
            VehicleType slotType = VehicleType.valueOf(parts[2]);
            boolean isOccupied = Boolean.parseBoolean(parts[3]);
            String vehicleNum = parts[4].equals("NONE") ? null : parts[4];

            ParkingSlot slot = new ParkingSlot(slotId, slotNumber, slotType);
            slot.setOccupied(isOccupied);
            slot.setAssignedVehicleNumber(vehicleNum);
            return slot;
        } catch (Exception e) {
            System.err.println("Error parsing slot line: " + line + " - " + e.getMessage());
            return null;
        }
    }
}
