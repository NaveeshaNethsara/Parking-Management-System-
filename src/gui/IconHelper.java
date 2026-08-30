package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Utility class providing vector-drawn Swing Icons using Graphics2D.
 * Completely eliminates dependence on emoji/Unicode fonts and ensures
 * crisp, modern UI rendering across all operating systems.
 */
public class IconHelper {

    /**
     * Creates a vector icon for navigation tabs and dashboard cards.
     */
    public static Icon getIcon(String type, int size, Color color) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.translate(x, y);

                int s = size;

                switch (type.toUpperCase()) {
                    case "LOGO_P":
                        // Rounded square with letter 'P'
                        g2.fill(new RoundRectangle2D.Float(0, 0, s, s, s * 0.35f, s * 0.35f));
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, (int) (s * 0.68)));
                        FontMetrics fmLogo = g2.getFontMetrics();
                        int tx = (s - fmLogo.stringWidth("P")) / 2;
                        int ty = (s - fmLogo.getHeight()) / 2 + fmLogo.getAscent();
                        g2.drawString("P", tx, ty);
                        break;

                    case "DASHBOARD":
                        // 2x2 grid of small rounded rectangles
                        int gap = Math.max(2, s / 8);
                        int w = (s - gap) / 2;
                        g2.fillRoundRect(0, 0, w, w, 3, 3);
                        g2.fillRoundRect(w + gap, 0, w, w, 3, 3);
                        g2.fillRoundRect(0, w + gap, w, w, 3, 3);
                        g2.fillRoundRect(w + gap, w + gap, w, w, 3, 3);
                        break;

                    case "VEHICLES":
                        // Car body representation
                        g2.setStroke(new BasicStroke(Math.max(1.5f, s / 10f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        // Roof / cabin
                        g2.drawRoundRect(s / 5, s / 6, (s * 3) / 5, s / 3, 3, 3);
                        // Body
                        g2.fillRoundRect(0, s / 3 + s / 12, s, (s * 4) / 10, 4, 4);
                        // Wheels
                        g2.setColor(Color.DARK_GRAY);
                        g2.fillOval(s / 6, (s * 7) / 10, s / 4, s / 4);
                        g2.fillOval((s * 7) / 12, (s * 7) / 10, s / 4, s / 4);
                        break;

                    case "SLOTS":
                        // Parking lot sign with 'P'
                        g2.setStroke(new BasicStroke(Math.max(1.5f, s / 10f)));
                        g2.drawRoundRect(1, 1, s - 3, s - 3, 5, 5);
                        g2.setFont(new Font("Segoe UI", Font.BOLD, (int) (s * 0.65)));
                        FontMetrics fm = g2.getFontMetrics();
                        int px = (s - fm.stringWidth("P")) / 2;
                        int py = (s - fm.getHeight()) / 2 + fm.getAscent();
                        g2.drawString("P", px, py);
                        break;

                    case "ENTRY":
                        // Right-pointing arrow into gate
                        g2.setStroke(new BasicStroke(Math.max(2f, s / 7f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(2, s / 2, s - 4, s / 2);
                        g2.drawLine(s - 8, s / 2 - 5, s - 3, s / 2);
                        g2.drawLine(s - 8, s / 2 + 5, s - 3, s / 2);
                        // Vertical bar on right (entry barrier)
                        g2.drawLine(s - 1, 3, s - 1, s - 3);
                        break;

                    case "EXIT":
                        // Left-pointing arrow out of gate
                        g2.setStroke(new BasicStroke(Math.max(2f, s / 7f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(s - 2, s / 2, 4, s / 2);
                        g2.drawLine(8, s / 2 - 5, 3, s / 2);
                        g2.drawLine(8, s / 2 + 5, 3, s / 2);
                        // Vertical bar on left (exit barrier)
                        g2.drawLine(1, 3, 1, s - 3);
                        break;

                    case "RECORDS":
                        // Document with 3 horizontal lines
                        g2.setStroke(new BasicStroke(Math.max(1.5f, s / 11f)));
                        g2.drawRoundRect(s / 6, 1, (s * 2) / 3, s - 3, 3, 3);
                        int lineStart = s / 3;
                        int lineEnd = (s * 2) / 3;
                        g2.drawLine(lineStart, s / 3, lineEnd, s / 3);
                        g2.drawLine(lineStart, s / 2, lineEnd, s / 2);
                        g2.drawLine(lineStart, (s * 2) / 3, lineEnd, (s * 2) / 3);
                        break;

                    case "PAYMENTS":
                        // Credit card shape
                        g2.setStroke(new BasicStroke(Math.max(1.5f, s / 11f)));
                        g2.drawRoundRect(1, s / 4, s - 3, (s * 5) / 9, 4, 4);
                        g2.fillRect(1, s / 3 + 2, s - 3, s / 6);
                        break;

                    case "CHECK":
                        // Green checkmark
                        g2.setStroke(new BasicStroke(Math.max(2f, s / 7f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(3, s / 2, s / 3 + 2, s - 4);
                        g2.drawLine(s / 3 + 2, s - 4, s - 3, 3);
                        break;

                    case "CROSS":
                        // Red cross
                        g2.setStroke(new BasicStroke(Math.max(2f, s / 7f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.drawLine(4, 4, s - 4, s - 4);
                        g2.drawLine(s - 4, 4, 4, s - 4);
                        break;

                    case "CLOCK":
                        // Clock circle with hands
                        g2.setStroke(new BasicStroke(Math.max(1.5f, s / 9f)));
                        g2.drawOval(1, 1, s - 3, s - 3);
                        g2.drawLine(s / 2, s / 2, s / 2, s / 4);
                        g2.drawLine(s / 2, s / 2, (s * 3) / 4, s / 2);
                        break;

                    default:
                        // Dot / bullet
                        g2.fillOval(s / 4, s / 4, s / 2, s / 2);
                        break;
                }

                g2.dispose();
            }

            @Override
            public int getIconWidth() {
                return size;
            }

            @Override
            public int getIconHeight() {
                return size;
            }
        };
    }
}
