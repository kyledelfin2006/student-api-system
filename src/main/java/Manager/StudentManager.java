package Manager;

import Model.Student;
import Repository.StudentRepository;
import Utils.StudentIDGenerator;

import java.io.IOException;
import java.util.List;

public class StudentManager {
    public StudentRepository repository;

    public StudentManager(StudentRepository repository) {
        this.repository = repository;
        loadFromStorage();
    }

    private void loadFromStorage() {
        try {
            repository.loadFromFile();
            List<Student> loadedStudents = repository.getAll();
            int maxId = 0;
            for (Student s : loadedStudents) {
                int idNum = Integer.parseInt(s.getId());
                if (idNum > maxId) {
                    maxId = idNum;
                }
            }
            StudentIDGenerator.setNextId(maxId + 1);
            System.out.println("Loaded " + loadedStudents.size() + " Students from storage.");
        } catch (IOException e) {
            System.out.println("Could not load Students: " + e.getMessage());
        }
    }

    private void saveToStorage() {
        try {
            repository.saveToFile();
        } catch (IOException e) {
            System.out.println("Failed to save students: " + e.getMessage());
        }
    }

    public void createStudent(String email, String gwa, String lastName, String firstName) {
        String generatedId = StudentIDGenerator.generateNextID();
        Student student = new Student(email, generatedId, gwa, lastName, firstName);
        repository.add(student);
        saveToStorage();
    }

    public void deleteStudent(String id) {
        boolean found = false;
        List<Student> studentList = repository.getAll();
        for (Student student : studentList) {
            if (student.getId().equals(id)) {
                found = true;
                repository.remove(student);
                saveToStorage();
                System.out.println("Student " + id + " successfully deleted. ");
                break;
            }
        }
        if (!found) {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    public Student findStudentById(String id) {
        for (Student student : repository.getAll()) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return repository.getAll();
    }

    public void updateStudentInfo(String id, String attribute, String newValue) {
        boolean found = false;
        for (Student student : repository.getAll()) {
            if (student.getId().equals(id)) {
                found = true;
                switch (attribute.toLowerCase().trim()) {
                    case "first name" -> student.setFirstName(newValue);
                    case "last name" -> student.setLastName(newValue);
                    case "gwa" -> student.setGwa(newValue);
                    case "email" -> student.setEmail(newValue);
                    default -> System.out.println("Could Not Find Attribute");
                }
                saveToStorage();
                System.out.println("Student updated.");
                break;
            }
        }
        if (!found) {
            System.out.println("Student ID " + id + " not found.");
        }
    }
}
