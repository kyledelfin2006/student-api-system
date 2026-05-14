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
        card.setPreferredSize(new Dimension(720, 470));

        JPanel header = createHeader(
                "Dashboard",
                "Choose a task from here or use the left navigation."
        );

        JPanel summaryGrid = createSectionPanel(new GridLayout(1, 3, 12, 0));
        summaryGrid.add(createStatTile("Start a new record", "Create"));
        summaryGrid.add(createStatTile("Review the full list", "View"));
        summaryGrid.add(createStatTile("Find by student ID", "Search"));

        JPanel buttonGrid = createSectionPanel(new GridLayout(2, 3, 12, 12));
        buttonGrid.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JButton createButton = createPrimaryButton("Create");
        JButton viewButton = createTertiaryButton("View");
        JButton searchButton = createTertiaryButton("Search");
        JButton updateButton = createSecondaryButton("Update");
        JButton deleteButton = createDangerButton("Delete");
        JButton exitButton = createWarningButton("Exit");

        createButton.addActionListener(e -> parentFrame.showPanel("CreateStudent"));
        viewButton.addActionListener(e -> parentFrame.showPanel("ViewStudents"));
        searchButton.addActionListener(e -> parentFrame.showPanel("SearchStudent"));
        updateButton.addActionListener(e -> parentFrame.showPanel("UpdateStudent"));
        deleteButton.addActionListener(e -> parentFrame.showPanel("DeleteStudent"));
        exitButton.addActionListener(e -> System.exit(0));

        buttonGrid.add(createButton);
        buttonGrid.add(viewButton);
        buttonGrid.add(searchButton);
        buttonGrid.add(updateButton);
        buttonGrid.add(deleteButton);
        buttonGrid.add(exitButton);

        JPanel content = createSectionPanel(new BorderLayout(0, 18));
        content.add(summaryGrid, BorderLayout.NORTH);
        content.add(buttonGrid, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        add(wrapInPage(card, 780), BorderLayout.CENTER);
    }

    @Override
    public void onPanelShown() {
    }
}
