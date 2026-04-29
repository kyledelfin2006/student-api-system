package UI;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends BasePanel {
    private final StudentManagementGUI parentFrame;

    public MainMenuPanel(StudentManagementGUI parentFrame) {
        this.parentFrame = parentFrame;
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());

        // Title
        JLabel titleLabel = createTitleLabel("STUDENT MANAGEMENT SYSTEM");

        // Button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = createGridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JButton createButton = createStyledButton("Create Student", new Color(46, 204, 113));
        JButton viewButton = createStyledButton("View All Students", new Color(52, 152, 219));
        JButton updateButton = createStyledButton("Update Student", new Color(241, 196, 15));
        JButton deleteButton = createStyledButton("Delete Student", new Color(231, 76, 60));
        JButton searchButton = createStyledButton("Search Student", new Color(155, 89, 182));
        JButton exitButton = createStyledButton("Exit", new Color(149, 165, 166));

        buttonPanel.add(createButton, gbc);
        buttonPanel.add(viewButton, gbc);
        buttonPanel.add(updateButton, gbc);
        buttonPanel.add(deleteButton, gbc);
        buttonPanel.add(searchButton, gbc);
        buttonPanel.add(exitButton, gbc);

        // Add action listeners
        createButton.addActionListener(e -> parentFrame.showPanel("CreateStudent"));

        // FIXED: Don't try to cast or access panel directly - just show the panel
        // The ViewStudentsPanel will refresh its data when it becomes visible
        viewButton.addActionListener(e -> {
            parentFrame.showPanel("ViewStudents");
        });

        updateButton.addActionListener(e -> parentFrame.showPanel("UpdateStudent"));
        deleteButton.addActionListener(e -> parentFrame.showPanel("DeleteStudent"));
        searchButton.addActionListener(e -> parentFrame.showPanel("SearchStudent"));
        exitButton.addActionListener(e -> System.exit(0));

        // Layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = createGridBagConstraints();
        mainGbc.insets = new Insets(20, 20, 20, 20);

        add(titleLabel, mainGbc);
        mainGbc.gridy = 1;
        mainGbc.insets = new Insets(40, 20, 20, 20);
        add(buttonPanel, mainGbc);
    }
}