package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;

public class DeleteStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField idField;
    private JTextArea studentInfoArea;
    private JButton deleteButton;
    private Student foundStudent;

    public DeleteStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        this.foundStudent = null;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());

        // Title
        JLabel titleLabel = createTitleLabel("DELETE STUDENT");

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Student ID input
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        idField = createTextField(15);
        formPanel.add(idField, gbc);

        // Search button
        gbc.gridx = 2;
        JButton searchButton = createStyledButton("Search", new Color(52, 152, 219));
        searchButton.addActionListener(e -> searchStudent());
        formPanel.add(searchButton, gbc);

        // Student Information Display Area
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(20, 10, 10, 10);

        studentInfoArea = new JTextArea(8, 30);
        studentInfoArea.setEditable(false);
        studentInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        studentInfoArea.setBorder(BorderFactory.createTitledBorder("Student Information"));
        studentInfoArea.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(studentInfoArea);
        formPanel.add(scrollPane, gbc);

        // Reset gridwidth and weight
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Button panel
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 10, 10);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        deleteButton = createStyledButton("Delete Student", new Color(231, 76, 60));
        deleteButton.setEnabled(false); // Disabled until student is found
        JButton backButton = createStyledButton("Back to Menu", new Color(149, 165, 166));

        deleteButton.addActionListener(e -> deleteStudent());
        backButton.addActionListener(e -> {
            clearForm();
            parentFrame.showPanel("MainMenu");
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);

        // Main layout
        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = createGridBagConstraints();
        mainGbc.insets = new Insets(10, 20, 10, 20);

        add(titleLabel, mainGbc);
        mainGbc.gridy = 1;
        add(formPanel, mainGbc);
    }

    private void searchStudent() {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            showError("Please enter a Student ID to search.");
            return;
        }

        try {
            Validator.validateID(id);

            foundStudent = manager.findStudentById(id);

            if (foundStudent == null) {
                studentInfoArea.setText("No student found with ID: " + id);
                deleteButton.setEnabled(false);
                foundStudent = null;
                return;
            }

            // Display student information
            displayStudentInfo(foundStudent);
            deleteButton.setEnabled(true);

        } catch (InvalidInputException e) {
            showError(e.getMessage());
            deleteButton.setEnabled(false);
            foundStudent = null;
            studentInfoArea.setText("");
        }
    }

    private void displayStudentInfo(Student student) {
        // Build the formatted string step by step for clarity
        StringBuilder info = new StringBuilder();
        info.append("╔════════════════════════════════════════════╗\n");
        info.append(String.format("║ %-42s ║\n", "STUDENT INFORMATION"));
        info.append("╠════════════════════════════════════════════╣\n");
        info.append(String.format("║ ID:         %-30s ║\n", student.getId()));
        info.append(String.format("║ First Name: %-30s ║\n", student.getFirstName()));
        info.append(String.format("║ Last Name:  %-30s ║\n", student.getLastName()));
        info.append(String.format("║ Email:      %-30s ║\n", student.getEmail()));
        info.append(String.format("║ GWA:        %-30s ║\n", student.getGwa()));
        info.append("╚════════════════════════════════════════════╝");

        studentInfoArea.setText(info.toString());
    }

    private void deleteStudent() {
        if (foundStudent == null) {
            showError("No student selected for deletion. Please search for a student first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this student?\n\n" +
                        "ID: " + foundStudent.getId() + "\n" +
                        "Name: " + foundStudent.getFirstName() + " " + foundStudent.getLastName() + "\n\n" +
                        "This action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                manager.deleteStudent(foundStudent.getId());
                showSuccess("Student deleted successfully!");
                clearForm();
            } catch (Exception e) {
                showError("Error deleting student: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        studentInfoArea.setText("");
        deleteButton.setEnabled(false);
        foundStudent = null;
    }

    // This method will be called when the panel is shown
    @Override
    public void onPanelShown() {
        clearForm();
    }
}