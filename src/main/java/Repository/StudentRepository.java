package Repository;

import Model.Student;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class StudentRepository {
    private final List<Student> studentList;
    private final ObjectMapper mapper;
    private final String filename;

    public StudentRepository(String filename, List<Student> studentList) {
        this.filename = filename;
        this.studentList = studentList;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void add(Student s) {
        studentList.add(s);
    }

    public void remove(Student s) {
        studentList.remove(s);
    }

    public List<Student> getAll() {
        return Collections.unmodifiableList(studentList);
    }

    public void loadFromFile() throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return;
        }
        List<Student> loaded = mapper.readValue(file, new TypeReference<List<Student>>() {});
        studentList.addAll(loaded);
    }

    public void saveToFile() throws IOException {
        mapper.writeValue(new File(filename), studentList);
    }
}
