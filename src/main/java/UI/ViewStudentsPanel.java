package UI;

import Manager.StudentManager;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewStudentsPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private DefaultTableModel tableModel;

    public ViewStudentsPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = createTitleLabel("ALL STUDENTS");
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Table
        // Array list of the columns
        String[] columns = {"ID", "First Name", "Last Name", "Email", "GWA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Setup JTable
        JTable studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 13));
        studentTable.setRowHeight(50);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(new Color(52, 73, 94));
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.setSelectionBackground(new Color(52, 152, 219));
        studentTable.setSelectionForeground(Color.WHITE);
        studentTable.setShowGrid(true);
        studentTable.setGridColor(new Color(189, 195, 199));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));
        JButton backButton = createStyledButton("Back to Menu", new Color(149, 165, 166));

        refreshButton.addActionListener(e -> refreshData());
        backButton.addActionListener(e -> parentFrame.showPanel("MainMenu"));

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        // Add components
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshData();
    }

    public void refreshData() {
        tableModel.setRowCount(0);
        List<Student> students = manager.repository.getAll();

        for (Student student : students) {
            Object[] row = {
                    student.getId(),
                    student.getFirstName(),
                    student.getLastName(),
                    student.getEmail(),
                    student.getGwa()  // FIXED: Display GWA as String directly, no formatting needed
            };
            tableModel.addRow(row);
        }

        if (students.isEmpty()) {
            JLabel emptyLabel = createLabel("No students found.");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(emptyLabel, BorderLayout.CENTER);
        }
    }
}