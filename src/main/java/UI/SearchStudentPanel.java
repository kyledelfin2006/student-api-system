package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for searching and displaying student information by ID.
 * Allows users to enter a student ID and view the corresponding student details.
 */
public class SearchStudentPanel extends BasePanel {

    // Parent frame reference for panel navigation
    private final StudentManagementGUI parentFrame;

    // Manager instance for student database operations
    private final StudentManager manager;

    // Input field for student ID
    private JTextField searchField;

    // Display area for search results
    private JTextArea resultArea;

    /**
     * Constructor initializes the search panel with required dependencies.
     * @param parentFrame The main GUI frame for navigation
     * @param manager The student manager for database operations
     */
    public SearchStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        initialize();
    }

    /**
     * Initializes all UI components and sets up the layout.
     */
    private void initialize() {
        setLayout(new GridBagLayout());

        // Top Panel with Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // Create and add title label
        JLabel titleLabel = createTitleLabel("SEARCH STUDENT");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add back button at top
        JButton backButton = createStyledButton("← Back to Menu", new Color(149, 165, 166));
        backButton.setPreferredSize(new Dimension(150, 40));
        backButton.addActionListener(e -> {
            searchField.setText("");
            resultArea.setText("");
            parentFrame.showPanel("MainMenu");
        });

        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Create search panel with input field and results area
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Search input section
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(createLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        searchField = createTextField(15);
        searchField.addActionListener(e -> searchStudent()); // Press Enter to search
        searchPanel.add(searchField, gbc);

        // Results display section with scrollable text area
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        resultArea = new JTextArea(15, 50); // Actual size of the Result Area
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(236, 240, 241));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        searchPanel.add(scrollPane, gbc);

        // Button panel for actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton searchButton = createStyledButton("Search", new Color(52, 152, 219));
        JButton clearButton = createStyledButton("Clear", new Color(241, 196, 15));

        // Add action listeners for buttons
        searchButton.addActionListener(e -> searchStudent());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            resultArea.setText("");
            searchField.requestFocus();
        });

        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        // Apply main layout
        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = createGridBagConstraints();
        mainGbc.insets = new Insets(10, 20, 10, 20);

        add(topPanel, mainGbc);
        mainGbc.gridy = 1;
        add(searchPanel, mainGbc);
        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(30, 20, 10, 20);
        add(buttonPanel, mainGbc);
    }

    /**
     * Performs the student search operation.
     * Validates the input ID, searches for the student, and displays results.
     */
    private void searchStudent() {
        String id = searchField.getText().trim();

        if (id.isEmpty()) {
            showError("Please enter a Student ID to search.");
            return;
        }

        resultArea.setText("");
        resultArea.setForeground(Color.BLACK);

        try {
            // Validate the student ID format
            Validator.validateID(id);

            // Search for student in database
            Student student = manager.findStudentById(id);

            if (student != null) {
                displayStudentInfo(student);
            } else {
                // Display not found message
                resultArea.setText("No student found with ID: " + id);
                resultArea.setForeground(Color.RED);
            }
        } catch (InvalidInputException e) {
            // Show validation error
            showError(e.getMessage());
            resultArea.setText("Error: " + e.getMessage());
            resultArea.setForeground(Color.RED);
        }
    }

    /**
     * Displays the complete information of a found student.
     * @param student The student object to display
     */
    private void displayStudentInfo(Student student) {
        resultArea.setForeground(Color.BLACK);
        resultArea.setText(""); // Use setText instead of append to avoid duplication

        resultArea.append("Student Found:\n\n");
        resultArea.append("=========================================\n");
        resultArea.append(String.format("Student ID:    %s\n", student.getId()));
        resultArea.append(String.format("Full Name:     %s %s\n",
                student.getFirstName(), student.getLastName()));
        resultArea.append(String.format("Email Address: %s\n", student.getEmail()));

        // FIXED: GWA is a String, so display it directly
        String gwaString = student.getGwa();
        resultArea.append(String.format("GWA:           %s\n", gwaString));
        resultArea.append("=========================================\n");

        // Display academic standing based on GWA (parse String to double for comparison)
        try {
            double gwa = Double.parseDouble(gwaString);
            if (gwa <= 1.5) {
                resultArea.append("\n[Excellent] Academic Standing: Excellent");
            } else if (gwa <= 2.5) {
                resultArea.append("\n[Good] Academic Standing: Good");
            } else if (gwa <= 3.0) {
                resultArea.append("\n[Satisfactory] Academic Standing: Satisfactory");
            } else {
                resultArea.append("\n[Needs Improvement] Academic Standing: Needs Improvement");
            }
        } catch (NumberFormatException e) {
            resultArea.append("\n[Unknown] Academic Standing: Unable to determine");
        }
    }
}