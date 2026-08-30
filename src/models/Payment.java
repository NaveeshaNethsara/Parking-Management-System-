package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a payment record for a completed parking session.
 * Demonstrates: Association (linked to ParkingTicket),
 *               Encapsulation (private fields + getters/setters).
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Private fields — Encapsulation
    private String paymentId;
    private String ticketId;
    private double amount;
    private String paymentMethod;  // e.g., "Cash", "Card"
    private String paymentDate;

    /**
     * Constructor for Payment.
     * @param paymentId     Unique payment identifier.
     * @param ticketId      The associated parking ticket ID.
     * @param amount        The payment amount.
     * @param paymentMethod The method of payment (Cash, Card).
     */
    public Payment(String paymentId, String ticketId, double amount, String paymentMethod) {
        this.paymentId = paymentId;
        this.ticketId = ticketId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = LocalDateTime.now().format(FORMATTER);
    }

    // --- Getters and Setters — Encapsulation ---

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "Payment [" + paymentId + "] Amount: LKR " + String.format("%.2f", amount)
                + " Method: " + paymentMethod;
    }

    /**
     * Converts to a delimited string for file storage.
     */
    public String toFileString() {
        return paymentId + "|" + ticketId + "|" + amount + "|" + paymentMethod + "|" + paymentDate;
    }
}
