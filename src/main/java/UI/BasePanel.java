package UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public abstract class BasePanel extends JPanel {

    // Shared colors keep the screens consistent and easy to adjust from one place.
    protected static final Color PAGE_BACKGROUND = new Color(245, 246, 248);
    protected static final Color SURFACE_COLOR = Color.WHITE;
    protected static final Color PRIMARY_COLOR = new Color(42, 92, 170);
    protected static final Color PRIMARY_HOVER = new Color(33, 75, 142);
    protected static final Color SECONDARY_COLOR = new Color(96, 104, 116);
    protected static final Color SECONDARY_HOVER = new Color(76, 84, 96);
    protected static final Color TERTIARY_COLOR = new Color(225, 230, 236);
    protected static final Color TERTIARY_HOVER = new Color(211, 218, 228);
    protected static final Color SUCCESS_COLOR = new Color(36, 128, 79);
    protected static final Color SUCCESS_HOVER = new Color(29, 103, 64);
    protected static final Color WARNING_COLOR = new Color(96, 104, 116);
    protected static final Color WARNING_HOVER = new Color(76, 84, 96);
    protected static final Color DANGER_COLOR = new Color(180, 47, 59);
    protected static final Color DANGER_HOVER = new Color(146, 36, 47);
    protected static final Color TEXT_PRIMARY = new Color(31, 35, 40);
    protected static final Color TEXT_MUTED = new Color(93, 99, 108);
    protected static final Color BORDER_COLOR = new Color(209, 213, 219);
    protected static final Color INPUT_BACKGROUND = new Color(250, 251, 252);
    protected static final Color PANEL_TINT = new Color(236, 239, 244);
    protected static final Color DISABLED_BACKGROUND = new Color(229, 231, 235);

    private static final String FONT_FAMILY = "Segoe UI";

    protected static final Font TITLE_FONT = new Font(FONT_FAMILY, Font.BOLD, 22);
    protected static final Font SUBTITLE_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    protected static final Font LABEL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    protected static final Font BUTTON_FONT = new Font(FONT_FAMILY, Font.BOLD, 13);
    protected static final Font TEXT_FONT = new Font(FONT_FAMILY, Font.PLAIN, 13);
    protected static final Font STUDENT_INFO_FONT = new Font(FONT_FAMILY, Font.PLAIN, 16);

    protected BasePanel() {
        setBackground(PAGE_BACKGROUND);
        setBorder(new EmptyBorder(18, 18, 18, 18));
    }

    protected JPanel createPagePanel() {
        JPanel pagePanel = new JPanel(new GridBagLayout());
        pagePanel.setOpaque(false);
        return pagePanel;
    }

    protected JPanel createCardPanel() {
        JPanel card = new JPanel(new BorderLayout(0, 16));
        card.setBackground(SURFACE_COLOR);
        card.setOpaque(true);
        card.setBorder(createCardBorder());
        return card;
    }

    protected JPanel createSectionPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        return panel;
    }

    protected Border createCardBorder() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(22, 22, 22, 22)
        );
    }

    protected JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    protected JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(SUBTITLE_FONT);
        label.setForeground(TEXT_MUTED);
        return label;
    }

    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    protected JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY_COLOR, PRIMARY_HOVER);
    }

    protected JButton createSecondaryButton(String text) {
        return createButton(text, SECONDARY_COLOR, SECONDARY_HOVER);
    }

    protected JButton createSuccessButton(String text) {
        return createButton(text, SUCCESS_COLOR, SUCCESS_HOVER);
    }

    protected JButton createTertiaryButton(String text) {
        return createButton(text, TERTIARY_COLOR, TERTIARY_HOVER);
    }

    protected JButton createWarningButton(String text) {
        return createButton(text, WARNING_COLOR, WARNING_HOVER);
    }

    protected JButton createDangerButton(String text) {
        return createButton(text, DANGER_COLOR, DANGER_HOVER);
    }

    protected JButton createButton(String text, Color background, Color hover) {
        Color foreground = isLightColor(background) ? TEXT_PRIMARY : Color.WHITE;
        JButton button = new JButton(text);
        // BasicButtonUI lets our background colors show instead of the OS repainting buttons white.
        button.setUI(new BasicButtonUI());
        button.setFont(BUTTON_FONT);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(isLightColor(background) ? BORDER_COLOR : background.darker()),
                new EmptyBorder(9, 15, 9, 15)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.getModel().addChangeListener(e -> setButtonColors(button, background, hover, foreground));
        setButtonColors(button, background, hover, foreground);
        return button;
    }

    protected JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(TEXT_FONT);
        textField.setBackground(INPUT_BACKGROUND);
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(TEXT_PRIMARY);
        textField.setOpaque(true);
        textField.setBorder(createInputBorder());
        textField.setMargin(new Insets(4, 6, 4, 6));
        return textField;
    }

    protected JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(TEXT_FONT);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBackground(INPUT_BACKGROUND);
        comboBox.setBorder(createInputBorder());
        comboBox.setFocusable(false);
        comboBox.setOpaque(true);
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(6, 10, 6, 10));
                label.setBackground(isSelected ? PANEL_TINT : INPUT_BACKGROUND);
                label.setForeground(TEXT_PRIMARY);
                return label;
            }
        });
        comboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = new JButton("\u25BE");
                arrowButton.setUI(new BasicButtonUI());
                arrowButton.setBackground(INPUT_BACKGROUND);
                arrowButton.setOpaque(true);
                arrowButton.setContentAreaFilled(true);
                arrowButton.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 10));
                arrowButton.setBorderPainted(false);
                arrowButton.setFocusPainted(false);
                arrowButton.setForeground(TEXT_MUTED);
                return arrowButton;
            }
        });
        return comboBox;
    }

    protected JTextArea createTextArea(int rows, int columns) {
        JTextArea textArea = new JTextArea(rows, columns);
        textArea.setFont(STUDENT_INFO_FONT);
        textArea.setEditable(false);
        textArea.setBackground(INPUT_BACKGROUND);
        textArea.setForeground(TEXT_PRIMARY);
        textArea.setOpaque(true);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(12, 12, 12, 12));
        return textArea;
    }

    protected JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBorder(createInputBorder());
        scrollPane.getViewport().setBackground(INPUT_BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.setBackground(SURFACE_COLOR);
        scrollPane.setOpaque(false);
        return scrollPane;
    }

    protected JPanel createHeader(String title, String subtitle) {
        JPanel header = createSectionPanel(new BorderLayout(0, 6));

        JPanel textPanel = createSectionPanel(new GridLayout(0, 1, 0, 4));
        textPanel.add(createTitleLabel(title));
        if (subtitle != null && !subtitle.isBlank()) {
            textPanel.add(createSubtitleLabel(subtitle));
        }

        header.add(textPanel, BorderLayout.CENTER);
        return header;
    }

    protected JPanel createFormGrid() {
        JPanel panel = createSectionPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(4, 0, 4, 0));
        return panel;
    }

    // Keeps labels and fields aligned the same way on every form screen.
    protected void addFormRow(JPanel panel, int row, String labelText, JComponent field) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 8, 12);
        panel.add(createLabel(labelText), gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);
        panel.add(field, gbc);
    }

    protected JPanel createButtonRow() {
        JPanel panel = createSectionPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));
        return panel;
    }

    protected GridBagConstraints createPageConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
    }

    protected JPanel wrapInPage(JComponent content, int maxWidth) {
        JPanel page = createPagePanel();
        JPanel widthWrapper = createSectionPanel(new BorderLayout());
        widthWrapper.add(content, BorderLayout.CENTER);

        Dimension preferred = content.getPreferredSize();
        int width = preferred.width > 0 ? Math.min(preferred.width, maxWidth) : maxWidth;
        widthWrapper.setPreferredSize(new Dimension(width, preferred.height));

        page.add(widthWrapper, createPageConstraints());
        return page;
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void setButtonColors(JButton button, Color background, Color hover, Color foreground) {
        ButtonModel model = button.getModel();
        boolean enabled = button.isEnabled();
        button.setBackground(!enabled ? DISABLED_BACKGROUND : model.isRollover() ? hover : background);
        button.setForeground(enabled ? foreground : TEXT_MUTED);
    }

    private Border createInputBorder() {
        return new CompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(8, 10, 8, 10)
        );
    }

    private static boolean isLightColor(Color color) {
        int brightness = (color.getRed() * 299 + color.getGreen() * 587 + color.getBlue() * 114) / 1000;
        return brightness >= 190;
    }

    public abstract void onPanelShown();
}
