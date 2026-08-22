package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ViewStudentsPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private final DefaultTableModel tableModel;
    private final JLabel countLabel;

    public ViewStudentsPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        this.tableModel = new DefaultTableModel(new String[]{"ID", "First Name", "Last Name", "Email", "GWA"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                if (col == 0) {
                    return;
                }

                String newValue = value.toString().trim();
                List<Student> students = manager.getAllStudents();
                if (row < 0 || row >= students.size()) {
                    return;
                }

                Student student = students.get(row);

                try {
                    switch (col) {
                        case 1 -> Validator.validateName(newValue);
                        case 2 -> Validator.validateName(newValue);
                        case 3 -> Validator.validateEmail(newValue);
                        case 4 -> Validator.validateGWA(newValue);
                    }

                    super.setValueAt(newValue, row, col);

                    switch (col) {
                        case 1 -> student.setFirstName(newValue);
                        case 2 -> student.setLastName(newValue);
                        case 3 -> student.setEmail(newValue);
                        case 4 -> student.setGwa(newValue);
                    }

                    try {
                        manager.repository.saveToFile();
                    } catch (Exception e) {
                        showError("Failed to save changes: " + e.getMessage());
                        refreshData();
                    }
                } catch (InvalidInputException e) {
                    showError(e.getMessage());
                    refreshData();
                }
            }
        };
        this.countLabel = createSubtitleLabel("");
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(980, 620));

        JPanel header = createSectionPanel(new BorderLayout());
        header.add(createHeader("Student Records", "Browse every saved student in one clean table."), BorderLayout.CENTER);
        countLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        header.add(countLabel, BorderLayout.EAST);

        JTable studentTable = new JTable(tableModel);
        studentTable.setFont(TEXT_FONT);
        studentTable.setRowHeight(28);
        studentTable.setGridColor(BORDER_COLOR);
        studentTable.setShowVerticalLines(false);
        studentTable.setShowHorizontalLines(true);
        studentTable.setSelectionBackground(new Color(226, 232, 240));
        studentTable.setSelectionForeground(TEXT_PRIMARY);
        studentTable.setFillsViewportHeight(true);
        studentTable.setBackground(SURFACE_COLOR);
        studentTable.setForeground(TEXT_PRIMARY);
        studentTable.setIntercellSpacing(new Dimension(0, 1));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        JTableHeader tableHeader = studentTable.getTableHeader();
        tableHeader.setFont(BUTTON_FONT);
        tableHeader.setBackground(new Color(238, 240, 243));
        tableHeader.setForeground(TEXT_PRIMARY);
        tableHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        tableHeader.setOpaque(true);
        tableHeader.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = tableHeader.columnAtPoint(e.getPoint());
                if (column == 2) {
                    sortByLastName();
                } else if (column == 4) {
                    sortByGwaDescending();
                }
            }
        });

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setBackground(new Color(238, 240, 243));
        headerRenderer.setForeground(TEXT_PRIMARY);

        JScrollPane tableScrollPane = createScrollPane(studentTable);

        JPanel buttonRow = createButtonRow();
        JButton refreshButton = createPrimaryButton("Refresh");
        JButton backButton = createSecondaryButton("Dashboard");
        refreshButton.addActionListener(e -> refreshData());
        backButton.addActionListener(e -> parentFrame.showPanel("MainMenu"));
        buttonRow.add(refreshButton);
        buttonRow.add(backButton);

        card.add(header, BorderLayout.NORTH);
        card.add(tableScrollPane, BorderLayout.CENTER);
        card.add(buttonRow, BorderLayout.SOUTH);
        add(wrapInPage(card, 1040), BorderLayout.CENTER);
    }

    private void sortByGwaDescending() {
        List<Student> students = new java.util.ArrayList<>(manager.getAllStudents());
        students.sort((a, b) -> {
            try {
                double gwaA = Double.parseDouble(a.getGwa());
                double gwaB = Double.parseDouble(b.getGwa());
                return Double.compare(gwaA, gwaB);
            } catch (NumberFormatException e) {
                return 0;
            }
        });

        refreshData(students);
    }

    private void sortByLastName() {
        List<Student> students = new java.util.ArrayList<>(manager.getAllStudents());
        students.sort((a, b) -> a.getLastName().compareToIgnoreCase(b.getLastName()));
        refreshData(students);
    }

    public void refreshData() {
        refreshData(null);
    }

    private void refreshData(List<Student> students) {
        tableModel.setRowCount(0);
        if (students == null) {
            students = manager.getAllStudents();
        }

        for (Student student : students) {
            tableModel.addRow(new Object[]{
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getGwa()
            });
        }

        countLabel.setText(students.size() + (students.size() == 1 ? " student" : " students"));
    }

    @Override
    public void onPanelShown() {
        refreshData();
    }
}
