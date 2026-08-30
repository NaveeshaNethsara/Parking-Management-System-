package utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Application-wide constants for the Parking Management System.
 * Centralizes configuration values, colors, and fonts.
 */
public class Constants {

    // --- Application ---
    public static final String APP_TITLE = "Parking Management System";
    public static final String DATA_DIR = "data";

    // --- Parking Rates (LKR per hour) ---
    public static final double CAR_RATE = 100.0;
    public static final double MOTORCYCLE_RATE = 50.0;
    public static final double VAN_RATE = 150.0;

    // --- Data File Names ---
    public static final String VEHICLES_FILE = "vehicles.txt";
    public static final String SLOTS_FILE = "slots.txt";
    public static final String TICKETS_FILE = "tickets.txt";
    public static final String PAYMENTS_FILE = "payments.txt";

    // --- UI Colors ---
    public static final Color PRIMARY_DARK = new Color(30, 39, 73);       // Dark navy sidebar
    public static final Color PRIMARY_MEDIUM = new Color(44, 56, 100);    // Hover state
    public static final Color PRIMARY_LIGHT = new Color(55, 70, 120);     // Selected state
    public static final Color ACCENT_BLUE = new Color(66, 133, 244);      // Accent blue
    public static final Color ACCENT_GREEN = new Color(52, 168, 83);      // Success green
    public static final Color ACCENT_RED = new Color(234, 67, 53);        // Danger red
    public static final Color ACCENT_ORANGE = new Color(251, 188, 4);     // Warning orange
    public static final Color ACCENT_PURPLE = new Color(142, 68, 173);    // Purple accent
    public static final Color BG_LIGHT = new Color(245, 247, 250);        // Light background
    public static final Color BG_WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(33, 37, 41);
    public static final Color TEXT_LIGHT = new Color(255, 255, 255);
    public static final Color TEXT_MUTED = new Color(108, 117, 125);
    public static final Color BORDER_COLOR = new Color(222, 226, 230);
    public static final Color TABLE_HEADER_BG = new Color(52, 58, 64);
    public static final Color TABLE_ALT_ROW = new Color(248, 249, 250);
    public static final Color CARD_SHADOW = new Color(0, 0, 0, 30);

    // --- UI Fonts ---
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SIDEBAR_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_STAT_NUMBER = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_STAT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);

    // --- UI Dimensions ---
    public static final int SIDEBAR_WIDTH = 240;
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 750;

    // --- Payment Methods ---
    public static final String[] PAYMENT_METHODS = {"Cash", "Card", "Online Transfer"};

    private Constants() {
        // Prevent instantiation
    }
}
