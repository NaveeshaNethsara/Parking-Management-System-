package data;

import models.ParkingTicket;
import utils.Constants;
import utils.IDGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based data store for ParkingTicket objects.
 * Implements the DataStore interface — demonstrates Abstraction.
 */
public class TicketDataStore implements DataStore<ParkingTicket> {
    private List<ParkingTicket> tickets;
    private final String filePath;

    public TicketDataStore() {
        this.tickets = new ArrayList<>();
        this.filePath = Constants.DATA_DIR + File.separator + Constants.TICKETS_FILE;
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
    public void add(ParkingTicket ticket) {
        tickets.add(ticket);
        saveAll();
    }

    @Override
    public List<ParkingTicket> getAll() {
        return new ArrayList<>(tickets);
    }

    @Override
    public void update(ParkingTicket updatedTicket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getTicketId().equals(updatedTicket.getTicketId())) {
                tickets.set(i, updatedTicket);
                break;
            }
        }
        saveAll();
    }

    @Override
    public void delete(String ticketId) {
        tickets.removeIf(t -> t.getTicketId().equals(ticketId));
        saveAll();
    }

    /**
     * Finds a ticket by its ID.
     */
    public ParkingTicket findById(String ticketId) {
        for (ParkingTicket t : tickets) {
            if (t.getTicketId().equals(ticketId)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Finds all active tickets (vehicles currently parked).
     */
    public List<ParkingTicket> getActiveTickets() {
        List<ParkingTicket> active = new ArrayList<>();
        for (ParkingTicket t : tickets) {
            if (t.isActive()) {
                active.add(t);
            }
        }
        return active;
    }

    /**
     * Finds all completed (inactive) tickets.
     */
    public List<ParkingTicket> getCompletedTickets() {
        List<ParkingTicket> completed = new ArrayList<>();
        for (ParkingTicket t : tickets) {
            if (!t.isActive()) {
                completed.add(t);
            }
        }
        return completed;
    }

    /**
     * Finds the active ticket for a specific vehicle.
     */
    public ParkingTicket findActiveTicketByVehicle(String vehicleNumber) {
        for (ParkingTicket t : tickets) {
            if (t.isActive() && t.getVehicleNumber().equalsIgnoreCase(vehicleNumber)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (ParkingTicket t : tickets) {
                writer.write(t.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving tickets: " + e.getMessage());
        }
    }

    @Override
    public void loadAll() {
        tickets.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                ParkingTicket ticket = parseTicket(line);
                if (ticket != null) {
                    tickets.add(ticket);
                    try {
                        int idNum = Integer.parseInt(ticket.getTicketId().replace("TKT-", ""));
                        if (idNum > maxId) maxId = idNum;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tickets: " + e.getMessage());
        }
        IDGenerator.setTicketCounter(maxId);
    }

    /**
     * Parses a pipe-delimited string into a ParkingTicket.
     * Format: ticketId|vehicleNumber|slotNumber|entryTime|exitTime|durationHours|fee|isActive
     */
    private ParkingTicket parseTicket(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 8) return null;

            ParkingTicket ticket = new ParkingTicket(parts[0], parts[1], parts[2]);
            ticket.setEntryTime(parts[3]);
            ticket.setExitTime(parts[4].equals("ACTIVE") ? null : parts[4]);
            ticket.setDurationHours(Long.parseLong(parts[5]));
            ticket.setFee(Double.parseDouble(parts[6]));
            ticket.setActive(Boolean.parseBoolean(parts[7]));
            return ticket;
        } catch (Exception e) {
            System.err.println("Error parsing ticket line: " + line + " - " + e.getMessage());
            return null;
        }
    }
}
