package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;

public class CreateStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField gwaField;

    public CreateStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(700, 440));
        card.add(createHeader("Create Student", "Add a new student record with a valid email and GWA."), BorderLayout.NORTH);

        JPanel formPanel = createFormGrid();
        firstNameField = createTextField(24);
        lastNameField = createTextField(24);
        emailField = createTextField(24);
        gwaField = createTextField(24);

        addFormRow(formPanel, 0, "First Name", firstNameField);
        addFormRow(formPanel, 1, "Last Name", lastNameField);
        addFormRow(formPanel, 2, "Email", emailField);
        addFormRow(formPanel, 3, "GWA (1.0 - 5.0)", gwaField);

        JPanel buttonRow = createButtonRow();
        JButton createButton = createSuccessButton("Save Student");
        JButton clearButton = createTertiaryButton("Clear");
        JButton backButton = createSecondaryButton("Dashboard");

        createButton.addActionListener(e -> createStudent());
        clearButton.addActionListener(e -> clearFields());
        backButton.addActionListener(e -> parentFrame.showPanel("MainMenu"));

        buttonRow.add(createButton);
        buttonRow.add(clearButton);
        buttonRow.add(backButton);

        JPanel body = createSectionPanel(new BorderLayout(0, 16));
        body.add(formPanel, BorderLayout.NORTH);
        body.add(buttonRow, BorderLayout.SOUTH);

        card.add(body, BorderLayout.CENTER);
        add(wrapInPage(card, 720), BorderLayout.CENTER);
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
            showSuccess("Student created successfully.");
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
        firstNameField.requestFocusInWindow();
    }

    @Override
    public void onPanelShown() {
        clearFields();
    }
}
