package api.storage;

import api.models.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Is a type of storage
public class StudentStorage implements Storage<Student> {
    private final ObjectMapper mapper;
    private final String filename;

    public StudentStorage(String filename) {
        this.filename = filename; // User decides this
        this.mapper = new ObjectMapper();

        // Make JSON output pretty (easier to read)
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // Save all notifications to file // Should be caught externally
    @Override
    public void save(List<Student> students) throws IOException {
        mapper.writeValue(new File(filename), students);
    }

    // Load all notifications from file // Should be caught externally
    @Override
    public List<Student> load() throws IOException {
        File filePath = new File(filename);

        if (!filePath.exists()) {
            return new ArrayList<>();
        }

        return mapper.readValue(filePath,new TypeReference<List<Student>>() {}); // Returns the loaded list read by mapper
    }

}