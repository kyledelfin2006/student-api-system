package Controller;

import Manager.StudentManager;
import Repository.StudentRepository;
import UI.StudentManagementGUI;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        StudentRepository repository = new StudentRepository("Students.json", new ArrayList<>());
        StudentManager manager = new StudentManager(repository);

        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI gui = new StudentManagementGUI(manager);
            gui.setVisible(true);
        });
    }
}
