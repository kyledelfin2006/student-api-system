import java.io.IOException;
import java.util.List;

public class StudentManager {
    StudentRepository repository; // In-Memory Repository List
    StudentStorage storage; // JSON File Storage



    public StudentManager(StudentRepository repository,StudentStorage storage) {
        this.repository = repository;
        this.storage = storage;
        loadFromStorage();
    }

    // Storage -> Repository
    private void loadFromStorage(){
        try{
            List<Student> loadedStudents = storage.load();



            // Update nextId based on loaded students
            int maxId = 0;
            for (Student s : loadedStudents) {
                int idNum = Integer.parseInt(s.getId());
                if (idNum > maxId) { // the largest loaded ID is the maxID
                    maxId = idNum;
                }
            }


            StudentIDGenerator.setNextId(maxId + 1);

            repository.addAll(loadedStudents);
            System.out.println("Loaded " + loadedStudents.size() + " Students from storage.");
        } catch (IOException e) {
            System.out.println("Could not load Students: "  + e.getMessage());
        }



    }

    // Repository -> Storage
    private void saveToStorage() {
        try {
            List<Student> loaded = repository.getAll();
            storage.save(loaded); // Calls save method of storage
        } catch (IOException e) {
            System.out.println("Failed to save students: " + e.getMessage());
        }
    }


    public void createStudent(String email, String gwa, String lastName, String firstName){

        String generatedId = StudentIDGenerator.generateNextID();
        Student student = new Student(email,generatedId,gwa,lastName,firstName);
        repository.add(student); // add to repository first
        saveToStorage(); // then, add to storage

    }

    public void deleteStudent(String id){
        boolean found = false;
        List<Student> studentList = repository.getAll();

        for (Student student : studentList){
            if (student.getId().equals(id)){
                found = true;

                // try catch for saving and removing
                try{
                    repository.remove(student);
                    saveToStorage();
                } catch (Exception e) {
                    System.out.println("Delete failed: " + e.getMessage());
                }

                System.out.println("Student " + id + " successfully deleted. ");
                break;
            }
        }

        if (!found){
            System.out.println("Student with ID " + id + " not found.");
        }

    }

    public void searchStudent(String id) {
        boolean found = false;
        List<Student> studentList = repository.getAll();

        for (Student student : studentList ){
            if (student.getId().equals(id)){
                found =  true;
                System.out.println(student);
            }
        }


        if (!found){
            System.out.println("Student with ID " + id + " not found.");
        }

    }

    public void displayStudents(){
        for (Student student : repository.getAll()){
            System.out.println(student);
        }
    }

    public void updateStudentInfo(String id, String attribute, String newValue) {
       boolean found = false;
        for (Student student : repository.getAll()){
            if (student.getId().equals(id)){
                found = true;
                switch (attribute.toLowerCase().trim()){
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

        if (!found){
            System.out.println("Student ID " + id + " not found.");
        }


    }



}
