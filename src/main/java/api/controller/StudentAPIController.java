package api.controller;

import api.models.Student;
import api.dto.StudentInput;
import api.manager.StudentManager;
import api.repository.StudentRepository;
import api.storage.StudentStorage;
import api.responses.ApiResponse;
import api.responses.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

import static spark.Spark.*;

public class StudentAPIController {
    public static void main(String [] args) {

        StudentRepository repository = new StudentRepository();
        StudentStorage storage = new StudentStorage("Students.json");
        StudentManager manager = new StudentManager(repository,storage);
        ObjectMapper mapper = new ObjectMapper();


        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        port(8081);  // Different port from LibraryAPI (8080)

        // Health check
        get("/api/health", (req, res) -> {
            res.type("application/json");
            return mapper.writeValueAsString(new ApiResponse<>(true, "Student API is running"));
        });

        // Get All Students
        get("/api/students", (req, res) -> {
            res.type("application/json");
            return mapper.writeValueAsString(manager.getAllStudents());
        });

        // Search Student by ID
        get("/api/students/:id",(req, res) -> {
            String id = req.params(":id");
            Student found = manager.findStudent(id);

            if (found == null){
                res.status(404);
                return mapper.writeValueAsString(new ErrorResponse("Student not found.", 404));
            }

            res.type("application/json");
            return mapper.writeValueAsString(found);

        });

        // Post Student
        post("/api/students/", (req,res) -> {
            try{
                StudentInput input = mapper.readValue(req.body(), StudentInput.class);
                Student student = manager.createStudent(input);

                res.type("application/json");
                res.status(201);

                return mapper.writeValueAsString(new ApiResponse<>(true, "Student Added Successfully",student));

            } catch (IllegalArgumentException e){
                res.status(400); // Validation failure
                return mapper.writeValueAsString(new ErrorResponse("Validation failed",e.getMessage(),400));
            } catch (Exception e) {
                res.status(400);
                return mapper.writeValueAsString(new ErrorResponse("Invalid JSON", e.getMessage(), 400));
            }
        });

        // Delete Student
        delete("/api/students/:id",(req,res) -> {
            String id = req.params(":id");
            boolean deleted = manager.deleteStudent(id);

            res.type("application/json");

            if (deleted){
                return mapper.writeValueAsString(new ApiResponse<>(true,"Student deleted successfully"));
            } else {
                res.status(404);
                return mapper.writeValueAsString(new ErrorResponse("Student not found", 404));
            }

        });

        patch("/api/students/:id", (req, res) -> {
            String id = req.params(":id");
            try {
                StudentInput updates = mapper.readValue(req.body(), StudentInput.class);
                Student updated = manager.patchStudent(id, updates);

                if (updated == null) {
                    res.status(404);
                    return mapper.writeValueAsString(new ErrorResponse("Student not found", 404));
                }

                res.type("application/json");
                return mapper.writeValueAsString(new ApiResponse<>(true, "Student updated successfully", updated));


            } catch (IllegalArgumentException e){
                res.status(400); // Validation failure
                return mapper.writeValueAsString(new ErrorResponse("Validation failed",e.getMessage(),400));
            } catch (Exception e) {
                res.status(400);
                return mapper.writeValueAsString(new ErrorResponse("Invalid JSON", e.getMessage(), 400));
            }
        });

        // Search students by query
        get("/api/students/search", (req, res) -> {
            String query = req.queryParams("q");

            if (query == null || query.trim().isEmpty()){
                res.status(400);
                return mapper.writeValueAsString(new ErrorResponse("Missing Queries","Required: q", 400));
            }

            List<Student> students = manager.searchStudents(query);
            res.type("application/json");
            return mapper.writeValueAsString(students);
        });

        // GET filter students by GWA
        get("/api/students/filter", (req, res) -> {
            String minGwaParam = req.queryParams("minGwa");
            String maxGwaParam = req.queryParams("maxGwa");

            List<Student> results;

            if (minGwaParam != null && maxGwaParam != null) {
                double minGwa = Double.parseDouble(minGwaParam);
                double maxGwa = Double.parseDouble(maxGwaParam);
                results = manager.filterMinMaxGwa(minGwa, maxGwa);
            } else if (minGwaParam != null) {
                double minGwa = Double.parseDouble(minGwaParam);
                results = manager.filterMinGwa(minGwa);
            } else if (maxGwaParam != null) {
                double maxGwa = Double.parseDouble(maxGwaParam);
                results = manager.filterMaxGwa(maxGwa);
            } else {
                results = manager.getAllStudents();
            }

            res.type("application/json");
            return mapper.writeValueAsString(results);
        });

        // 404 handler
        notFound((req, res) -> {
            res.type("application/json");
            res.status(404);
            return mapper.writeValueAsString(new ErrorResponse("Endpoint not found", 404));
        });

        System.out.println("\n========================================");
        System.out.println(" STUDENT API IS RUNNING");
        System.out.println("========================================");
        System.out.println(" URL: http://localhost:8081");
        System.out.println(" ENDPOINTS:");
        System.out.println("   GET    /api/health");
        System.out.println("   GET    /api/students");
        System.out.println("   GET    /api/students/:id");
        System.out.println("   POST   /api/students");
        System.out.println("   DELETE /api/students/:id");
        System.out.println("   PATCH  /api/students/:id");
        System.out.println("   GET    /api/students/search?q=");
        System.out.println("   GET    /api/students/filter?minGwa=&maxGwa=");
        System.out.println("========================================\n");


    }
}
