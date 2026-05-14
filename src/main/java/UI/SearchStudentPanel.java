package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;

public class SearchStudentPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;
    private final StudentManager manager;
    private JTextField searchField;
    private JTextArea resultArea;

    public SearchStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    private void initialize() {
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(800, 520));
        card.add(createHeader("Search Student", "Look up a saved student by their generated ID."), BorderLayout.NORTH);

        JPanel formPanel = createFormGrid();
        searchField = createTextField(18);
        searchField.addActionListener(e -> searchStudent());

        JPanel searchFieldRow = createSectionPanel(new BorderLayout(10, 0));
        searchFieldRow.add(searchField, BorderLayout.CENTER);
        JButton searchButton = createPrimaryButton("Search");
        searchButton.addActionListener(e -> searchStudent());
        searchFieldRow.add(searchButton, BorderLayout.EAST);
        addFormRow(formPanel, 0, "Student ID", searchFieldRow);

        resultArea = createTextArea(12, 42);
        resultArea.setFont(STUDENT_INFO_FONT.deriveFont(Font.PLAIN, 17f));
        JScrollPane resultScrollPane = createScrollPane(resultArea);
        resultScrollPane.setPreferredSize(new Dimension(100, 280));
        addFormRow(formPanel, 1, "Result", resultScrollPane);

        JPanel buttonRow = createButtonRow();
        JButton clearButton = createTertiaryButton("Clear");
        JButton backButton = createSecondaryButton("Dashboard");
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> {
            clearForm();
            parentFrame.showPanel("MainMenu");
        });
        buttonRow.add(clearButton);
        buttonRow.add(backButton);

        JPanel body = createSectionPanel(new BorderLayout(0, 16));
        body.add(formPanel, BorderLayout.CENTER);
        body.add(buttonRow, BorderLayout.SOUTH);

        card.add(body, BorderLayout.CENTER);
        add(wrapInPage(card, 900), BorderLayout.CENTER);
    }

    private void searchStudent() {
        String id = searchField.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter a Student ID.");
            searchField.requestFocusInWindow();
            return;
        }

        try {
            Validator.validateID(id);
            Student student = manager.findStudentById(id);

            if (student == null) {
                resultArea.setForeground(DANGER_COLOR);
                resultArea.setText("No student found with ID: " + id);
                return;
            }

            resultArea.setForeground(TEXT_PRIMARY);
            resultArea.setText(
                    "ID: " + student.getId() + "\n" +
                    "First Name: " + student.getFirstName() + "\n" +
                    "Last Name: " + student.getLastName() + "\n" +
                    "Email: " + student.getEmail() + "\n" +
                    "GWA: " + student.getGwa()
            );
            resultArea.setCaretPosition(0);
        } catch (InvalidInputException e) {
            resultArea.setForeground(DANGER_COLOR);
            resultArea.setText("Error: " + e.getMessage());
            showError(e.getMessage());
        } catch (Exception e) {
            resultArea.setForeground(DANGER_COLOR);
            resultArea.setText("System error: Unable to complete search.");
            showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void clearForm() {
        searchField.setText("");
        resultArea.setText("");
        resultArea.setForeground(TEXT_PRIMARY);
        searchField.requestFocusInWindow();
    }

    @Override
    public void onPanelShown() {
        clearForm();
    }
}
