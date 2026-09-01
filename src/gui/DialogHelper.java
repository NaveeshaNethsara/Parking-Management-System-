package gui;

import utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Utility class providing reusable dialog and UI component helpers.
 * Centralizes common Swing UI operations.
 */
public class DialogHelper {

    /**
     * Shows an error message dialog with default title.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static void showError(Component parent, String message) {
        showError(parent, "Error", message);
    }

    /**
     * Shows an error message dialog with custom title.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static void showError(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a success/information message dialog with default title.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static void showSuccess(Component parent, String message) {
        showSuccess(parent, "Success", message);
    }

    /**
     * Shows a success/information message dialog with custom title.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static void showSuccess(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a warning message dialog with default title.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static void showWarning(Component parent, String message) {
        showWarning(parent, "Warning", message);
    }

    /**
     * Shows a warning message dialog with custom title.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static void showWarning(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows a confirmation dialog (Yes/No).
     * @return true if the user clicked "Yes".
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Shows an input dialog to get text from the user.
     * @return The entered text, or null if cancelled.
     */
    public static String showInput(Component parent, String message) {
        return JOptionPane.showInputDialog(parent, message, "Input", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Creates a styled JButton with default size.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static JButton createStyledButton(String text, Color bgColor) {
        return createStyledButton(text, bgColor, 130, 36);
    }

    /**
     * Creates a styled JButton with custom dimensions.
     * Demonstrates: Static Polymorphism (Method Overloading).
     */
    public static JButton createStyledButton(String text, Color bgColor, int width, int height) {
         JButton button = new JButton(text);
         button.setFont(Constants.FONT_BUTTON);
         button.setBackground(bgColor);
         button.setForeground(Color.WHITE);
         button.setFocusPainted(false);
         button.setBorderPainted(false);
         button.setOpaque(true);
         button.setCursor(new Cursor(Cursor.HAND_CURSOR));
         button.setPreferredSize(new Dimension(width, height));

        // Hover effect
        Color hoverColor = bgColor.brighter();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Creates a styled JLabel for form fields.
     */
    public static JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Constants.FONT_BODY);
        label.setForeground(Constants.TEXT_DARK);
        return label;
    }

    /**
     * Creates a styled JTextField for form input.
     */
    public static JTextField createFormTextField() {
        JTextField field = new JTextField();
        field.setFont(Constants.FONT_BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_COLOR, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        field.setPreferredSize(new Dimension(250, 36));
        return field;
    }

    /**
     * Creates a styled JComboBox.
     */
    public static <T> JComboBox<T> createFormComboBox(T[] items) {
        JComboBox<T> combo = new JComboBox<>(items);
        combo.setFont(Constants.FONT_BODY);
        combo.setPreferredSize(new Dimension(250, 36));
        return combo;
    }

    /**
     * Creates a section title label.
     */
    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Constants.FONT_TITLE);
        label.setForeground(Constants.TEXT_DARK);
        label.setBorder(new EmptyBorder(0, 0, 15, 0));
        return label;
    }

    /**
     * Creates a panel with a card-like appearance (white background, rounded feel, shadow-like border).
     */
    public static JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(Constants.BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Constants.BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    private DialogHelper() {
        // Prevent instantiation
    }
}
