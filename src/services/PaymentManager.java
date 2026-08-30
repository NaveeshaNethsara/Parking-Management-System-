package services;

import data.PaymentDataStore;
import models.Payment;
import utils.IDGenerator;
import utils.Validator;

import java.util.List;

/**
 * Service class for managing payment operations (FR-14).
 */
public class PaymentManager {
    private PaymentDataStore paymentDataStore;

    public PaymentManager(PaymentDataStore paymentDataStore) {
        this.paymentDataStore = paymentDataStore;
    }

    /**
     * Records a payment for a completed parking session (FR-14).
     * @param ticketId      The associated ticket ID.
     * @param amount        The payment amount.
     * @param paymentMethod The payment method (Cash, Card, etc.).
     * @return The created Payment object.
     */
    public Payment recordPayment(String ticketId, double amount, String paymentMethod)
            throws IllegalArgumentException {

        if (!Validator.isNotEmpty(ticketId)) {
            throw new IllegalArgumentException("Ticket ID cannot be empty.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }
        if (!Validator.isNotEmpty(paymentMethod)) {
            throw new IllegalArgumentException("Payment method must be selected.");
        }

        // Check if payment already exists for this ticket
        if (paymentDataStore.findByTicketId(ticketId) != null) {
            throw new IllegalArgumentException("Payment already recorded for ticket '" + ticketId + "'.");
        }

        String paymentId = IDGenerator.generatePaymentId();
        Payment payment = new Payment(paymentId, ticketId, amount, paymentMethod);
        paymentDataStore.add(payment);
        return payment;
    }

    /**
     * Gets all payment records.
     */
    public List<Payment> getAllPayments() {
        return paymentDataStore.getAll();
    }

    /**
     * Finds a payment by ticket ID.
     */
    public Payment findPaymentByTicketId(String ticketId) {
        return paymentDataStore.findByTicketId(ticketId);
    }

    /**
     * Gets total revenue from all payments.
     */
    public double getTotalRevenue() {
        return paymentDataStore.getTotalRevenue();
    }

    /**
     * Gets the payment data store.
     */
    public PaymentDataStore getDataStore() {
        return paymentDataStore;
    }
}
