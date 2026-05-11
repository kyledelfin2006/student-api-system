package UI;

import Manager.StudentManager;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class StudentManagementGUI extends JFrame {

    private final StudentManager manager;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    private final Map<String, BasePanel> panels = new LinkedHashMap<>();

    public StudentManagementGUI(StudentManager manager) {
        this.manager = manager;
        initializeFrame();
        createPanels();
    }

    private void initializeFrame() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 680));
        setSize(1100, 760);
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
        add(mainPanel);
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
            cardLayout.show(mainPanel, panelName);
        }
    }
}
