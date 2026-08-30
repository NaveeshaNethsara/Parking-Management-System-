package gui;

import models.ParkingTicket;
import models.Payment;
import models.Vehicle;
import services.ParkingManager;
import services.PaymentManager;
import services.VehicleManager;
import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for processing Vehicle Parking Exit and Payment (FR-11 to FR-15).
 * Calculates parking duration and fee using polymorphism, processes payment,
 * and releases the assigned parking slot.
 */
public class ParkingExitPanel extends JPanel {
    private ParkingManager parkingManager;
    private PaymentManager paymentManager;
    private VehicleManager vehicleManager;

    private JComboBox<String> cmbActiveTickets;
    private JLabel lblVehicleNoVal;
    private JLabel lblOwnerVal;
    private JLabel lblSlotVal;
    private JLabel lblEntryTimeVal;
    private JLabel lblDurationVal;
    private JLabel lblFeeVal;
    private JComboBox<String> cmbPaymentMethod;
    private JTextArea txtReceipt;

    private ParkingTicket currentProcessedTicket;

    public ParkingExitPanel(ParkingManager parkingManager, PaymentManager paymentManager, VehicleManager vehicleManager) {
        this.parkingManager = parkingManager;
        this.paymentManager = paymentManager;
        this.vehicleManager = vehicleManager;

        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        add(DialogHelper.createSectionTitle("Vehicle Parking Exit & Payment"), BorderLayout.NORTH);

        JPanel mainGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        mainGrid.setOpaque(false);

        // Left Panel: Exit Form Card
        JPanel formCard = DialogHelper.createCardPanel();
        formCard.setLayout(new BorderLayout(0, 15));

        JLabel formHeader = new JLabel("Process Vehicle Exit");
        formHeader.setFont(Constants.FONT_SUBTITLE);
        formHeader.setForeground(Constants.PRIMARY_DARK);
        formCard.add(formHeader, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Select Active Ticket
        gbc.gridx = 0; gbc.gridy = 0;
        formFields.add(DialogHelper.createFormLabel("Active Ticket:"), gbc);
        gbc.gridx = 1;
        cmbActiveTickets = new JComboBox<>();
        cmbActiveTickets.setFont(Constants.FONT_BODY);
        formFields.add(cmbActiveTickets, gbc);

        // Details
        gbc.gridx = 0; gbc.gridy = 1;
        formFields.add(DialogHelper.createFormLabel("Vehicle No:"), gbc);
        gbc.gridx = 1;
        lblVehicleNoVal = new JLabel("-");
        lblVehicleNoVal.setFont(Constants.FONT_BODY);
        formFields.add(lblVehicleNoVal, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formFields.add(DialogHelper.createFormLabel("Owner Name:"), gbc);
        gbc.gridx = 1;
        lblOwnerVal = new JLabel("-");
        lblOwnerVal.setFont(Constants.FONT_BODY);
        formFields.add(lblOwnerVal, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formFields.add(DialogHelper.createFormLabel("Slot Number:"), gbc);
        gbc.gridx = 1;
        lblSlotVal = new JLabel("-");
        lblSlotVal.setFont(Constants.FONT_BODY);
        formFields.add(lblSlotVal, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formFields.add(DialogHelper.createFormLabel("Entry Time:"), gbc);
        gbc.gridx = 1;
        lblEntryTimeVal = new JLabel("-");
        lblEntryTimeVal.setFont(Constants.FONT_BODY);
        formFields.add(lblEntryTimeVal, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formFields.add(DialogHelper.createFormLabel("Estimated Duration:"), gbc);
        gbc.gridx = 1;
        lblDurationVal = new JLabel("-");
        lblDurationVal.setFont(Constants.FONT_BODY);
        formFields.add(lblDurationVal, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formFields.add(DialogHelper.createFormLabel("Calculated Fee:"), gbc);
        gbc.gridx = 1;
        lblFeeVal = new JLabel("-");
        lblFeeVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFeeVal.setForeground(Constants.ACCENT_BLUE);
        formFields.add(lblFeeVal, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formFields.add(DialogHelper.createFormLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cmbPaymentMethod = DialogHelper.createFormComboBox(Constants.PAYMENT_METHODS);
        formFields.add(cmbPaymentMethod, gbc);

        formCard.add(formFields, BorderLayout.CENTER);

        // Action Button
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        JButton btnProcessExit = DialogHelper.createStyledButton("Process Exit & Pay", Constants.ACCENT_GREEN);
        btnProcessExit.setPreferredSize(new Dimension(180, 40));
        actionPanel.add(btnProcessExit);
        formCard.add(actionPanel, BorderLayout.SOUTH);

        // Right Panel: Receipt Preview Card
        JPanel previewCard = DialogHelper.createCardPanel();
        previewCard.setLayout(new BorderLayout(0, 15));

        JLabel previewHeader = new JLabel("Payment Receipt");
        previewHeader.setFont(Constants.FONT_SUBTITLE);
        previewHeader.setForeground(Constants.PRIMARY_DARK);
        previewCard.add(previewHeader, BorderLayout.NORTH);

        txtReceipt = new JTextArea();
        txtReceipt.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtReceipt.setEditable(false);
        txtReceipt.setBackground(new Color(250, 250, 250));
        txtReceipt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_COLOR),
                new EmptyBorder(15, 15, 15, 15)
        ));
        txtReceipt.setText("=== NO PAYMENT PROCESSED ===\n\nSelect an active ticket and click 'Process Exit & Pay' to finalize parking session.");
        previewCard.add(new JScrollPane(txtReceipt), BorderLayout.CENTER);

        mainGrid.add(formCard);
        mainGrid.add(previewCard);
        add(mainGrid, BorderLayout.CENTER);

        // Listeners
        cmbActiveTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSelectedTicketDetails();
            }
        });

        btnProcessExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processExitAndPayment();
            }
        });
    }

    private void updateSelectedTicketDetails() {
        String selected = (String) cmbActiveTickets.getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            String ticketId = selected.split(" ")[0];
            ParkingTicket ticket = parkingManager.findTicketById(ticketId);
            if (ticket != null) {
                lblVehicleNoVal.setText(ticket.getVehicleNumber());
                lblSlotVal.setText(ticket.getSlotNumber());
                lblEntryTimeVal.setText(ticket.getEntryTime());

                Vehicle v = vehicleManager.searchVehicle(ticket.getVehicleNumber());
                if (v != null) {
                    lblOwnerVal.setText(v.getOwnerName() + " (" + v.getVehicleType().getDisplayName() + ")");
                }

                lblDurationVal.setText("Calculated at exit (Min 1 hr)");
                if (v != null) {
                    lblFeeVal.setText("Rate: LKR " + String.format("%.2f", v.calculateParkingFee(1)) + "/hr");
                }
                return;
            }
        }
        lblVehicleNoVal.setText("-");
        lblOwnerVal.setText("-");
        lblSlotVal.setText("-");
        lblEntryTimeVal.setText("-");
        lblDurationVal.setText("-");
        lblFeeVal.setText("-");
    }

    private void processExitAndPayment() {
        String selected = (String) cmbActiveTickets.getSelectedItem();
        if (selected == null || selected.isEmpty()) {
            DialogHelper.showWarning(this, "Please select an active parking ticket.");
            return;
        }

        String ticketId = selected.split(" ")[0];
        String paymentMethod = (String) cmbPaymentMethod.getSelectedItem();

        try {
            // Process exit & calculate fee (Polymorphism)
            ParkingTicket ticket = parkingManager.recordExit(ticketId);
            currentProcessedTicket = ticket;

            // Process payment
            Payment payment = paymentManager.recordPayment(ticketId, ticket.getFee(), paymentMethod);

            Vehicle v = vehicleManager.searchVehicle(ticket.getVehicleNumber());
            String vehicleTypeStr = (v != null) ? v.getVehicleType().getDisplayName() : "Unknown";
            String ownerNameStr = (v != null) ? v.getOwnerName() : "Unknown";

            DialogHelper.showSuccess(this, "Exit & Payment Completed Successfully!\n" +
                    "Ticket: " + ticket.getTicketId() + "\n" +
                    "Duration: " + ticket.getDurationHours() + " hr(s)\n" +
                    "Total Fee: LKR " + String.format("%.2f", ticket.getFee()) + "\n" +
                    "Slot " + ticket.getSlotNumber() + " has been released.");

            // Generate receipt text
            StringBuilder sb = new StringBuilder();
            sb.append("================================================\n");
            sb.append("            PARKING PAYMENT RECEIPT             \n");
            sb.append("================================================\n");
            sb.append(String.format(" Payment ID     : %s\n", payment.getPaymentId()));
            sb.append(String.format(" Ticket ID      : %s\n", ticket.getTicketId()));
            sb.append(String.format(" Vehicle Number : %s\n", ticket.getVehicleNumber()));
            sb.append(String.format(" Vehicle Type   : %s\n", vehicleTypeStr));
            sb.append(String.format(" Owner Name     : %s\n", ownerNameStr));
            sb.append(String.format(" Parking Slot   : %s\n", ticket.getSlotNumber()));
            sb.append("------------------------------------------------\n");
            sb.append(String.format(" Entry Time     : %s\n", ticket.getEntryTime()));
            sb.append(String.format(" Exit Time      : %s\n", ticket.getExitTime()));
            sb.append(String.format(" Total Duration : %d Hour(s)\n", ticket.getDurationHours()));
            sb.append("------------------------------------------------\n");
            sb.append(String.format(" TOTAL AMOUNT   : LKR %.2f\n", ticket.getFee()));
            sb.append(String.format(" Payment Method : %s\n", payment.getPaymentMethod()));
            sb.append(String.format(" Payment Date   : %s\n", payment.getPaymentDate()));
            sb.append(String.format(" Slot Status    : RELEASED & AVAILABLE\n"));
            sb.append("================================================\n");
            sb.append("           Thank you for parking with us!       \n");
            sb.append("================================================\n");

            txtReceipt.setText(sb.toString());

            refreshData();
        } catch (IllegalArgumentException ex) {
            DialogHelper.showError(this, ex.getMessage());
        }
    }

    /**
     * Refreshes active ticket dropdown.
     */
    public void refreshData() {
        cmbActiveTickets.removeAllItems();
        List<ParkingTicket> activeTickets = parkingManager.getActiveTickets();
        for (ParkingTicket t : activeTickets) {
            cmbActiveTickets.addItem(t.getTicketId() + " - " + t.getVehicleNumber() + " (Slot: " + t.getSlotNumber() + ")");
        }
        updateSelectedTicketDetails();
    }
}
