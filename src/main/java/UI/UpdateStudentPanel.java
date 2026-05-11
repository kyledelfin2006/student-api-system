package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class UpdateStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField idField;
    private JTextField newValueField;
    private JComboBox<String> attributeComboBox;
    private JTextArea studentInfoArea;
    private JButton updateButton;
    private Student foundStudent;

    public UpdateStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(820, 580));
        card.add(createHeader("Update Student", null), BorderLayout.NORTH);

        JPanel formPanel = createFormGrid();
        idField = createTextField(18);
        idField.addActionListener(e -> searchStudent());

        JPanel searchFieldRow = createSectionPanel(new BorderLayout(10, 0));
        searchFieldRow.add(idField, BorderLayout.CENTER);
        JButton searchButton = createPrimaryButton("Find");
        searchButton.addActionListener(e -> searchStudent());
        searchFieldRow.add(searchButton, BorderLayout.EAST);
        addFormRow(formPanel, 0, "Student ID", searchFieldRow);

        studentInfoArea = createTextArea(10, 40);
        JScrollPane infoScrollPane = createScrollPane(studentInfoArea);
        infoScrollPane.setPreferredSize(new Dimension(100, 220));
        addFormRow(formPanel, 1, "Student Info", infoScrollPane);

        attributeComboBox = createComboBox(new String[]{"First Name", "Last Name", "Email", "GWA"});
        addFormRow(formPanel, 2, "Field to Update", attributeComboBox);

        newValueField = createTextField(24);
        addFormRow(formPanel, 3, "New Value", newValueField);

        JPanel buttonRow = createButtonRow();
        updateButton = createSuccessButton("Apply Update");
        updateButton.setEnabled(false);
        JButton clearButton = createTertiaryButton("Clear");
        JButton backButton = createSecondaryButton("Back to Menu");

        updateButton.addActionListener(e -> updateStudent());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> {
            clearForm();
            parentFrame.showPanel("MainMenu");
        });

        buttonRow.add(updateButton);
        buttonRow.add(clearButton);
        buttonRow.add(backButton);

        JPanel body = createSectionPanel(new BorderLayout(0, 16));
        body.add(formPanel, BorderLayout.CENTER);
        body.add(buttonRow, BorderLayout.SOUTH);

        card.add(body, BorderLayout.CENTER);
        add(wrapInPage(card, 920), BorderLayout.CENTER);
    }

    private void searchStudent() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter a Student ID.");
            return;
        }

        try {
            Validator.validateID(id);
            foundStudent = manager.findStudentById(id);

            if (foundStudent == null) {
                studentInfoArea.setText("No student found with ID: " + id);
                studentInfoArea.setForeground(DANGER_COLOR);
                updateButton.setEnabled(false);
                return;
            }

            displayStudentInfo(foundStudent);
            updateButton.setEnabled(true);
        } catch (InvalidInputException e) {
            showError(e.getMessage());
            foundStudent = null;
            updateButton.setEnabled(false);
            studentInfoArea.setText("");
            studentInfoArea.setForeground(TEXT_PRIMARY);
        }
    }

    private void displayStudentInfo(Student student) {
        studentInfoArea.setForeground(TEXT_PRIMARY);
        studentInfoArea.setText(
                "ID: " + student.getId() + "\n" +
                "First Name: " + student.getFirstName() + "\n" +
                "Last Name: " + student.getLastName() + "\n" +
                "Email: " + student.getEmail() + "\n" +
                "GWA: " + student.getGwa()
        );
        studentInfoArea.setCaretPosition(0);
    }

    private void updateStudent() {
        if (foundStudent == null) {
            showError("Search for a student before updating.");
            return;
        }

        String attribute = (String) attributeComboBox.getSelectedItem();
        String newValue = newValueField.getText().trim();

        if (newValue.isEmpty()) {
            showError("Please enter a new value.");
            return;
        }

        try {
            Validator.validateID(idField.getText().trim());

            // Validate the new value according to the field selected by the user.
            switch (Objects.requireNonNull(attribute).toLowerCase()) {
                case "first name", "last name" -> Validator.validateName(newValue);
                case "email" -> Validator.validateEmail(newValue);
                case "gwa" -> Validator.validateGWA(newValue);
                default -> {
                }
            }

            manager.updateStudentInfo(foundStudent.getId(), attribute, newValue);
            showSuccess("Student updated successfully.");
            newValueField.setText("");
            searchStudent();
        } catch (InvalidInputException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error updating student: " + e.getMessage());
        }
    }

    private void clearForm() {
        foundStudent = null;
        idField.setText("");
        newValueField.setText("");
        attributeComboBox.setSelectedIndex(0);
        studentInfoArea.setText("");
        studentInfoArea.setForeground(TEXT_PRIMARY);
        updateButton.setEnabled(false);
        idField.requestFocusInWindow();
    }

    @Override
    public void onPanelShown() {
        clearForm();
    }
}
