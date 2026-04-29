package UI;

import Manager.StudentManager;

import javax.swing.*;
import java.awt.*;

public class StudentManagementGUI extends JFrame {
    private final StudentManager manager;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StudentManagementGUI(StudentManager manager) {
        this.manager = manager;
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
    }

    // Method to create all the components with panels
    private void createComponents() {
        cardLayout = new CardLayout();

        mainPanel = new JPanel(cardLayout); // mainPanel incorporating all other panels

        // Add all panels
        mainPanel.add(new MainMenuPanel(this), "MainMenu");
        mainPanel.add(new CreateStudentPanel(this, manager), "CreateStudent");
        mainPanel.add(new ViewStudentsPanel(this, manager), "ViewStudents");
        mainPanel.add(new UpdateStudentPanel(this, manager), "UpdateStudent");
        mainPanel.add(new DeleteStudentPanel(this, manager), "DeleteStudent");
        mainPanel.add(new SearchStudentPanel(this, manager), "SearchStudent");

        add(mainPanel);
        cardLayout.show(mainPanel, "MainMenu");
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
}
