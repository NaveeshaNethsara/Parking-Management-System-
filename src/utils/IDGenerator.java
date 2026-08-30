package utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for generating unique identifiers.
 * Uses a simple counter-based approach suitable for a standalone application.
 */
public class IDGenerator {
    private static AtomicInteger vehicleCounter = new AtomicInteger(0);
    private static AtomicInteger slotCounter = new AtomicInteger(0);
    private static AtomicInteger ticketCounter = new AtomicInteger(0);
    private static AtomicInteger paymentCounter = new AtomicInteger(0);
    private static AtomicInteger customerCounter = new AtomicInteger(0);

    /**
     * Generates a unique Slot ID.
     * @return A unique slot ID string (e.g., "SLT-001").
     */
    public static String generateSlotId() {
        return String.format("SLT-%03d", slotCounter.incrementAndGet());
    }

    /**
     * Generates a unique Ticket ID.
     * @return A unique ticket ID string (e.g., "TKT-001").
     */
    public static String generateTicketId() {
        return String.format("TKT-%03d", ticketCounter.incrementAndGet());
    }

    /**
     * Generates a unique Payment ID.
     * @return A unique payment ID string (e.g., "PAY-001").
     */
    public static String generatePaymentId() {
        return String.format("PAY-%03d", paymentCounter.incrementAndGet());
    }

    /**
     * Generates a unique Customer ID.
     * @return A unique customer ID string (e.g., "CUS-001").
     */
    public static String generateCustomerId() {
        return String.format("CUS-%03d", customerCounter.incrementAndGet());
    }

    /**
     * Sets the slot counter to continue from existing data.
     * Called when loading data from files.
     * @param value The current max counter value.
     */
    public static void setSlotCounter(int value) {
        slotCounter.set(value);
    }

    /**
     * Sets the ticket counter to continue from existing data.
     * @param value The current max counter value.
     */
    public static void setTicketCounter(int value) {
        ticketCounter.set(value);
    }

    /**
     * Sets the payment counter to continue from existing data.
     * @param value The current max counter value.
     */
    public static void setPaymentCounter(int value) {
        paymentCounter.set(value);
    }

    /**
     * Sets the customer counter to continue from existing data.
     * @param value The current max counter value.
     */
    public static void setCustomerCounter(int value) {
        customerCounter.set(value);
    }

    private IDGenerator() {
        // Prevent instantiation
    }
}
