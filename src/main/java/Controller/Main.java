package Controller;

import Manager.StudentManager;
import Repository.StudentRepository;
import Storage.StudentStorage;
import UI.StudentManagementGUI;

import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        // UIManager is a class that manages the look and feel (visual appearance) of Swing components.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize backend components
        StudentRepository repository = new StudentRepository(new ArrayList<>());
        StudentStorage storage = new StudentStorage("Students.json");
        StudentManager manager = new StudentManager(repository, storage);

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI gui = new StudentManagementGUI(manager);
            gui.setVisible(true);
        });
    }
}