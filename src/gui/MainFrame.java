package gui;

import services.*;
import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Main application window for the Parking Management System.
 * Features a modern sidebar navigation with CardLayout content switching.
 */
public class MainFrame extends JFrame {
    // Services
    private VehicleManager vehicleManager;
    private ParkingSlotManager slotManager;
    private ParkingManager parkingManager;
    private PaymentManager paymentManager;
    private DashboardManager dashboardManager;

    // GUI Components
    private JPanel sidebarPanel;
    private JPanel contentContainer;
    private CardLayout cardLayout;
    private JLabel lblClock;

    // Panels
    private DashboardPanel dashboardPanel;
    private VehiclePanel vehiclePanel;
    private ParkingSlotPanel slotPanel;
    private ParkingEntryPanel entryPanel;
    private ParkingExitPanel exitPanel;
    private ParkingRecordsPanel recordsPanel;
    private PaymentPanel paymentPanel;

    // Navigation tracking
    private Map<String, JButton> navButtons = new HashMap<>();
    private String currentActiveCard = "DASHBOARD";

    public MainFrame(VehicleManager vehicleManager, ParkingSlotManager slotManager,
                     ParkingManager parkingManager, PaymentManager paymentManager,
                     DashboardManager dashboardManager) {
        this.vehicleManager = vehicleManager;
        this.slotManager = slotManager;
        this.parkingManager = parkingManager;
        this.paymentManager = paymentManager;
        this.dashboardManager = dashboardManager;

        initUI();
    }

    private void initUI() {
        setTitle("ParkSmart — Parking Management System");
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Layout
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.setBackground(Constants.BG_LIGHT);

        // 1. Sidebar (West)
        createSidebar();
        rootPanel.add(sidebarPanel, BorderLayout.WEST);

        // 2. Content Area (Center with CardLayout)
        createContentPanels();
        rootPanel.add(contentContainer, BorderLayout.CENTER);

        // 3. Status Bar (South)
        createStatusBar(rootPanel);

        setContentPane(rootPanel);

        // Show Dashboard initially
        switchCard("DASHBOARD");
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(Constants.SIDEBAR_WIDTH, 0));
        sidebarPanel.setBackground(Constants.PRIMARY_DARK);
        sidebarPanel.setLayout(new BorderLayout());

        // Header / Logo Area
        JPanel brandPanel = new JPanel();
        brandPanel.setBackground(Constants.PRIMARY_DARK);
        brandPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 20));

        JLabel lblLogoIcon = new JLabel(IconHelper.getIcon("LOGO_P", 32, Constants.ACCENT_BLUE));
        JPanel brandTextPanel = new JPanel();
        brandTextPanel.setOpaque(false);
        brandTextPanel.setLayout(new BoxLayout(brandTextPanel, BoxLayout.Y_AXIS));

        JLabel lblLogo = new JLabel("PARK SMART");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);

        JLabel lblSubLogo = new JLabel("Management System");
        lblSubLogo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSubLogo.setForeground(new Color(180, 190, 210));

        brandTextPanel.add(lblLogo);
        brandTextPanel.add(Box.createVerticalStrut(2));
        brandTextPanel.add(lblSubLogo);

        brandPanel.add(lblLogoIcon);
        brandPanel.add(brandTextPanel);

        sidebarPanel.add(brandPanel, BorderLayout.NORTH);

        // Navigation Buttons Area
        JPanel navPanel = new JPanel();
        navPanel.setBackground(Constants.PRIMARY_DARK);
        navPanel.setLayout(new GridLayout(7, 1, 0, 8));
        navPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        navPanel.add(createNavButton("DASHBOARD", "Dashboard", "DASHBOARD"));
        navPanel.add(createNavButton("VEHICLES", "Vehicles", "VEHICLES"));
        navPanel.add(createNavButton("SLOTS", "Parking Slots", "SLOTS"));
        navPanel.add(createNavButton("ENTRY", "Parking Entry", "ENTRY"));
        navPanel.add(createNavButton("EXIT", "Parking Exit & Pay", "EXIT"));
        navPanel.add(createNavButton("RECORDS", "Parking Records", "RECORDS"));
        navPanel.add(createNavButton("PAYMENTS", "Payments", "PAYMENTS"));

        sidebarPanel.add(navPanel, BorderLayout.CENTER);

        // Footer info in sidebar
        JPanel sidebarFooter = new JPanel(new BorderLayout());
        sidebarFooter.setBackground(Constants.PRIMARY_DARK);
        sidebarFooter.setBorder(new EmptyBorder(15, 20, 20, 20));

        JLabel lblVersion = new JLabel("System Version 1.0.0");
        lblVersion.setFont(Constants.FONT_SMALL);
        lblVersion.setForeground(new Color(140, 150, 175));
        sidebarFooter.add(lblVersion, BorderLayout.SOUTH);

        sidebarPanel.add(sidebarFooter, BorderLayout.SOUTH);
    }

    private JButton createNavButton(String cardName, String title, String iconType) {
        JButton btn = new JButton(title);
        btn.setFont(Constants.FONT_SIDEBAR);
        btn.setForeground(new Color(210, 220, 240));
        btn.setBackground(Constants.PRIMARY_DARK);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIcon(IconHelper.getIcon(iconType, 18, new Color(180, 200, 235)));
        btn.setIconTextGap(12);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));

        btn.addActionListener(e -> switchCard(cardName));

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!cardName.equals(currentActiveCard)) {
                    btn.setBackground(Constants.PRIMARY_MEDIUM);
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!cardName.equals(currentActiveCard)) {
                    btn.setBackground(Constants.PRIMARY_DARK);
                }
            }
        });

        navButtons.put(cardName, btn);
        return btn;
    }

    private void createContentPanels() {
        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);
        contentContainer.setBackground(Constants.BG_LIGHT);

        // Instantiate child panels
        dashboardPanel = new DashboardPanel(dashboardManager);
        vehiclePanel = new VehiclePanel(vehicleManager);
        slotPanel = new ParkingSlotPanel(slotManager);
        entryPanel = new ParkingEntryPanel(parkingManager, vehicleManager, slotManager);
        exitPanel = new ParkingExitPanel(parkingManager, paymentManager, vehicleManager);
        recordsPanel = new ParkingRecordsPanel(parkingManager);
        paymentPanel = new PaymentPanel(paymentManager);

        // Add to CardLayout container
        contentContainer.add(dashboardPanel, "DASHBOARD");
        contentContainer.add(vehiclePanel, "VEHICLES");
        contentContainer.add(slotPanel, "SLOTS");
        contentContainer.add(entryPanel, "ENTRY");
        contentContainer.add(exitPanel, "EXIT");
        contentContainer.add(recordsPanel, "RECORDS");
        contentContainer.add(paymentPanel, "PAYMENTS");
    }

    private void createStatusBar(JPanel rootPanel) {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Constants.BG_WHITE);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.BORDER_COLOR),
                new EmptyBorder(6, 15, 6, 15)
        ));

        JLabel lblStatus = new JLabel("System Ready | Operational");
        lblStatus.setFont(Constants.FONT_SMALL);
        lblStatus.setForeground(Constants.TEXT_MUTED);

        lblClock = new JLabel();
        lblClock.setFont(Constants.FONT_SMALL);
        lblClock.setForeground(Constants.TEXT_MUTED);

        // Timer for clock updates
        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
        updateClock();

        statusBar.add(lblStatus, BorderLayout.WEST);
        statusBar.add(lblClock, BorderLayout.EAST);
        rootPanel.add(statusBar, BorderLayout.SOUTH);
    }

    private void updateClock() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        lblClock.setText(LocalDateTime.now().format(dtf));
    }

    /**
     * Switches the active card in CardLayout and refreshes the destination panel data.
     */
    public void switchCard(String cardName) {
        currentActiveCard = cardName;
        cardLayout.show(contentContainer, cardName);

        // Update button visual states
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            if (entry.getKey().equals(cardName)) {
                entry.getValue().setBackground(Constants.PRIMARY_LIGHT);
                entry.getValue().setForeground(Color.WHITE);
                entry.getValue().setFont(new Font("Segoe UI", Font.BOLD, 14));
            } else {
                entry.getValue().setBackground(Constants.PRIMARY_DARK);
                entry.getValue().setForeground(new Color(210, 220, 240));
                entry.getValue().setFont(Constants.FONT_SIDEBAR);
            }
        }

        // Trigger data refreshes on panel switches
        switch (cardName) {
            case "DASHBOARD":
                dashboardPanel.refreshData();
                break;
            case "VEHICLES":
                vehiclePanel.refreshTable();
                break;
            case "SLOTS":
                slotPanel.refreshTable();
                break;
            case "ENTRY":
                entryPanel.refreshData();
                break;
            case "EXIT":
                exitPanel.refreshData();
                break;
            case "RECORDS":
                recordsPanel.refreshTable();
                break;
            case "PAYMENTS":
                paymentPanel.refreshTable();
                break;
        }
    }
}
