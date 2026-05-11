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
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(780, 500));
        card.add(createHeader("Delete Student", null), BorderLayout.NORTH);

        JPanel formPanel = createFormGrid();
        idField = createTextField(18);
        idField.addActionListener(e -> searchStudent());

        JPanel searchFieldRow = createSectionPanel(new BorderLayout(10, 0));
        searchFieldRow.add(idField, BorderLayout.CENTER);
        JButton searchButton = createPrimaryButton("Find");
        searchButton.addActionListener(e -> searchStudent());
        searchFieldRow.add(searchButton, BorderLayout.EAST);
        addFormRow(formPanel, 0, "Student ID", searchFieldRow);

        studentInfoArea = createTextArea(9, 40);
        JScrollPane infoScrollPane = createScrollPane(studentInfoArea);
        infoScrollPane.setPreferredSize(new Dimension(100, 220));
        addFormRow(formPanel, 1, "Student Info", infoScrollPane);

        JPanel buttonRow = createButtonRow();
        deleteButton = createDangerButton("Delete Student");
        deleteButton.setEnabled(false);
        JButton backButton = createSecondaryButton("Back to Menu");

        deleteButton.addActionListener(e -> deleteStudent());
        backButton.addActionListener(e -> {
            clearForm();
            parentFrame.showPanel("MainMenu");
        });

        buttonRow.add(deleteButton);
        buttonRow.add(backButton);

        JPanel body = createSectionPanel(new BorderLayout(0, 16));
        body.add(formPanel, BorderLayout.CENTER);
        body.add(buttonRow, BorderLayout.SOUTH);

        card.add(body, BorderLayout.CENTER);
        add(wrapInPage(card, 880), BorderLayout.CENTER);
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
                deleteButton.setEnabled(false);
                return;
            }

            studentInfoArea.setForeground(TEXT_PRIMARY);
            studentInfoArea.setText(
                    "ID: " + foundStudent.getId() + "\n" +
                    "First Name: " + foundStudent.getFirstName() + "\n" +
                    "Last Name: " + foundStudent.getLastName() + "\n" +
                    "Email: " + foundStudent.getEmail() + "\n" +
                    "GWA: " + foundStudent.getGwa()
            );
            studentInfoArea.setCaretPosition(0);
            deleteButton.setEnabled(true);
        } catch (InvalidInputException e) {
            foundStudent = null;
            deleteButton.setEnabled(false);
            studentInfoArea.setText("");
            studentInfoArea.setForeground(TEXT_PRIMARY);
            showError(e.getMessage());
        }
    }

    private void deleteStudent() {
        if (foundStudent == null) {
            showError("Search for a student before deleting.");
            return;
        }

        // Confirm destructive actions so accidental clicks do not remove a record.
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete student " + foundStudent.getFirstName() + " " + foundStudent.getLastName() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                manager.deleteStudent(foundStudent.getId());
                showSuccess("Student deleted successfully.");
                clearForm();
            } catch (Exception e) {
                showError("Error deleting student: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        foundStudent = null;
        idField.setText("");
        studentInfoArea.setText("");
        studentInfoArea.setForeground(TEXT_PRIMARY);
        deleteButton.setEnabled(false);
        idField.requestFocusInWindow();
    }

    @Override
    public void onPanelShown() {
        clearForm();
    }
}
