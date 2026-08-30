package data;

import models.*;
import utils.Constants;
import utils.IDGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based data store for Vehicle objects.
 * Implements the DataStore interface — demonstrates Abstraction.
 * Reads/writes vehicle data to a text file.
 */
public class VehicleDataStore implements DataStore<Vehicle> {
    private List<Vehicle> vehicles;
    private final String filePath;

    public VehicleDataStore() {
        this.vehicles = new ArrayList<>();
        this.filePath = Constants.DATA_DIR + File.separator + Constants.VEHICLES_FILE;
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
    public void add(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveAll();
    }

    @Override
    public List<Vehicle> getAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public void update(Vehicle updatedVehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getVehicleNumber().equalsIgnoreCase(updatedVehicle.getVehicleNumber())) {
                vehicles.set(i, updatedVehicle);
                break;
            }
        }
        saveAll();
    }

    @Override
    public void delete(String vehicleNumber) {
        vehicles.removeIf(v -> v.getVehicleNumber().equalsIgnoreCase(vehicleNumber));
        saveAll();
    }

    /**
     * Finds a vehicle by its registration number.
     * @param vehicleNumber The vehicle number to search for.
     * @return The Vehicle object, or null if not found.
     */
    public Vehicle findByVehicleNumber(String vehicleNumber) {
        for (Vehicle v : vehicles) {
            if (v.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Checks if a vehicle with the given number already exists.
     * @param vehicleNumber The vehicle number to check.
     * @return true if a duplicate exists, false otherwise.
     */
    public boolean isDuplicate(String vehicleNumber) {
        return findByVehicleNumber(vehicleNumber) != null;
    }

    @Override
    public void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Vehicle v : vehicles) {
                writer.write(v.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving vehicles: " + e.getMessage());
        }
    }

    @Override
    public void loadAll() {
        vehicles.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Vehicle vehicle = parseVehicle(line);
                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }

    /**
     * Parses a pipe-delimited string back into a Vehicle object.
     * Format: vehicleNumber|ownerName|contactInfo|vehicleType|isParked
     */
    private Vehicle parseVehicle(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 5) return null;

            String vehicleNumber = parts[0];
            String ownerName = parts[1];
            String contactInfo = parts[2];
            VehicleType type = VehicleType.valueOf(parts[3]);
            boolean isParked = Boolean.parseBoolean(parts[4]);

            Vehicle vehicle;
            switch (type) {
                case CAR:
                    vehicle = new Car(vehicleNumber, ownerName, contactInfo);
                    break;
                case MOTORCYCLE:
                    vehicle = new Motorcycle(vehicleNumber, ownerName, contactInfo);
                    break;
                case VAN:
                    vehicle = new Van(vehicleNumber, ownerName, contactInfo);
                    break;
                default:
                    return null;
            }
            vehicle.setParked(isParked);
            return vehicle;
        } catch (Exception e) {
            System.err.println("Error parsing vehicle line: " + line + " - " + e.getMessage());
            return null;
        }
    }
}
