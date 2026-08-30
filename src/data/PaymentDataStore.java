package data;

import models.Payment;
import utils.Constants;
import utils.IDGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based data store for Payment objects.
 * Implements the DataStore interface — demonstrates Abstraction.
 */
public class PaymentDataStore implements DataStore<Payment> {
    private List<Payment> payments;
    private final String filePath;

    public PaymentDataStore() {
        this.payments = new ArrayList<>();
        this.filePath = Constants.DATA_DIR + File.separator + Constants.PAYMENTS_FILE;
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
    public void add(Payment payment) {
        payments.add(payment);
        saveAll();
    }

    @Override
    public List<Payment> getAll() {
        return new ArrayList<>(payments);
    }

    @Override
    public void update(Payment updatedPayment) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getPaymentId().equals(updatedPayment.getPaymentId())) {
                payments.set(i, updatedPayment);
                break;
            }
        }
        saveAll();
    }

    @Override
    public void delete(String paymentId) {
        payments.removeIf(p -> p.getPaymentId().equals(paymentId));
        saveAll();
    }

    /**
     * Finds a payment by its ID.
     */
    public Payment findById(String paymentId) {
        for (Payment p : payments) {
            if (p.getPaymentId().equals(paymentId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Finds a payment associated with a specific ticket.
     */
    public Payment findByTicketId(String ticketId) {
        for (Payment p : payments) {
            if (p.getTicketId().equals(ticketId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Calculates the total revenue from all payments.
     */
    public double getTotalRevenue() {
        double total = 0;
        for (Payment p : payments) {
            total += p.getAmount();
        }
        return total;
    }

    @Override
    public void saveAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Payment p : payments) {
                writer.write(p.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving payments: " + e.getMessage());
        }
    }

    @Override
    public void loadAll() {
        payments.clear();
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Payment payment = parsePayment(line);
                if (payment != null) {
                    payments.add(payment);
                    try {
                        int idNum = Integer.parseInt(payment.getPaymentId().replace("PAY-", ""));
                        if (idNum > maxId) maxId = idNum;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
        IDGenerator.setPaymentCounter(maxId);
    }

    /**
     * Parses a pipe-delimited string into a Payment.
     * Format: paymentId|ticketId|amount|paymentMethod|paymentDate
     */
    private Payment parsePayment(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length < 5) return null;

            Payment payment = new Payment(parts[0], parts[1], Double.parseDouble(parts[2]), parts[3]);
            payment.setPaymentDate(parts[4]);
            return payment;
        } catch (Exception e) {
            System.err.println("Error parsing payment line: " + line + " - " + e.getMessage());
            return null;
        }
    }
}
