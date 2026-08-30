package gui;

import models.ParkingSlot;
import models.ParkingTicket;
import models.Vehicle;
import services.ParkingManager;
import services.ParkingSlotManager;
import services.VehicleManager;
import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for processing Vehicle Parking Entry (FR-08, FR-09, FR-10).
 * Dynamically filters available slots to match the selected vehicle's type
 * and enforces slot type compatibility.
 */
public class ParkingEntryPanel extends JPanel {
    private ParkingManager parkingManager;
    private VehicleManager vehicleManager;
    private ParkingSlotManager slotManager;

    private JComboBox<String> cmbVehicles;
    private JComboBox<String> cmbSlots;
    private JLabel lblOwnerValue;
    private JLabel lblTypeValue;
    private JLabel lblContactValue;
    private JLabel lblSlotTypeValue;
    private JButton btnGenerate;
    private JTextArea txtTicketPreview;

    public ParkingEntryPanel(ParkingManager parkingManager, VehicleManager vehicleManager, ParkingSlotManager slotManager) {
        this.parkingManager = parkingManager;
        this.vehicleManager = vehicleManager;
        this.slotManager = slotManager;

        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        add(DialogHelper.createSectionTitle("Vehicle Parking Entry"), BorderLayout.NORTH);

        JPanel mainGrid = new JPanel(new GridLayout(1, 2, 20, 0));
        mainGrid.setOpaque(false);

        // Left Panel: Entry Form Card
        JPanel formCard = DialogHelper.createCardPanel();
        formCard.setLayout(new BorderLayout(0, 15));

        JLabel formHeader = new JLabel("Create New Parking Ticket");
        formHeader.setFont(Constants.FONT_SUBTITLE);
        formHeader.setForeground(Constants.PRIMARY_DARK);
        formCard.add(formHeader, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Vehicle Selection
        gbc.gridx = 0; gbc.gridy = 0;
        formFields.add(DialogHelper.createFormLabel("Select Vehicle:"), gbc);
        gbc.gridx = 1;
        cmbVehicles = new JComboBox<>();
        cmbVehicles.setFont(Constants.FONT_BODY);
        formFields.add(cmbVehicles, gbc);

        // Vehicle Info Details
        gbc.gridx = 0; gbc.gridy = 1;
        formFields.add(DialogHelper.createFormLabel("Owner Name:"), gbc);
        gbc.gridx = 1;
        lblOwnerValue = new JLabel("-");
        lblOwnerValue.setFont(Constants.FONT_BODY);
        formFields.add(lblOwnerValue, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formFields.add(DialogHelper.createFormLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1;
        lblTypeValue = new JLabel("-");
        lblTypeValue.setFont(Constants.FONT_BODY);
        formFields.add(lblTypeValue, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formFields.add(DialogHelper.createFormLabel("Contact Info:"), gbc);
        gbc.gridx = 1;
        lblContactValue = new JLabel("-");
        lblContactValue.setFont(Constants.FONT_BODY);
        formFields.add(lblContactValue, gbc);

        // Slot Selection
        gbc.gridx = 0; gbc.gridy = 4;
        formFields.add(DialogHelper.createFormLabel("Matching Slot:"), gbc);
        gbc.gridx = 1;
        cmbSlots = new JComboBox<>();
        cmbSlots.setFont(Constants.FONT_BODY);
        formFields.add(cmbSlots, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formFields.add(DialogHelper.createFormLabel("Slot Type:"), gbc);
        gbc.gridx = 1;
        lblSlotTypeValue = new JLabel("-");
        lblSlotTypeValue.setFont(Constants.FONT_BODY);
        formFields.add(lblSlotTypeValue, gbc);

        formCard.add(formFields, BorderLayout.CENTER);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        btnGenerate = DialogHelper.createStyledButton("Generate Ticket", Constants.ACCENT_GREEN);
        btnGenerate.setPreferredSize(new Dimension(160, 40));
        actionPanel.add(btnGenerate);
        formCard.add(actionPanel, BorderLayout.SOUTH);

        // Right Panel: Ticket Preview Card
        JPanel previewCard = DialogHelper.createCardPanel();
        previewCard.setLayout(new BorderLayout(0, 15));

        JLabel previewHeader = new JLabel("Generated Ticket Preview");
        previewHeader.setFont(Constants.FONT_SUBTITLE);
        previewHeader.setForeground(Constants.PRIMARY_DARK);
        previewCard.add(previewHeader, BorderLayout.NORTH);

        txtTicketPreview = new JTextArea();
        txtTicketPreview.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtTicketPreview.setEditable(false);
        txtTicketPreview.setBackground(new Color(250, 250, 250));
        txtTicketPreview.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_COLOR),
                new EmptyBorder(15, 15, 15, 15)
        ));
        txtTicketPreview.setText("=== NO TICKET GENERATED ===\n\nPlease select a vehicle and a matching available slot, then click 'Generate Ticket'.");
        previewCard.add(new JScrollPane(txtTicketPreview), BorderLayout.CENTER);

        mainGrid.add(formCard);
        mainGrid.add(previewCard);
        add(mainGrid, BorderLayout.CENTER);

        // --- Listeners ---
        cmbVehicles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVehicleDetailsAndSlots();
            }
        });

        cmbSlots.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSlotDetails();
            }
        });

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processEntry();
            }
        });
    }

    private void updateVehicleDetailsAndSlots() {
        String selected = (String) cmbVehicles.getSelectedItem();
        if (selected != null && !selected.isEmpty()) {
            String vehicleNo = selected.split(" ")[0];
            Vehicle v = vehicleManager.searchVehicle(vehicleNo);
            if (v != null) {
                lblOwnerValue.setText(v.getOwnerName());
                lblTypeValue.setText(v.getVehicleType().getDisplayName());
                lblContactValue.setText(v.getContactInfo());

                // Filter available slots specifically matching this vehicle's type
                populateAvailableSlotsForType(v);
                return;
            }
        }
        lblOwnerValue.setText("-");
        lblTypeValue.setText("-");
        lblContactValue.setText("-");
        cmbSlots.removeAllItems();
        lblSlotTypeValue.setText("-");
    }

    private void populateAvailableSlotsForType(Vehicle v) {
        cmbSlots.removeAllItems();
        List<ParkingSlot> matchingSlots = slotManager.getAvailableSlotsByType(v.getVehicleType());

        if (matchingSlots.isEmpty()) {
            cmbSlots.addItem("No available " + v.getVehicleType().getDisplayName() + " slots");
            btnGenerate.setEnabled(false);
            lblSlotTypeValue.setText("Unavailable");
        } else {
            for (ParkingSlot s : matchingSlots) {
                cmbSlots.addItem(s.getSlotNumber() + " - " + s.getSlotType().getDisplayName());
            }
            btnGenerate.setEnabled(true);
        }
        updateSlotDetails();
    }

    private void updateSlotDetails() {
        String selected = (String) cmbSlots.getSelectedItem();
        if (selected != null && !selected.isEmpty() && !selected.startsWith("No available")) {
            String slotNumber = selected.split(" ")[0];
            ParkingSlot s = slotManager.findSlotByNumber(slotNumber);
            if (s != null) {
                lblSlotTypeValue.setText(s.getSlotType().getDisplayName());
                return;
            }
        }
        lblSlotTypeValue.setText("-");
    }

    private void processEntry() {
        String selectedVehicle = (String) cmbVehicles.getSelectedItem();
        String selectedSlot = (String) cmbSlots.getSelectedItem();

        if (selectedVehicle == null || selectedVehicle.isEmpty()) {
            DialogHelper.showWarning(this, "Please select an unparked registered vehicle.");
            return;
        }

        if (selectedSlot == null || selectedSlot.isEmpty() || selectedSlot.startsWith("No available")) {
            DialogHelper.showWarning(this, "No matching available parking slot selected.");
            return;
        }

        String vehicleNo = selectedVehicle.split(" ")[0];
        String slotNumber = selectedSlot.split(" ")[0];
        ParkingSlot slot = slotManager.findSlotByNumber(slotNumber);

        if (slot == null) {
            DialogHelper.showError(this, "Selected slot not found.");
            return;
        }

        try {
            ParkingTicket ticket = parkingManager.recordEntry(vehicleNo, slot.getSlotId());
            DialogHelper.showSuccess(this, "Parking Ticket Generated Successfully!\nTicket ID: " + ticket.getTicketId());

            // Build preview text
            StringBuilder sb = new StringBuilder();
            sb.append("================================================\n");
            sb.append("             PARKING ENTRY TICKET               \n");
            sb.append("================================================\n");
            sb.append(String.format(" Ticket ID      : %s\n", ticket.getTicketId()));
            sb.append(String.format(" Vehicle Number : %s\n", ticket.getVehicleNumber()));
            sb.append(String.format(" Vehicle Type   : %s\n", lblTypeValue.getText()));
            sb.append(String.format(" Owner Name     : %s\n", lblOwnerValue.getText()));
            sb.append(String.format(" Assigned Slot  : %s (%s)\n", ticket.getSlotNumber(), lblSlotTypeValue.getText()));
            sb.append(String.format(" Entry Time     : %s\n", ticket.getEntryTime()));
            sb.append("------------------------------------------------\n");
            sb.append(" Status         : ACTIVE (Parked)\n");
            sb.append(" Please keep this ticket safe until exit.\n");
            sb.append("================================================\n");
            txtTicketPreview.setText(sb.toString());

            refreshData();
        } catch (IllegalArgumentException ex) {
            DialogHelper.showError(this, ex.getMessage());
        }
    }

    /**
     * Refreshes dropdowns with unparked registered vehicles.
     */
    public void refreshData() {
        cmbVehicles.removeAllItems();
        List<Vehicle> allVehicles = vehicleManager.getAllVehicles();
        for (Vehicle v : allVehicles) {
            if (!v.isParked()) {
                cmbVehicles.addItem(v.getVehicleNumber() + " (" + v.getOwnerName() + ")");
            }
        }
        updateVehicleDetailsAndSlots();
    }
}
