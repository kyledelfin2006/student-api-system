package UI;

import Manager.StudentManager;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UpdateStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField idField, newValueField;
    private JComboBox<String> attributeComboBox;

    public UpdateStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());

        // Title
        JLabel titleLabel = createTitleLabel("UPDATE STUDENT INFORMATION");

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

        // Attribute to update
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Attribute:"), gbc);
        gbc.gridx = 1;
        String[] attributes = {"First Name", "Last Name", "Email", "GWA"};
        attributeComboBox = new JComboBox<>(attributes);
        attributeComboBox.setFont(LABEL_FONT);
        attributeComboBox.setBackground(Color.WHITE);
        formPanel.add(attributeComboBox, gbc);

        // New value
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("New Value:"), gbc);
        gbc.gridx = 1;
        newValueField = createTextField(20);
        formPanel.add(newValueField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton updateButton = createStyledButton("Update", new Color(46, 204, 113));
        JButton backButton = createStyledButton("Back to Menu", new Color(149, 165, 166));

        updateButton.addActionListener(e -> updateStudent());
        backButton.addActionListener(e -> parentFrame.showPanel("MainMenu"));

        buttonPanel.add(updateButton);
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

    private void updateStudent() {
        String id = idField.getText().trim();
        String attribute = (String) attributeComboBox.getSelectedItem();
        String newValue = newValueField.getText().trim();

        try {
            Validator.validateID(id);

            // Validate new value based on attribute
            switch (Objects.requireNonNull(attribute).toLowerCase()) {
                case "first name", "last name" -> Validator.validateName(newValue);
                case "email" -> Validator.validateEmail(newValue);
                case "gwa" -> Validator.validateGWA(newValue);
            }

            manager.updateStudentInfo(id, attribute, newValue);
            showSuccess("Student updated successfully!");
            clearFields();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void clearFields() {
        idField.setText("");
        newValueField.setText("");
    }
}