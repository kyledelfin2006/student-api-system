package UI;

import Manager.StudentManager;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class AdvancedSearchPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private final DefaultTableModel tableModel;
    private final JLabel countLabel;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField gwaMinField;
    private JTextField gwaMaxField;

    public AdvancedSearchPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        this.tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Email", "GWA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.countLabel = createSubtitleLabel("");
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(1040, 900));

        JPanel header = createSectionPanel(new BorderLayout());
        header.add(createHeader("Advanced Search", "Filter students by name, email, or GWA range."), BorderLayout.CENTER);
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(countLabel, BorderLayout.EAST);

        JPanel formBody = createSectionPanel(new BorderLayout(0, 8));
        formBody.setOpaque(false);

        JPanel fieldsGrid = new JPanel(new GridLayout(0, 2, 12, 4));
        fieldsGrid.setOpaque(false);
        fieldsGrid.add(createAlignedLabel("First Name"));
        fieldsGrid.add(firstNameField = createTextField(14));
        fieldsGrid.add(createAlignedLabel("Last Name"));
        fieldsGrid.add(lastNameField = createTextField(14));
        fieldsGrid.add(createAlignedLabel("Email"));
        fieldsGrid.add(emailField = createTextField(14));

        JPanel gwaPanel = new JPanel(new GridLayout(1, 4, 8, 0));
        gwaPanel.setOpaque(false);
        gwaPanel.add(createAlignedLabel("GWA Min"));
        gwaPanel.add(gwaMinField = createTextField(8));
        gwaPanel.add(createAlignedLabel("GWA Max"));
        gwaPanel.add(gwaMaxField = createTextField(8));

        JPanel searchRow = createButtonRow();
        JButton searchButton = createPrimaryButton("Search");
        searchButton.addActionListener(e -> performSearch());
        searchRow.add(searchButton);

        formBody.add(fieldsGrid, BorderLayout.NORTH);
        formBody.add(gwaPanel, BorderLayout.CENTER);
        formBody.add(searchRow, BorderLayout.SOUTH);

        JTable resultTable = new JTable(tableModel);
        resultTable.setFont(TEXT_FONT);
        resultTable.setRowHeight(24);
        resultTable.setGridColor(BORDER_COLOR);
        resultTable.setShowVerticalLines(false);
        resultTable.setShowHorizontalLines(true);
        resultTable.setSelectionBackground(new Color(226, 232, 240));
        resultTable.setSelectionForeground(TEXT_PRIMARY);
        resultTable.setFillsViewportHeight(true);
        resultTable.setBackground(SURFACE_COLOR);
        resultTable.setForeground(TEXT_PRIMARY);
        resultTable.setIntercellSpacing(new Dimension(0, 1));
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? SURFACE_COLOR : new Color(249, 250, 252));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return component;
            }
        });

        JTableHeader tableHeader = resultTable.getTableHeader();
        tableHeader.setFont(BUTTON_FONT);
        tableHeader.setBackground(new Color(238, 240, 243));
        tableHeader.setForeground(TEXT_PRIMARY);
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableHeader.setOpaque(true);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setBackground(new Color(238, 240, 243));
        headerRenderer.setForeground(TEXT_PRIMARY);

        JScrollPane tableScrollPane = createScrollPane(resultTable);

        JPanel bottomRow = createButtonRow();
        JButton clearButton = createTertiaryButton("Clear");
        JButton backButton = createSecondaryButton("Dashboard");
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> {
            clearForm();
            parentFrame.showPanel("MainMenu");
        });
        bottomRow.add(clearButton);
        bottomRow.add(backButton);

        JPanel body = createSectionPanel(new BorderLayout(0, 10));
        body.add(formBody, BorderLayout.NORTH);
        body.add(tableScrollPane, BorderLayout.CENTER);
        body.add(bottomRow, BorderLayout.SOUTH);

        card.add(header, BorderLayout.NORTH);
        card.add(body, BorderLayout.CENTER);
        add(wrapInPage(card, 1060), BorderLayout.CENTER);
    }

    private void performSearch() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String gwaMin = gwaMinField.getText().trim();
        String gwaMax = gwaMaxField.getText().trim();

        if (firstName.isEmpty() && lastName.isEmpty() && email.isEmpty() && gwaMin.isEmpty() && gwaMax.isEmpty()) {
            showError("Please enter at least one search criteria.");
            return;
        }

        if (!gwaMin.isEmpty() || !gwaMax.isEmpty()) {
            if (!gwaMin.isEmpty()) {
                try {
                    Double.parseDouble(gwaMin);
                } catch (NumberFormatException e) {
                    showError("GWA Min must be a valid number.");
                    gwaMinField.requestFocusInWindow();
                    return;
                }
            }
            if (!gwaMax.isEmpty()) {
                try {
                    Double.parseDouble(gwaMax);
                } catch (NumberFormatException e) {
                    showError("GWA Max must be a valid number.");
                    gwaMaxField.requestFocusInWindow();
                    return;
                }
            }
            if (!gwaMin.isEmpty() && !gwaMax.isEmpty()) {
                double min = Double.parseDouble(gwaMin);
                double max = Double.parseDouble(gwaMax);
                if (min > max) {
                    showError("GWA Min cannot be greater than GWA Max.");
                    return;
                }
            }
        }

        try {
            List<Student> results = manager.searchStudents(firstName, lastName, email, gwaMin, gwaMax);
            refreshTable(results);
        } catch (Exception e) {
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void refreshTable(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getGwa()
            });
        }
        countLabel.setText(students.size() + (students.size() == 1 ? " result" : " results"));
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        gwaMinField.setText("");
        gwaMaxField.setText("");
        tableModel.setRowCount(0);
        countLabel.setText("");
    }

    private JLabel createAlignedLabel(String text) {
        JLabel label = createLabel(text);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    @Override
    public void onPanelShown() {
        clearForm();
    }
}
