package UI;

import Manager.StudentManager;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StudentManagementGUI extends JFrame {

    private final StudentManager manager;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Map<String, BasePanel> panels = new LinkedHashMap<>();
    private final Map<String, JButton> navigationButtons = new LinkedHashMap<>();
    private final JLabel sectionTitle = new JLabel("Dashboard");

    public StudentManagementGUI(StudentManager manager) {
        this.manager = manager;
        initializeFrame();
        createPanels();
    }

    private void initializeFrame() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1040, 700));
        setSize(1180, 780);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BasePanel.PAGE_BACKGROUND);
    }

    private void createPanels() {
        mainPanel.setBackground(BasePanel.PAGE_BACKGROUND);

        registerPanel("MainMenu", new MainMenuPanel(this));
        registerPanel("CreateStudent", new CreateStudentPanel(this, manager));
        registerPanel("ViewStudents", new ViewStudentsPanel(this, manager));
        registerPanel("UpdateStudent", new UpdateStudentPanel(this, manager));
        registerPanel("DeleteStudent", new DeleteStudentPanel(this, manager));
        registerPanel("SearchStudent", new SearchStudentPanel(this, manager));

        JPanel appShell = new JPanel(new BorderLayout());
        appShell.setBackground(BasePanel.PAGE_BACKGROUND);
        appShell.add(createSidebar(), BorderLayout.WEST);
        appShell.add(createWorkspace(), BorderLayout.CENTER);

        setContentPane(appShell);
        showPanel("MainMenu");
    }

    private void registerPanel(String key, BasePanel panel) {
        panels.put(key, panel);
        mainPanel.add(panel, key);
    }

    public void showPanel(String panelName) {
        BasePanel panel = panels.get(panelName);
        if (panel != null) {
            panel.onPanelShown();
            updateNavigation(panelName);
            cardLayout.show(mainPanel, panelName);
        }
    }

    private JPanel createWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout(0, 0));
        workspace.setBackground(BasePanel.PAGE_BACKGROUND);
        workspace.add(createTopBar(), BorderLayout.NORTH);
        workspace.add(mainPanel, BorderLayout.CENTER);
        return workspace;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BasePanel.PAGE_BACKGROUND);
        topBar.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BasePanel.BORDER_COLOR),
                new EmptyBorder(18, 24, 16, 24)
        ));

        sectionTitle.setFont(BasePanel.TITLE_FONT.deriveFont(Font.BOLD, 20f));
        sectionTitle.setForeground(BasePanel.TEXT_PRIMARY);

        JLabel helperText = new JLabel("Manage student records without leaving the desktop app.");
        helperText.setFont(BasePanel.SUBTITLE_FONT);
        helperText.setForeground(BasePanel.TEXT_MUTED);

        JPanel textPanel = new JPanel(new GridLayout(0, 1, 0, 3));
        textPanel.setOpaque(false);
        textPanel.add(sectionTitle);
        textPanel.add(helperText);
        topBar.add(textPanel, BorderLayout.CENTER);
        return topBar;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(BasePanel.SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(232, 1));
        sidebar.setBorder(new EmptyBorder(24, 18, 20, 18));

        JPanel brand = new JPanel(new BorderLayout(0, 4));
        brand.setOpaque(false);

        JLabel title = new JLabel("Student CRUD");
        title.setFont(BasePanel.APP_TITLE_FONT);
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Records console");
        subtitle.setFont(BasePanel.SUBTITLE_FONT);
        subtitle.setForeground(BasePanel.TEXT_INVERSE_MUTED);

        brand.add(title, BorderLayout.NORTH);
        brand.add(subtitle, BorderLayout.CENTER);

        JPanel navigation = new JPanel(new GridLayout(0, 1, 0, 8));
        navigation.setOpaque(false);
        navigation.setBorder(new EmptyBorder(30, 0, 0, 0));

        addNavigationButton(navigation, "MainMenu", "Dashboard");
        addNavigationButton(navigation, "CreateStudent", "Create Student");
        addNavigationButton(navigation, "ViewStudents", "View Students");
        addNavigationButton(navigation, "SearchStudent", "Search Student");
        addNavigationButton(navigation, "UpdateStudent", "Update Student");
        addNavigationButton(navigation, "DeleteStudent", "Delete Student");

        JButton exitButton = createSidebarButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        sidebar.add(brand, BorderLayout.NORTH);
        sidebar.add(navigation, BorderLayout.CENTER);
        sidebar.add(exitButton, BorderLayout.SOUTH);
        return sidebar;
    }

    private void addNavigationButton(JPanel navigation, String panelName, String label) {
        JButton button = createSidebarButton(label);
        button.addActionListener(e -> showPanel(panelName));
        navigationButtons.put(panelName, button);
        navigation.add(button);
    }

    private JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(BasePanel.BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(11, 13, 11, 13));
        button.setBackground(BasePanel.SIDEBAR_BACKGROUND);
        button.setForeground(BasePanel.TEXT_INVERSE_MUTED);
        button.getModel().addChangeListener(e -> {
            if (!button.isSelected()) {
                button.setBackground(button.getModel().isRollover()
                        ? BasePanel.SIDEBAR_HOVER
                        : BasePanel.SIDEBAR_BACKGROUND);
            }
        });
        return button;
    }

    private void updateNavigation(String panelName) {
        navigationButtons.forEach((key, button) -> {
            boolean selected = key.equals(panelName);
            button.setSelected(selected);
            button.setBackground(selected ? BasePanel.PRIMARY_COLOR : BasePanel.SIDEBAR_BACKGROUND);
            button.setForeground(selected ? Color.WHITE : BasePanel.TEXT_INVERSE_MUTED);
        });
        sectionTitle.setText(switch (panelName) {
            case "CreateStudent" -> "Create Student";
            case "ViewStudents" -> "Student Records";
            case "SearchStudent" -> "Search Student";
            case "UpdateStudent" -> "Update Student";
            case "DeleteStudent" -> "Delete Student";
            default -> "Dashboard";
        });
    }
}
