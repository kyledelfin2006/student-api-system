package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;

public class DeleteStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField idField;

    public DeleteStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
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

        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        idField = createTextField(15);
        formPanel.add(idField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton deleteButton = createStyledButton("Delete Student", new Color(231, 76, 60));
        JButton backButton = createStyledButton("Back to Menu", new Color(149, 165, 166));

        deleteButton.addActionListener(e -> deleteStudent());
        backButton.addActionListener(e -> parentFrame.showPanel("MainMenu"));

        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Layout
        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = createGridBagConstraints();
        mainGbc.insets = new Insets(10, 20, 10, 20);

        add(titleLabel, mainGbc);
        mainGbc.gridy = 1;
        add(formPanel, mainGbc);
        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(30, 20, 10, 20);
        add(buttonPanel, mainGbc);
    }

    private void deleteStudent() {
        String id = idField.getText().trim();

        try {
            Validator.validateID(id);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete student with ID: " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                manager.deleteStudent(id);
                showSuccess("Student deleted successfully!");
                idField.setText("");
            }
        } catch (InvalidInputException e) {
            showError(e.getMessage());
        }
    }
}