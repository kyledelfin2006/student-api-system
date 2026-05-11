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
        setLayout(new BorderLayout());

        JPanel card = createCardPanel();
        card.setPreferredSize(new Dimension(640, 420));

        JPanel header = createHeader(
                "Student Management System",
                null
        );

        // Each JButton is different in design, all with ActionListeners

        // Grid Format
        JPanel buttonGrid = createSectionPanel(new GridLayout(3, 2, 10, 10));

        // Set Border
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        // Instantiate Buttons
        JButton createButton = createPrimaryButton("Create Student");
        JButton viewButton = createTertiaryButton("View Students");
        JButton updateButton = createSecondaryButton("Update Student");
        JButton deleteButton = createDangerButton("Delete Student");
        JButton searchButton = createTertiaryButton("Search Student");
        JButton exitButton = createWarningButton("Exit");

        // Create Action Listeners
        createButton.addActionListener(e -> parentFrame.showPanel("CreateStudent"));
        viewButton.addActionListener(e -> parentFrame.showPanel("ViewStudents"));
        updateButton.addActionListener(e -> parentFrame.showPanel("UpdateStudent"));
        deleteButton.addActionListener(e -> parentFrame.showPanel("DeleteStudent"));
        searchButton.addActionListener(e -> parentFrame.showPanel("SearchStudent"));
        exitButton.addActionListener(e -> System.exit(0));

        // Add Each Button to the Button Grid

        buttonGrid.add(createButton);
        buttonGrid.add(viewButton);
        buttonGrid.add(updateButton);
        buttonGrid.add(deleteButton);
        buttonGrid.add(searchButton);
        buttonGrid.add(exitButton);

        JPanel content = createSectionPanel(new BorderLayout(0, 14));
        content.add(buttonGrid, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        add(wrapInPage(card, 680), BorderLayout.CENTER);
    }

    @Override
    public void onPanelShown() {
        // The main menu is static, so it does not need to refresh when revisited.
    }
}
