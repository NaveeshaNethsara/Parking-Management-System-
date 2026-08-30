package gui;

import models.ParkingTicket;
import services.ParkingManager;
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
 * Panel for viewing and filtering parking records / tickets (FR-16).
 */
public class ParkingRecordsPanel extends JPanel {
    private ParkingManager parkingManager;
    private JTable recordsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ParkingRecordsPanel(ParkingManager parkingManager) {
        this.parkingManager = parkingManager;
        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        add(DialogHelper.createSectionTitle("Parking Records"), BorderLayout.NORTH);

        JPanel contentPanel = DialogHelper.createCardPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        searchField = DialogHelper.createFormTextField();
        searchField.setPreferredSize(new Dimension(200, 36));
        JButton btnSearch = DialogHelper.createStyledButton("Search", Constants.ACCENT_BLUE);
        btnSearch.setPreferredSize(new Dimension(100, 36));
        searchPanel.add(DialogHelper.createFormLabel("Ticket/Vehicle:"));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        topBar.add(searchPanel, BorderLayout.WEST);

        // Filter Buttons
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);
        JButton btnAll = DialogHelper.createStyledButton("All Records", Constants.ACCENT_BLUE);
        JButton btnActive = DialogHelper.createStyledButton("Active Only", Constants.ACCENT_ORANGE);
        JButton btnCompleted = DialogHelper.createStyledButton("Completed Only", Constants.ACCENT_GREEN);
        filterPanel.add(btnAll);
        filterPanel.add(btnActive);
        filterPanel.add(btnCompleted);
        topBar.add(filterPanel, BorderLayout.EAST);

        contentPanel.add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"Ticket ID", "Vehicle No", "Slot No", "Entry Time", "Exit Time", "Duration (hrs)", "Fee (LKR)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recordsTable = new JTable(tableModel);
        styleTable(recordsTable);

        JScrollPane scrollPane = new JScrollPane(recordsTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Constants.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Constants.BG_WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Listeners
        btnSearch.addActionListener(e -> searchRecords());
        searchField.addActionListener(e -> searchRecords());

        btnAll.addActionListener(e -> {
            searchField.setText("");
            refreshTable();
        });

        btnActive.addActionListener(e -> {
            tableModel.setRowCount(0);
            List<ParkingTicket> active = parkingManager.getActiveTickets();
            for (ParkingTicket t : active) {
                addTicketToTable(t);
            }
        });

        btnCompleted.addActionListener(e -> {
            tableModel.setRowCount(0);
            List<ParkingTicket> completed = parkingManager.getCompletedTickets();
            for (ParkingTicket t : completed) {
                addTicketToTable(t);
            }
        });
    }

    private void searchRecords() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            refreshTable();
            return;
        }

        tableModel.setRowCount(0);
        List<ParkingTicket> all = parkingManager.getAllTickets();
        for (ParkingTicket t : all) {
            if (t.getTicketId().toLowerCase().contains(query) ||
                t.getVehicleNumber().toLowerCase().contains(query) ||
                t.getSlotNumber().toLowerCase().contains(query)) {
                addTicketToTable(t);
            }
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<ParkingTicket> tickets = parkingManager.getAllTickets();
        for (ParkingTicket t : tickets) {
            addTicketToTable(t);
        }
    }

    private void addTicketToTable(ParkingTicket t) {
        String exitTime = (t.getExitTime() != null) ? t.getExitTime() : "In Parking";
        String duration = t.isActive() ? "Ongoing" : String.valueOf(t.getDurationHours());
        String fee = t.isActive() ? "Pending" : String.format("%.2f", t.getFee());
        String status = t.isActive() ? "ACTIVE" : "COMPLETED";

        tableModel.addRow(new Object[]{
                t.getTicketId(),
                t.getVehicleNumber(),
                t.getSlotNumber(),
                t.getEntryTime(),
                exitTime,
                duration,
                fee,
                status
        });
    }

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
                    if (column == 7 && value != null) {
                        if (value.toString().equals("ACTIVE")) {
                            setForeground(Constants.ACCENT_ORANGE);
                        } else {
                            setForeground(Constants.ACCENT_GREEN);
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
