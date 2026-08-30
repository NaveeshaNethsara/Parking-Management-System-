package gui;

import services.DashboardManager;
import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dashboard panel displaying parking facility summary statistics (FR-17).
 * Shows: Total Slots, Available Slots, Occupied Slots, Parked Vehicles,
 *        Registered Vehicles, Total Records, Total Revenue.
 */
public class DashboardPanel extends JPanel {
    private DashboardManager dashboardManager;

    // Stat labels that get updated
    private JLabel lblTotalSlots;
    private JLabel lblAvailable;
    private JLabel lblOccupied;
    private JLabel lblParkedVehicles;
    private JLabel lblRegisteredVehicles;
    private JLabel lblTotalRecords;
    private JLabel lblTotalRevenue;
    private JLabel lblActiveSessions;

    public DashboardPanel(DashboardManager dashboardManager) {
        this.dashboardManager = dashboardManager;
        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        JLabel title = DialogHelper.createSectionTitle("Dashboard Overview");
        title.setIcon(null);
        add(title, BorderLayout.NORTH);

        // Stats grid
        JPanel gridPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        gridPanel.setOpaque(false);

        // Create stat labels
        lblTotalSlots = new JLabel("0");
        lblAvailable = new JLabel("0");
        lblOccupied = new JLabel("0");
        lblParkedVehicles = new JLabel("0");
        lblRegisteredVehicles = new JLabel("0");
        lblTotalRecords = new JLabel("0");
        lblTotalRevenue = new JLabel("LKR 0.00");
        lblActiveSessions = new JLabel("0");

        gridPanel.add(createStatCard("Total Slots", lblTotalSlots, Constants.ACCENT_BLUE, "SLOTS"));
        gridPanel.add(createStatCard("Available", lblAvailable, Constants.ACCENT_GREEN, "CHECK"));
        gridPanel.add(createStatCard("Occupied", lblOccupied, Constants.ACCENT_RED, "CROSS"));
        gridPanel.add(createStatCard("Active Sessions", lblActiveSessions, Constants.ACCENT_ORANGE, "CLOCK"));

        gridPanel.add(createStatCard("Parked Vehicles", lblParkedVehicles, new Color(0, 150, 136), "VEHICLES"));
        gridPanel.add(createStatCard("Registered Vehicles", lblRegisteredVehicles, new Color(103, 58, 183), "RECORDS"));
        gridPanel.add(createStatCard("Total Records", lblTotalRecords, new Color(233, 30, 99), "RECORDS"));
        gridPanel.add(createStatCard("Total Revenue", lblTotalRevenue, new Color(255, 87, 34), "PAYMENTS"));

        add(gridPanel, BorderLayout.CENTER);

        // Welcome message at bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        JLabel welcome = new JLabel("ParkSmart Automated Facility Management Console");
        welcome.setFont(Constants.FONT_SUBTITLE);
        welcome.setForeground(Constants.TEXT_MUTED);
        bottomPanel.add(welcome);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a stat card with a colored left border, number, and vector icon.
     */
    private JPanel createStatCard(String labelText, JLabel valueLabel, Color accentColor, String iconType) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Constants.BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Constants.BORDER_COLOR, 1),
                        new EmptyBorder(20, 20, 20, 20)
                )
        ));

        // Icon and label panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topPanel.setOpaque(false);
        JLabel iconLabel = new JLabel(IconHelper.getIcon(iconType, 18, accentColor));
        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(Constants.FONT_STAT_LABEL);
        textLabel.setForeground(Constants.TEXT_MUTED);
        topPanel.add(iconLabel);
        topPanel.add(textLabel);

        // Value
        valueLabel.setFont(Constants.FONT_STAT_NUMBER);
        valueLabel.setForeground(accentColor);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        valueLabel.setBorder(new EmptyBorder(10, 5, 0, 0));

        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Refreshes all dashboard statistics.
     * Called each time the panel is shown.
     */
    public void refreshData() {
        lblTotalSlots.setText(String.valueOf(dashboardManager.getTotalSlots()));
        lblAvailable.setText(String.valueOf(dashboardManager.getAvailableSlots()));
        lblOccupied.setText(String.valueOf(dashboardManager.getOccupiedSlots()));
        lblParkedVehicles.setText(String.valueOf(dashboardManager.getCurrentlyParkedVehicles()));
        lblRegisteredVehicles.setText(String.valueOf(dashboardManager.getTotalRegisteredVehicles()));
        lblTotalRecords.setText(String.valueOf(dashboardManager.getTotalParkingRecords()));
        lblTotalRevenue.setText("LKR " + String.format("%.2f", dashboardManager.getTotalRevenue()));
        lblActiveSessions.setText(String.valueOf(dashboardManager.getActiveSessions()));
    }
}
