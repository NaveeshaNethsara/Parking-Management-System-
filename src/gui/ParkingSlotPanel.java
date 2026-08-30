package gui;

import models.ParkingSlot;
import models.VehicleType;
import services.ParkingSlotManager;
import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Panel for Parking Slot Management — CRUD operations (FR-06, FR-07).
 */
public class ParkingSlotPanel extends JPanel {
    private ParkingSlotManager slotManager;
    private JTable slotTable;
    private DefaultTableModel tableModel;

    public ParkingSlotPanel(ParkingSlotManager slotManager) {
        this.slotManager = slotManager;
        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        add(DialogHelper.createSectionTitle("Parking Slot Management"), BorderLayout.NORTH);

        // Main content card
        JPanel contentPanel = DialogHelper.createCardPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));

        // Top bar with filter and buttons
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        // Filter buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        JButton btnAll = DialogHelper.createStyledButton("All Slots", Constants.ACCENT_BLUE);
        JButton btnAvailable = DialogHelper.createStyledButton("Available", Constants.ACCENT_GREEN);
        JButton btnOccupied = DialogHelper.createStyledButton("Occupied", Constants.ACCENT_RED);
        filterPanel.add(btnAll);
        filterPanel.add(btnAvailable);
        filterPanel.add(btnOccupied);
        topBar.add(filterPanel, BorderLayout.WEST);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);
        JButton btnAdd = DialogHelper.createStyledButton("+ Add Slot", Constants.ACCENT_GREEN);
        JButton btnEdit = DialogHelper.createStyledButton("Edit", Constants.ACCENT_BLUE);
        JButton btnDelete = DialogHelper.createStyledButton("Delete", Constants.ACCENT_RED);
        actionPanel.add(btnAdd);
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        topBar.add(actionPanel, BorderLayout.EAST);

        contentPanel.add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Slot ID", "Slot Number", "Slot Type", "Status", "Assigned Vehicle"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        slotTable = new JTable(tableModel);
        styleTable(slotTable);

        JScrollPane scrollPane = new JScrollPane(slotTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Constants.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Constants.BG_WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // --- Event Handlers ---
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddSlotDialog();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedSlot();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedSlot();
            }
        });

        btnAll.addActionListener(e -> refreshTable());
        btnAvailable.addActionListener(e -> showFilteredSlots(false));
        btnOccupied.addActionListener(e -> showFilteredSlots(true));
    }

    /**
     * Shows dialog to add a new parking slot.
     */
    private void showAddSlotDialog() {
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtSlotNumber = DialogHelper.createFormTextField();
        JComboBox<VehicleType> cmbType = DialogHelper.createFormComboBox(VehicleType.values());

        formPanel.add(DialogHelper.createFormLabel("Slot Number:"));
        formPanel.add(txtSlotNumber);
        formPanel.add(DialogHelper.createFormLabel("Slot Type:"));
        formPanel.add(cmbType);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Parking Slot",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                slotManager.addSlot(txtSlotNumber.getText(), (VehicleType) cmbType.getSelectedItem());
                DialogHelper.showSuccess(this, "Parking slot added successfully!");
                refreshTable();
            } catch (IllegalArgumentException ex) {
                DialogHelper.showError(this, ex.getMessage());
            }
        }
    }

    /**
     * Edits the selected parking slot.
     */
    private void editSelectedSlot() {
        int selectedRow = slotTable.getSelectedRow();
        if (selectedRow < 0) {
            DialogHelper.showWarning(this, "Please select a slot to edit.");
            return;
        }

        String slotId = (String) tableModel.getValueAt(selectedRow, 0);
        ParkingSlot slot = slotManager.findSlotById(slotId);
        if (slot == null) {
            DialogHelper.showError(this, "Slot not found.");
            return;
        }

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField txtSlotNumber = DialogHelper.createFormTextField();
        txtSlotNumber.setText(slot.getSlotNumber());
        JComboBox<VehicleType> cmbType = DialogHelper.createFormComboBox(VehicleType.values());
        cmbType.setSelectedItem(slot.getSlotType());

        formPanel.add(DialogHelper.createFormLabel("Slot Number:"));
        formPanel.add(txtSlotNumber);
        formPanel.add(DialogHelper.createFormLabel("Slot Type:"));
        formPanel.add(cmbType);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Parking Slot",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                slotManager.updateSlot(slotId, txtSlotNumber.getText(), (VehicleType) cmbType.getSelectedItem());
                DialogHelper.showSuccess(this, "Parking slot updated successfully!");
                refreshTable();
            } catch (IllegalArgumentException ex) {
                DialogHelper.showError(this, ex.getMessage());
            }
        }
    }

    /**
     * Deletes the selected parking slot.
     */
    private void deleteSelectedSlot() {
        int selectedRow = slotTable.getSelectedRow();
        if (selectedRow < 0) {
            DialogHelper.showWarning(this, "Please select a slot to delete.");
            return;
        }

        String slotId = (String) tableModel.getValueAt(selectedRow, 0);
        String slotNumber = (String) tableModel.getValueAt(selectedRow, 1);

        if (DialogHelper.showConfirm(this, "Are you sure you want to delete slot '" + slotNumber + "'?")) {
            try {
                slotManager.deleteSlot(slotId);
                DialogHelper.showSuccess(this, "Parking slot deleted successfully!");
                refreshTable();
            } catch (IllegalArgumentException ex) {
                DialogHelper.showError(this, ex.getMessage());
            }
        }
    }

    /**
     * Shows slots filtered by occupancy status.
     */
    private void showFilteredSlots(boolean occupied) {
        tableModel.setRowCount(0);
        List<ParkingSlot> slots = occupied ? slotManager.getOccupiedSlots() : slotManager.getAvailableSlots();
        for (ParkingSlot s : slots) {
            addSlotToTable(s);
        }
    }

    /**
     * Refreshes the table with all slots.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<ParkingSlot> slots = slotManager.getAllSlots();
        for (ParkingSlot s : slots) {
            addSlotToTable(s);
        }
    }

    private void addSlotToTable(ParkingSlot s) {
        String vehicleNum = (s.getAssignedVehicleNumber() != null) ? s.getAssignedVehicleNumber() : "-";
        tableModel.addRow(new Object[]{
                s.getSlotId(),
                s.getSlotNumber(),
                s.getSlotType().getDisplayName(),
                s.getStatusDisplay(),
                vehicleNum
        });
    }

    /**
     * Applies consistent styling to a JTable.
     */
    private void styleTable(JTable table) {
        table.setFont(Constants.FONT_BODY);
        table.setRowHeight(35);
        table.setSelectionBackground(Constants.ACCENT_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Constants.BORDER_COLOR);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(Constants.FONT_HEADER);
        header.setBackground(Constants.TABLE_HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Constants.BG_WHITE : Constants.TABLE_ALT_ROW);
                    // Color code the Status column
                    if (column == 3 && value != null) {
                        if (value.toString().equals("Available")) {
                            setForeground(Constants.ACCENT_GREEN);
                        } else if (value.toString().equals("Occupied")) {
                            setForeground(Constants.ACCENT_RED);
                        }
                    } else {
                        setForeground(Constants.TEXT_DARK);
                    }
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
}
