package UI;

import Manager.StudentManager;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window for the Student Management System.
 * This JFrame serves as the primary container that manages all panels
 * using CardLayout for seamless navigation between different views.
 * Each panel represents a different functionality (Create, View, Update, Delete, Search).
 */
public class StudentManagementGUI extends JFrame {

    /** Shared student manager instance for all data operations across panels */
    private final StudentManager manager;

    /**
     * CardLayout manager for switching between panels.
     * Each panel is treated as a "card" and can be shown by its unique string identifier.
     */
    private CardLayout cardLayout;

    /**
     * Main JPanel container that holds all child panels.
     * Uses CardLayout to display only one panel at a time, keeping others hidden in memory.
     */
    private JPanel mainPanel;

    public StudentManagementGUI(StudentManager manager) {
        this.manager = manager;
        initializeFrame();
        createComponents();
    }

    /**
     * Configures the JFrame window properties.
     * Sets title, default close operation, initial size, and centers the window.
     * Also sets a minimum size to prevent layout distortion.
     */
    private void initializeFrame() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }

    /**
     * Creates and initializes all application panels.
     * Sets up the CardLayout on the main JPanel and adds each functional panel
     * with a unique string identifier. The CardLayout allows switching between
     * panels using these identifiers. The MainMenu panel is displayed by default.
     */
    private void createComponents() {
        // Create CardLayout to manage panel navigation
        cardLayout = new CardLayout();

        // Create main JPanel container with CardLayout
        mainPanel = new JPanel(cardLayout);

        // Add all functional panels with unique identifiers for navigation
        mainPanel.add(new MainMenuPanel(this), "MainMenu");
        mainPanel.add(new CreateStudentPanel(this, manager), "CreateStudent");
        mainPanel.add(new ViewStudentsPanel(this, manager), "ViewStudents");
        mainPanel.add(new UpdateStudentPanel(this, manager), "UpdateStudent");
        mainPanel.add(new DeleteStudentPanel(this, manager), "DeleteStudent");
        mainPanel.add(new SearchStudentPanel(this, manager), "SearchStudent");

        // Add mainPanel to the JFrame and display the MainMenu by default
        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }

    /**
     * Switches the visible panel to the one specified by the panel name.
     * Uses CardLayout to hide the current panel and show the requested one.
     *
     * @param panelName The unique string identifier of the panel to display
     *                  (e.g., "MainMenu", "CreateStudent", "ViewStudents", etc.)
     */
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
}