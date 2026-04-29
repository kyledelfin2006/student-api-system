package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;

public class CreateStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField firstNameField, lastNameField, emailField, gwaField;

    public CreateStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());

        // Title
        JLabel titleLabel = createTitleLabel("CREATE NEW STUDENT");

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // First Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createLabel("First Name:"), gbc);
        gbc.gridx = 1;
        firstNameField = createTextField(20);
        formPanel.add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        lastNameField = createTextField(20);
        formPanel.add(lastNameField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = createTextField(20);
        formPanel.add(emailField, gbc);

        // GWA
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(createLabel("GWA (1.0-5.0):"), gbc);
        gbc.gridx = 1;
        gwaField = createTextField(20);
        formPanel.add(gwaField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton createButton = createStyledButton("Create Student", new Color(46, 204, 113));
        JButton clearButton = createStyledButton("Clear", new Color(52, 152, 219));
        JButton backButton = createStyledButton("Back to Menu", new Color(149, 165, 166));

        createButton.addActionListener(e -> createStudent());
        clearButton.addActionListener(e -> clearFields());
        backButton.addActionListener(e -> parentFrame.showPanel("MainMenu"));

        buttonPanel.add(createButton);
        buttonPanel.add(clearButton);
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

    private void createStudent() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String gwa = gwaField.getText().trim();

        try {
            Validator.validateName(firstName);
            Validator.validateName(lastName);
            Validator.validateEmail(email);
            Validator.validateGWA(gwa);

            manager.createStudent(email, gwa, lastName, firstName);
            showSuccess("Student created successfully!");
            clearFields();
        } catch (InvalidInputException e) {
            showError(e.getMessage());
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        gwaField.setText("");
    }
}