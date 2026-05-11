package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Panel for updating student information in the Student Management System.
 * This panel allows users to:
 * 1. Search for a student by ID to view current information
 * 2. Select which attribute to update (First Name, Last Name, Email, or GWA)
 * 3. Enter the new value for the selected attribute
 * 4. Submit the update after validation

 * The panel follows a two-step process: first search to verify the student exists,
 * then update to modify the selected attribute.
 *
 */
public class UpdateStudentPanel extends BasePanel {

    // Reference to the main application frame for navigation between panels
    private final StudentManagementGUI parentFrame;

    // Manager instance that handles all student-related business logic and data operations
    private final StudentManager manager;

    // Input field for entering the student ID to search
    private JTextField idField;

    // Input field for entering the new value to update
    private JTextField newValueField;

    // Dropdown menu for selecting which student attribute to modify
    private JComboBox<String> attributeComboBox;

    // Non-editable text area that displays the found student's current information
    private JTextArea studentInfoArea;

    // Button that triggers the update operation (initially disabled until student is found)
    private JButton updateButton;

    // Button to search for a student by ID
    private JButton searchButton;

    // Stores the currently found student object; null if no valid student is found
    private Student foundStudent;

    /**
     * Constructor initializes the update panel with required dependencies.
     * Sets up all UI components and initial state.
     *
     * @param parentFrame The main GUI frame for panel navigation
     * @param manager The student manager for database operations
     */
    public UpdateStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        this.parentFrame = parentFrame;
        this.manager = manager;
        this.foundStudent = null; // Initialize with no student found
        initialize(); // Build the UI
    }

    /**
     * Initializes all UI components and sets up the complete layout.
     * Creates a two-section layout:
     * 1. Search section with ID input and student information display
     * 2. Update section with attribute selection and new value input
     */
    private void initialize() {
        // Use GridBagLayout for flexible, responsive component positioning
        setLayout(new GridBagLayout());

        // ---- TITLE SECTION ----
        JLabel titleLabel = createTitleLabel("UPDATE STUDENT INFORMATION");

        // ---- MAIN FORM PANEL ----
        // Contains all input fields and displays arranged in a grid
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent to show background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // GridBagConstraints controls the positioning and sizing of components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Components stretch horizontally
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        // ---- STUDENT ID INPUT SECTION ----
        // Label for student ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createLabel("Student ID:"), gbc);

        // Text field for entering student ID
        gbc.gridx = 1;
        idField = createTextField(15);
        // Add action listener to allow Enter key to trigger search
        idField.addActionListener(e -> searchStudent());
        formPanel.add(idField, gbc);

        // Search button to find and display student information
        gbc.gridx = 2;
        searchButton = createStyledButton("Search", new Color(52, 152, 219));
        searchButton.addActionListener(e -> searchStudent());
        formPanel.add(searchButton, gbc);

        // ---- STUDENT INFORMATION DISPLAY SECTION ----
        // Shows current student data after successful search
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Span across all three columns
        gbc.fill = GridBagConstraints.BOTH; // Fill both horizontally and vertically
        gbc.weightx = 1.0; // Allow horizontal stretching
        gbc.weighty = 1.0; // Allow vertical stretching
        gbc.insets = new Insets(20, 10, 10, 10);

        // Create read-only text area for displaying student info
        studentInfoArea = new JTextArea(8, 30);
        studentInfoArea.setEditable(false); // User cannot modify displayed info
        studentInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        studentInfoArea.setBorder(BorderFactory.createTitledBorder("Current Student Information"));
        studentInfoArea.setBackground(new Color(245, 245, 245)); // Light gray background

        // Add scrolling capability for long student information
        JScrollPane scrollPane = new JScrollPane(studentInfoArea);
        formPanel.add(scrollPane, gbc);

        // Reset grid constraints for subsequent components
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ---- ATTRIBUTE SELECTION SECTION ----
        // Dropdown for choosing which field to update
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        formPanel.add(createLabel("Attribute to Update:"), gbc);

        gbc.gridx = 1;
        // Define available attributes that can be modified
        String[] attributes = {"First Name", "Last Name", "Email", "GWA"};
        attributeComboBox = new JComboBox<>(attributes);
        attributeComboBox.setFont(LABEL_FONT);
        attributeComboBox.setBackground(Color.WHITE);
        formPanel.add(attributeComboBox, gbc);

        // ---- NEW VALUE INPUT SECTION ----
        // Field for entering the replacement value
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createLabel("New Value:"), gbc);

        gbc.gridx = 1;
        newValueField = createTextField(20);
        formPanel.add(newValueField, gbc);

        // ---- BUTTON PANEL SECTION ----
        // Contains action buttons for update and navigation
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // Update button - initially disabled until student is found and validated
        updateButton = createStyledButton("Update Student", new Color(46, 204, 113));
        updateButton.setEnabled(false); // Disabled by default for safety

        // Back button returns to main menu, clearing all fields
        JButton backButton = createStyledButton("Back to Menu", new Color(149, 165, 166));

        // Clear button resets all fields in the form
        JButton clearButton = createStyledButton("Clear Form", new Color(241, 196, 15));

        // Add action listeners for button functionality
        updateButton.addActionListener(e -> updateStudent());
        backButton.addActionListener(e -> {
            clearForm(); // Reset all fields when leaving
            parentFrame.showPanel("MainMenu");
        });
        clearButton.addActionListener(e -> clearForm());

        // Add buttons to the button panel
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        // ---- MAIN LAYOUT ASSEMBLY ----
        // Apply the complete layout structure to the panel
        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = createGridBagConstraints();
        mainGbc.insets = new Insets(10, 20, 10, 20);

        // Add components in order from top to bottom
        add(titleLabel, mainGbc);           // Row 0: Title
        mainGbc.gridy = 1;
        add(formPanel, mainGbc);            // Row 1: Main form with all inputs
        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(30, 20, 10, 20);
        add(buttonPanel, mainGbc);          // Row 2: Action buttons
    }

    /**
     * Searches for a student by the entered ID and displays their current information.
     *
     * This method performs the following steps:
     * 1. Validates that the ID field is not empty
     * 2. Validates the ID format using the Validator utility
     * 3. Searches for the student in the database
     * 4. Displays the student's information if found
     * 5. Enables the update button for valid students
     * 6. Shows appropriate error messages for invalid cases
     */
    private void searchStudent() {
        // Get and clean the entered student ID
        String id = idField.getText().trim();

        // Check if the ID field is empty
        if (id.isEmpty()) {
            showError("Please enter a Student ID to search.");
            return;
        }

        try {
            // Validate the ID format (e.g., correct length, alphanumeric, etc.)
            Validator.validateID(id);

            // Search for the student in the data store
            foundStudent = manager.findStudentById(id);

            // Handle case where no student exists with the given ID
            if (foundStudent == null) {
                studentInfoArea.setText("No student found with ID: " + id);
                updateButton.setEnabled(false); // Disable update for non-existent student
                foundStudent = null;
                return;
            }

            // Display the found student's complete information
            displayStudentInfo(foundStudent);

            // Enable the update button now that we have a valid student
            updateButton.setEnabled(true);

        } catch (InvalidInputException e) {
            // Handle invalid ID format
            showError(e.getMessage());
            updateButton.setEnabled(false);
            foundStudent = null;
            studentInfoArea.setText(""); // Clear any previous results
        }
    }

    /**
     * Formats and displays the complete information of the found student.
     * Uses a box-drawing format for clear visual presentation.
     *
     * @param student The student object whose information should be displayed
     */
    private void displayStudentInfo(Student student) {
        // Use StringBuilder for efficient string construction
        StringBuilder info = new StringBuilder();

        // Create a formatted box with student details using Unicode box characters
        info.append(String.format("║ %-42s \n", "CURRENT STUDENT INFORMATION"));
        info.append("╠════════════════════════════════════════════╣\n");
        info.append(String.format("║ ID:         %-30s \n", student.getId()));
        info.append(String.format("║ First Name: %-30s \n", student.getFirstName()));
        info.append(String.format("║ Last Name:  %-30s \n", student.getLastName()));
        info.append(String.format("║ Email:      %-30s \n", student.getEmail()));
        info.append(String.format("║ GWA:        %-30s \n", student.getGwa()));
        info.append("╠════════════════════════════════════════════╣");

        // Add a hint about the update process
        info.append("\nSelect an attribute above and enter the new value to update.");

        // Set the formatted text in the display area
        studentInfoArea.setText(info.toString());
        studentInfoArea.setCaretPosition(0); // Scroll to top
    }

    /**
     * Validates and processes the student update request.
     * This method performs several validation checks:
     * 1. Ensures a student has been found first
     * 2. Validates the student ID
     * 3. Validates the new value based on the selected attribute type
     * 4. Applies the update through the manager
     * 5. Shows success or error feedback to the user
     */
    private void updateStudent() {
        // Safety check - ensure we have a student to update
        if (foundStudent == null) {
            showError("Please search for a student first before updating.");
            return;
        }

        // Get all required input values
        String id = idField.getText().trim();
        String attribute = (String) attributeComboBox.getSelectedItem();
        String newValue = newValueField.getText().trim();

        // Check if new value is empty
        if (newValue.isEmpty()) {
            showError("Please enter a new value for the selected attribute.");
            return;
        }

        try {
            // Validate the student ID format
            Validator.validateID(id);

            // Perform attribute-specific validation on the new value
            // This ensures the new value matches the expected format for the chosen attribute
            switch (Objects.requireNonNull(attribute).toLowerCase()) {
                case "first name", "last name" -> {
                    // Names should only contain letters and be of reasonable length
                    Validator.validateName(newValue);
                }
                case "email" -> {
                    // Email must follow standard email format (e.g., user@domain.com)
                    Validator.validateEmail(newValue);
                }
                case "gwa" -> {
                    // GWA must be a number between 1.0 and 5.0
                    Validator.validateGWA(newValue);
                }
            }

            // Perform the actual update operation through the manager
            manager.updateStudentInfo(id, attribute, newValue);

            // Show success message to the user
            showSuccess("Student updated successfully!");

            // Refresh the display to show updated information
            searchStudent();

            // Clear the new value field for the next update if needed
            newValueField.setText("");

        } catch (InvalidInputException e) {
            // Handle validation errors (invalid format, etc.)
            showError(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors (database issues, etc.)
            showError("Error updating student: " + e.getMessage());
        }
    }

    /**
     * Clears all form fields and resets the panel to its initial state.
     * This method:
     * - Clears the ID input field
     * - Clears the new value input field
     * - Clears the student information display
     * - Disables the update button
     * - Resets the found student to null
     * - Resets the attribute combo box to first option
     */
    private void clearForm() {
        idField.setText("");              // Clear ID input
        newValueField.setText("");        // Clear new value input
        studentInfoArea.setText("");      // Clear student info display
        updateButton.setEnabled(false);   // Disable update button
        foundStudent = null;              // Reset stored student
        attributeComboBox.setSelectedIndex(0); // Reset to first attribute

        // Set focus back to ID field for convenience
        idField.requestFocus();
    }

    /**
     * Called automatically when this panel becomes visible.
     * Ensures the form is in a clean state each time it's shown.
     * Implements the abstract method from BasePanel.
     */
    @Override
    public void onPanelShown() {
        clearForm(); // Reset everything when panel is displayed
    }
}