package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    protected static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    protected static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    public BasePanel() {
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    protected JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text); // Instantiate Label
        label.setFont(TITLE_FONT); // Set font
        label.setForeground(new Color(44, 62, 80)); // Set Color Foreground
        label.setHorizontalAlignment(SwingConstants.CENTER); // Centered
        return label; // Return the label
    }

    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text); // Instantiate a basic Label
        label.setFont(LABEL_FONT); // Set Label font
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    protected JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    protected JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(LABEL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return textField;
    }

    protected GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 0, 3, 0);
        gbc.weightx = 1;
        return gbc;
    }

    protected JPanel getPanel(String panelName) {
        return (JPanel) getParent().getComponent(0).getParent();
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}