package UI;

import Exceptions.InvalidInputException;
import Manager.StudentManager;
import Model.Student;
import Utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

/**
 * Panel for searching and displaying detailed student information by ID.
 * This panel provides a user-friendly interface that allows users to:
 * - Enter a student ID to search for specific student records
 * - View comprehensive student information in a formatted display
 * - See academic standing evaluations based on GWA
 * - Clear search results and perform new searches
 * - Navigate back to the main menu
 */
public class SearchStudentPanel extends BasePanel {

    /** Reference to the main application frame for navigation between panels */
    private final StudentManagementGUI parentFrame;

    /** Manager instance that handles all student-related business logic and data operations */
    private final StudentManager manager;

    /** Input field where users enter the student ID to search for */
    private JTextField searchField;

    /**
     * Display area for showing search results.
     * Uses monospaced font for aligned, formatted output.
     * Non-editable to prevent user modification of results.
     */
    private JTextArea resultArea;

    /** Scroll pane to allow scrolling through long search results */
    private JScrollPane scrollPane;

    /** Search button that triggers the student lookup operation */
    private JButton searchButton;

    /** Clear button that resets all fields and results */
    private JButton clearButton;

    /** Back button for navigation to the main menu */
    private JButton backButton;

    /**
     * Constructor initializes the search panel with required dependencies.
     * Sets up the StudentManager for data operations and parent frame for navigation.
     *
     * @param parentFrame The main GUI frame that manages panel navigation
     * @param manager The StudentManager instance that handles data operations
     * @throws IllegalArgumentException if parentFrame or manager is null
     */
    public SearchStudentPanel(StudentManagementGUI parentFrame, StudentManager manager) {
        // Validate constructor parameters
        if (parentFrame == null) {
            throw new IllegalArgumentException("Parent frame cannot be null");
        }
        if (manager == null) {
            throw new IllegalArgumentException("Student manager cannot be null");
        }

        this.parentFrame = parentFrame;
        this.manager = manager;

        // Build the complete UI structure
        initialize();
    }

    /**
     * Initializes all UI components and sets up the complete layout structure.
     * Layout hierarchy:
     * - Top Panel: Title and back button
     * - Search Panel: ID input field and results display area
     * - Button Panel: Search and Clear action buttons
     * The layout uses GridBagLayout for flexible component positioning
     * and proper spacing between UI elements.
     */
    private void initialize() {
        // Set the main layout manager for this panel
        setLayout(new GridBagLayout());


        // TOP PANEL - Title and Navigation
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false); // Transparent to show panel background

        // Create and configure the title label
        JLabel titleLabel = createTitleLabel("SEARCH STUDENT");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create and configure the back button
        backButton = createStyledButton("← Back to Menu", new Color(149, 165, 166));
        backButton.setPreferredSize(new Dimension(150, 40));

        // Add action listener for navigation back to main menu
        backButton.addActionListener(e -> {
            clearForm(); // Reset the form state
            parentFrame.showPanel("MainMenu"); // Navigate to main menu
        });

        // Assemble the top panel
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ==========================================
        // SEARCH PANEL - Input and Results
        // ==========================================
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Configure grid constraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- Student ID Input Section ---
        // Label for ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(createLabel("Student ID:"), gbc);

        // Text field for student ID input
        gbc.gridx = 1;
        searchField = createTextField(15);
        // Allow Enter key to trigger search for better UX
        searchField.addActionListener(e -> searchStudent());
        searchPanel.add(searchField, gbc);

        // --- Results Display Section ---
        // Configure constraints for the results area (spans full width)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span across both columns
        gbc.fill = GridBagConstraints.BOTH; // Fill both directions
        gbc.weightx = 1.0; // Allow horizontal stretching
        gbc.weighty = 1.0; // Allow vertical stretching

        // Create the non-editable text area for displaying results
        resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13)); // Monospaced for alignment
        resultArea.setEditable(false); // Prevent user modification
        resultArea.setBackground(new Color(236, 240, 241)); // Light gray-blue background
        resultArea.setLineWrap(true); // Wrap long lines
        resultArea.setWrapStyleWord(true); // Wrap at word boundaries

        // Create compound border for visual appeal
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Add scrolling capability
        scrollPane = new JScrollPane(resultArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchPanel.add(scrollPane, gbc);

        // ==========================================
        // BUTTON PANEL - Action Buttons
        // ==========================================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // Create search button with blue color scheme
        searchButton = createStyledButton("Search", new Color(52, 152, 219));

        // Create clear button with yellow/amber color scheme
        clearButton = createStyledButton("Clear", new Color(241, 196, 15));

        // Add action listeners for button interactions
        // Assertions that it is not null
        assert searchButton != null;
        assert clearButton != null;
        searchButton.addActionListener(e -> searchStudent());
        clearButton.addActionListener(e -> {
            searchField.setText(""); // Clear the ID input
            resultArea.setText(""); // Clear the results display
            searchField.requestFocus(); // Return focus to input field
        });

        // Add buttons to the button panel
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        // ==========================================
        // MAIN LAYOUT ASSEMBLY
        // ==========================================
        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = createGridBagConstraints();
        mainGbc.insets = new Insets(10, 20, 10, 20);

        // Add components in top-to-bottom order
        add(topPanel, mainGbc);           // Top section with title and back button
        mainGbc.gridy = 1;
        add(searchPanel, mainGbc);        // Middle section with search inputs
        mainGbc.gridy = 2;
        mainGbc.insets = new Insets(30, 20, 10, 20);
        add(buttonPanel, mainGbc);        // Bottom section with action buttons
    }

    /**
     * Performs the student search operation with comprehensive validation.
     * Process flow:
     * 1. Retrieves and validates the entered student ID
     * 2. Validates the ID format using the Validator utility
     * 3. Searches for the student in the database
     * 4. Displays results or appropriate error messages
     * 5. Provides color-coded feedback (red for errors, black for success)
     * Error handling:
     * - Empty ID: Shows error message and returns
     * - Invalid format: Shows validation error with specific details
     * - Student not found: Shows "not found" message in red
     * - Unexpected errors: Shows generic error message
     */
    private void searchStudent() {
        // Get and clean the entered student ID
        String id = searchField.getText().trim();

        // Validate that an ID was entered
        if (id.isEmpty()) {
            showError("Please enter a Student ID to search.");
            searchField.requestFocus(); // Return focus to input field
            return;
        }

        // Clear previous results and reset text color
        resultArea.setText("");
        resultArea.setForeground(Color.BLACK);

        try {
            // Validate the student ID format (throws InvalidInputException if invalid)
            Validator.validateID(id);

            // Attempt to find the student in the database
            Student student = manager.findStudentById(id);

            if (student != null) {
                // Student found - display comprehensive information
                displayStudentInfo(student);
                resultArea.setForeground(new Color(0, 100, 0)); // Dark green for success
            } else {
                // Student not found - show friendly message
                resultArea.setText(" No student found with ID: " + id);
                resultArea.setForeground(Color.RED);

                // Add helpful suggestions
                resultArea.append("\n\nSuggestions:");
                resultArea.append("\n• Check if the ID was entered correctly");
                resultArea.append("\n• Try searching with a different ID");
                resultArea.append("\n• View all students to see available IDs");
            }
        } catch (InvalidInputException e) {
            // Handle validation errors with specific feedback
            showError(e.getMessage());
            resultArea.setText(" Error: " + e.getMessage());
            resultArea.setForeground(Color.RED);
        } catch (Exception e) {
            // Handle unexpected errors
            showError("An unexpected error occurred: " + e.getMessage());
            resultArea.setText(" System Error: Unable to complete search");
            resultArea.setForeground(Color.RED);
        }
    }

    /**
     * Displays the complete information of a found student in a formatted layout.
     * The display includes:
     * - Student ID
     * - Full Name (First Name + Last Name)
     * - Email Address
     * - GWA (General Weighted Average)
     * - Academic Standing evaluation
     * Academic standing categories:
     * - Excellent: GWA ≤ 1.50
     * - Good: GWA ≤ 1.75
     * - Satisfactory: GWA ≤ 3.00
     * - Needs Improvement: GWA > 3.00
     * - Unknown: If GWA cannot be parsed
     *
     * @param student The student object containing all information to display
     * @throws NullPointerException if student is null
     */
    private void displayStudentInfo(Student student) {
        // Validate input parameter
        if (student == null) {
            throw new NullPointerException("Cannot display information for null student");
        }

        // Reset text color and clear previous content
        resultArea.setForeground(Color.BLACK);
        resultArea.setText(""); // Clear any previous content

        // ==========================================
        // Student Header Section
        // ==========================================

        // Create a formatted box with student details using Unicode box characters
        resultArea.append("╔════════════════════════════════════════════╗\n");
        resultArea.append(String.format("║ %-42s \n", "CURRENT STUDENT INFORMATION"));
        resultArea.append("╠════════════════════════════════════════════╣\n");
        resultArea.append(String.format("║ ID:         %-30s \n", student.getId()));
        resultArea.append(String.format("║ First Name: %-30s \n", student.getFirstName()));
        resultArea.append(String.format("║ Last Name:  %-30s \n", student.getLastName()));
        resultArea.append(String.format("║ Email:      %-30s \n", student.getEmail()));
        resultArea.append(String.format("║ GWA:        %-30s \n", student.getGwa()));
        String gwaString = student.getGwa();

        // Calculate and display academic standing
        String academicStanding = calculateAcademicStanding(gwaString);
        resultArea.append(String.format("║ Standing:  %-30s \n", academicStanding));
        resultArea.append("╚═══════════════════════════════════════════");



    }

    private String calculateAcademicStanding(String gwaString) {
        try {
            // Parse the GWA string to a double for comparison
            double gwa = Double.parseDouble(gwaString);

            // Determine academic standing based on GWA ranges
            if (gwa >= 1.00 && gwa <= 1.50) {
                return " Excellent (1.00-1.50)";
            } else if (gwa > 1.50 && gwa <= 1.75) {
                return " Very Good (1.51-1.75)";
            } else if (gwa > 1.75 && gwa <= 2.50) {
                return " Good (1.76-2.50)";
            } else if (gwa > 2.50 && gwa <= 3.00) {
                return "✓ Satisfactory (2.51-3.00)";
            } else if (gwa > 3.00 && gwa <= 5.00) {
                return " Needs Improvement (3.01-5.00)";
            } else {
                return " Invalid GWA Range";
            }
        } catch (NumberFormatException e) {
            // Handle case where GWA cannot be parsed as a number
            return " Unable to determine (Invalid GWA format)";
        }
    }

    /**
     * Clears all form fields and resets the panel to its initial state.

     * This method:
     * - Clears the search input field
     * - Clears the results display area
     * - Resets text color to default
     * - Returns focus to the search field for convenience

     * Called when:
     * - User clicks the Clear button
     * - User navigates back to this panel
     * - User navigates away from this panel
     */
    private void clearForm() {
        // Clear all text fields and areas
        searchField.setText("");
        resultArea.setText("");

        // Reset text color to default black
        resultArea.setForeground(Color.BLACK);

        // Return focus to the search field for immediate typing
        searchField.requestFocus();
    }

    /**
     * Called automatically when this panel becomes visible.
     * Ensures the form is in a clean state each time it's shown.
     * This method implements the abstract method from BasePanel
     * and is invoked by the panel switching mechanism.
     * The clear ensures that:
     * - Previous search results don't persist between navigations
     * - The input field is ready for new searches
     * - No stale data is displayed
     */
    @Override
    public void onPanelShown() {
        clearForm(); // Reset entire form to initial state

        // Additional initialization when panel is shown
        searchField.requestFocus(); // Set focus to input field
        resultArea.setForeground(Color.BLACK); // Reset text color
    }
}