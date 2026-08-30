package gui;

import models.Vehicle;
import models.VehicleType;
import services.VehicleManager;
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
 * Panel for Vehicle Management — CRUD operations (FR-01 to FR-05).
 * Demonstrates: Event handling (ActionListeners on buttons).
 */
public class VehiclePanel extends JPanel {
    private VehicleManager vehicleManager;
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public VehiclePanel(VehicleManager vehicleManager) {
        this.vehicleManager = vehicleManager;
        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        add(DialogHelper.createSectionTitle("Vehicle Management"), BorderLayout.NORTH);

        // Main content
        JPanel contentPanel = DialogHelper.createCardPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));

        // Top bar: Search + Buttons
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        searchField = DialogHelper.createFormTextField();
        searchField.setPreferredSize(new Dimension(200, 36));
        JButton btnSearch = DialogHelper.createStyledButton("Search", Constants.ACCENT_BLUE);
        btnSearch.setPreferredSize(new Dimension(100, 36));
        JButton btnShowAll = DialogHelper.createStyledButton("Show All", Constants.TEXT_MUTED);
        btnShowAll.setPreferredSize(new Dimension(100, 36));
        searchPanel.add(DialogHelper.createFormLabel("Vehicle No:"));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        topBar.add(searchPanel, BorderLayout.WEST);

        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton btnAdd = DialogHelper.createStyledButton("+ Add Vehicle", Constants.ACCENT_GREEN);
        JButton btnEdit = DialogHelper.createStyledButton("Edit", Constants.ACCENT_BLUE);
        JButton btnDelete = DialogHelper.createStyledButton("Delete", Constants.ACCENT_RED);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        topBar.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Vehicle Number", "Owner Name", "Contact", "Type", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only table
            }
        };
        vehicleTable = new JTable(tableModel);
        styleTable(vehicleTable);

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Constants.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Constants.BG_WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // --- Event Handling (Demonstrates ActionListener) ---
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddVehicleDialog();
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedVehicle();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedVehicle();
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchVehicle();
            }
        });

        btnShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                refreshTable();
            }
        });

        // Enter key to search
        searchField.addActionListener(e -> searchVehicle());
    }

    /**
     * Shows a dialog to add a new vehicle (FR-01).
     */
    private void showAddVehicleDialog() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtVehicleNo = DialogHelper.createFormTextField();
        JTextField txtOwner = DialogHelper.createFormTextField();
        JTextField txtContact = DialogHelper.createFormTextField();
        JComboBox<VehicleType> cmbType = DialogHelper.createFormComboBox(VehicleType.values());

        formPanel.add(DialogHelper.createFormLabel("Vehicle Number:"));
        formPanel.add(txtVehicleNo);
        formPanel.add(DialogHelper.createFormLabel("Owner Name:"));
        formPanel.add(txtOwner);
        formPanel.add(DialogHelper.createFormLabel("Contact:"));
        formPanel.add(txtContact);
        formPanel.add(DialogHelper.createFormLabel("Vehicle Type:"));
        formPanel.add(cmbType);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Add New Vehicle",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                vehicleManager.registerVehicle(
                        txtVehicleNo.getText(),
                        txtOwner.getText(),
                        txtContact.getText(),
                        (VehicleType) cmbType.getSelectedItem()
                );
                DialogHelper.showSuccess(this, "Vehicle registered successfully!");
                refreshTable();
            } catch (IllegalArgumentException ex) {
                DialogHelper.showError(this, ex.getMessage());
            }
        }
    }

    /**
     * Edits the selected vehicle (FR-03).
     */
    private void editSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow < 0) {
            DialogHelper.showWarning(this, "Please select a vehicle to edit.");
            return;
        }

        String vehicleNumber = (String) tableModel.getValueAt(selectedRow, 0);
        Vehicle vehicle = vehicleManager.searchVehicle(vehicleNumber);
        if (vehicle == null) {
            DialogHelper.showError(this, "Vehicle not found.");
            return;
        }

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JLabel lblVehicleNo = new JLabel(vehicle.getVehicleNumber());
        lblVehicleNo.setFont(Constants.FONT_BODY);
        JTextField txtOwner = DialogHelper.createFormTextField();
        txtOwner.setText(vehicle.getOwnerName());
        JTextField txtContact = DialogHelper.createFormTextField();
        txtContact.setText(vehicle.getContactInfo());

        formPanel.add(DialogHelper.createFormLabel("Vehicle Number:"));
        formPanel.add(lblVehicleNo);
        formPanel.add(DialogHelper.createFormLabel("Owner Name:"));
        formPanel.add(txtOwner);
        formPanel.add(DialogHelper.createFormLabel("Contact:"));
        formPanel.add(txtContact);

        int result = JOptionPane.showConfirmDialog(this, formPanel, "Edit Vehicle",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                vehicleManager.updateVehicle(vehicleNumber, txtOwner.getText(), txtContact.getText());
                DialogHelper.showSuccess(this, "Vehicle updated successfully!");
                refreshTable();
            } catch (IllegalArgumentException ex) {
                DialogHelper.showError(this, ex.getMessage());
            }
        }
    }

    /**
     * Deletes the selected vehicle (FR-04).
     */
    private void deleteSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow < 0) {
            DialogHelper.showWarning(this, "Please select a vehicle to delete.");
            return;
        }

        String vehicleNumber = (String) tableModel.getValueAt(selectedRow, 0);
        if (DialogHelper.showConfirm(this, "Are you sure you want to delete vehicle '" + vehicleNumber + "'?")) {
            try {
                vehicleManager.deleteVehicle(vehicleNumber);
                DialogHelper.showSuccess(this, "Vehicle deleted successfully!");
                refreshTable();
            } catch (IllegalArgumentException ex) {
                DialogHelper.showError(this, ex.getMessage());
            }
        }
    }

    /**
     * Searches for a vehicle (FR-05).
     */
    private void searchVehicle() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            refreshTable();
            return;
        }

        Vehicle vehicle = vehicleManager.searchVehicle(searchText);
        tableModel.setRowCount(0);
        if (vehicle != null) {
            addVehicleToTable(vehicle);
        } else {
            DialogHelper.showWarning(this, "Vehicle '" + searchText + "' not found.");
        }
    }

    /**
     * Refreshes the vehicle table with all data.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Vehicle> vehicles = vehicleManager.getAllVehicles();
        for (Vehicle v : vehicles) {
            addVehicleToTable(v);
        }
    }

    private void addVehicleToTable(Vehicle v) {
        tableModel.addRow(new Object[]{
                v.getVehicleNumber(),
                v.getOwnerName(),
                v.getContactInfo(),
                v.getVehicleType().getDisplayName(),
                v.isParked() ? "Parked" : "Not Parked"
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

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(Constants.FONT_HEADER);
        header.setBackground(Constants.TABLE_HEADER_BG);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Constants.BG_WHITE : Constants.TABLE_ALT_ROW);
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
}
