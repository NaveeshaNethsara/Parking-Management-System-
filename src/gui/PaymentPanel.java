package gui;

import models.Payment;
import services.PaymentManager;
import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Panel for viewing and managing Payment records (FR-14).
 * Displays payment history, payment methods, timestamps, and total revenue.
 */
public class PaymentPanel extends JPanel {
    private PaymentManager paymentManager;
    private JTable paymentTable;
    private DefaultTableModel tableModel;
    private JLabel lblTotalRevenueBanner;

    public PaymentPanel(PaymentManager paymentManager) {
        this.paymentManager = paymentManager;
        setLayout(new BorderLayout());
        setBackground(Constants.BG_LIGHT);
        setBorder(new EmptyBorder(30, 30, 30, 30));
        initComponents();
    }

    private void initComponents() {
        // Title
        add(DialogHelper.createSectionTitle("Payment Management"), BorderLayout.NORTH);

        JPanel contentPanel = DialogHelper.createCardPanel();
        contentPanel.setLayout(new BorderLayout(0, 15));

        // Top Revenue Summary Banner
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(new Color(232, 245, 233)); // Light green tint
        bannerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.ACCENT_GREEN, 1),
                new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel bannerTitle = new JLabel("Total Parking Revenue Collected");
        bannerTitle.setFont(Constants.FONT_HEADER);
        bannerTitle.setForeground(new Color(46, 125, 50));

        lblTotalRevenueBanner = new JLabel("LKR 0.00");
        lblTotalRevenueBanner.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotalRevenueBanner.setForeground(new Color(46, 125, 50));

        bannerPanel.add(bannerTitle, BorderLayout.WEST);
        bannerPanel.add(lblTotalRevenueBanner, BorderLayout.EAST);

        contentPanel.add(bannerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Payment ID", "Ticket ID", "Amount (LKR)", "Payment Method", "Payment Date & Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        paymentTable = new JTable(tableModel);
        styleTable(paymentTable);

        JScrollPane scrollPane = new JScrollPane(paymentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Constants.BORDER_COLOR));
        scrollPane.getViewport().setBackground(Constants.BG_WHITE);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Payment> payments = paymentManager.getAllPayments();
        for (Payment p : payments) {
            tableModel.addRow(new Object[]{
                    p.getPaymentId(),
                    p.getTicketId(),
                    String.format("%.2f", p.getAmount()),
                    p.getPaymentMethod(),
                    p.getPaymentDate()
            });
        }
        lblTotalRevenueBanner.setText("LKR " + String.format("%.2f", paymentManager.getTotalRevenue()));
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
                    if (column == 2) {
                        setForeground(Constants.ACCENT_GREEN);
                        setFont(Constants.FONT_BUTTON);
                    } else {
                        setForeground(Constants.TEXT_DARK);
                        setFont(Constants.FONT_BODY);
                    }
                }
                setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }
}
