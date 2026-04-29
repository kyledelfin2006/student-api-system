package api.manager;

import api.data.Student;
import api.data.StudentInput;
import api.exceptions.InvalidInputException;
import api.repository.StudentRepository;
import api.storage.StudentStorage;
import api.util.StudentIDGenerator;
import api.util.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentManager {
    private final StudentRepository repository; // In-Memory Repository List
    private final StudentStorage storage;   // JSON File Storage
    private final HashMap<String, Student> idIndex = new HashMap<>();

    public StudentManager(StudentRepository repository, StudentStorage storage) {
        this.repository = repository;
        this.storage = storage;
        loadFromStorage();
        resetIndex();
    }

    // Storage -> Repository
    private void loadFromStorage() {
        try {
            List<Student> loadedStudents = storage.load();

            // Start fresh
            repository.clear();

            int maxId = 0;
            for (Student s : loadedStudents) {
                int idNum = Integer.parseInt(s.getId());
                if (idNum > maxId) maxId = idNum;
            }

            StudentIDGenerator.setNextId(maxId + 1);
            repository.addAll(loadedStudents);

            System.out.println("Loaded " + loadedStudents.size() + " Students from storage.");

        } catch (IOException e) {
            System.out.println("Could not load Students: " + e.getMessage());
            repository.clear(); // Ensure clean state on failure
        }
        resetIndex(); // Rebuild HashMap index after repository is populated
    }

    // Repository -> Storage
    private void saveToStorage() {
        try {
            List<Student> loaded = repository.getAll();
            storage.save(loaded);
        } catch (IOException e) {
            System.out.println("Failed to save students: " + e.getMessage());
        }
    }

    private void resetIndex() {
        idIndex.clear();
        int count = 0;
        for (Student student : repository.getAll()) {
            idIndex.put(student.getId(), student); // Add ID, Object
            count++;
        }
        System.out.println("Index rebuilt with " + count + " students");
    }

    // GET - "/api/students/"
    public List<Student> getAllStudents() {
        return new ArrayList<Student>(repository.getAll());
    }

    // GET - "/api/students/{id}"
    public Student findStudent(String id) {
        return idIndex.get(id);
    }

    private boolean isEmailUnique(String email) {
        return repository.getAll().stream()
                .noneMatch(s -> s.getEmail().equalsIgnoreCase(email));
    }

    // POST - "/api/students/"
    public Student createStudent(StudentInput studentInput) {
        if (!isEmailUnique(studentInput.getEmail())){
            throw new InvalidInputException("Email already exists: " + studentInput.getEmail());
        }

        Student newStudent = new Student(
                studentInput.getFirstName(),
                studentInput.getLastName(),
                studentInput.getCourse(),
                studentInput.getYearLevel(),
                studentInput.getGwa(),
                studentInput.getEmail()
        );

        repository.add(newStudent);
        saveToStorage();
        resetIndex(); // Update index with new student
        return newStudent;
    }

    // DELETE - "/api/students/{id}"
    public boolean deleteStudent(String id) {
        Student toRemove = findStudent(id);
        if (toRemove != null) {
            repository.remove(toRemove);
            saveToStorage();
            resetIndex(); // Rebuild index after deletion
            return true;
        }
        return false;
    }

    // PATCH - "/api/students/{id}"
    public Student patchStudent(String id, StudentInput updates) {
        Student existing = findStudent(id);
        if (existing == null) return null;

        if (updates.getFirstName() != null) existing.setFirstName(Validator.validateName(updates.getFirstName()));
        if (updates.getLastName() != null) existing.setLastName(Validator.validateName(updates.getLastName()));
        if (updates.getCourse() != null) existing.setCourse(Validator.validateCourse(updates.getCourse()));
        if (updates.getYearLevel() != null) existing.setYearLevel(Validator.validateYearLevel(updates.getYearLevel()));
        if (updates.getGwa() != null) existing.setGwa(Validator.validateGWA(updates.getGwa()));
        if (updates.getEmail() != null) {

            // Duplicate Email Checker
            if (!updates.getEmail().equalsIgnoreCase(existing.getEmail()) &&
                    !isEmailUnique(updates.getEmail())) {
                throw new InvalidInputException("Email already exists: " + updates.getEmail());
            }

            existing.setEmail(Validator.validateEmail(updates.getEmail()));
        }

        saveToStorage();
        // No need to rebuild index for PATCH since ID doesn't change
        return existing;
    }

    // GET /api/students/filter?maxGwa=2.5
    // Returns students with GWA of 2.5 or BETTER (1.0, 1.25, 1.5, 2.0, 2.5)
    public List<Student> filterMaxGwa(double maxGwa) {
        List<Student> filteredStudents = new ArrayList<>();

        for (Student student : repository.getAll()) {
            if (student.getGwa() <= maxGwa) {  // LOWER is better
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    // GET /api/students/filter?minGwa=2.5
    // Returns students with GWA of 2.5 or WORSE (2.5, 3.0, 3.5, 4.0, 5.0)
    public List<Student> filterMinGwa(double minGwa) {
        List<Student> filteredStudents = new ArrayList<>();

        for (Student student : repository.getAll()) {
            if (student.getGwa() >= minGwa) {  // HIGHER is worse
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    // GET /api/students/filter?minGwa=1.5&maxGwa=2.5
    // Returns students with GWA between 1.5 and 2.5 (inclusive)
    public List<Student> filterMinMaxGwa(double minGwa, double maxGwa) {
        List<Student> filteredStudents = new ArrayList<>();

        for (Student student : repository.getAll()) {
            double gwa = student.getGwa();
            if (gwa >= minGwa && gwa <= maxGwa) {
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    // GET /api/students/search?q={searchValue} - just name and email (most common use cases)
    public List<Student> searchStudents(String query) {
        List<Student> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();

        for (Student s : repository.getAll()) {
            if (s.getFirstName().toLowerCase().contains(lowerQuery) ||
                    s.getLastName().toLowerCase().contains(lowerQuery) ||
                    s.getEmail().toLowerCase().contains(lowerQuery)) {
                results.add(s);
            }
        }
        return results;
    }
}