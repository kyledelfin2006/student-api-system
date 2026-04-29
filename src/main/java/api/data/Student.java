package api.data;

import api.util.StudentIDGenerator;
import api.util.Validator;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "firstName", "lastName", "course","yearLevel","gwa","email"})
public class Student {

    private String id;
    private String firstName;
    private String lastName;
    private String course;
    private int yearLevel;
    private double gwa;
    private String email;

    public Student(){}

    public Student(String firstName, String lastName, String course, int yearLevel, double gwa, String email) {
        this.id = StudentIDGenerator.generateNextID();
        this.firstName = Validator.validateName(firstName);
        this.lastName = Validator.validateName(lastName);;
        this.course = Validator.validateCourse(course);
        this.yearLevel = Validator.validateYearLevel(yearLevel); // Turns String Year Level -> Int YearLevel
        this.gwa = Validator.validateGWA(gwa); // Turns String Gwa -> double GWA
        this.email = Validator.validateEmail(email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(int yearLevel) {
        this.yearLevel = yearLevel;
    }

    public double getGwa() {
        return gwa;
    }

    public void setGwa(double gwa) {
        this.gwa = gwa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("%s %s, ID: %s, GWA: %s, email: %s",
                firstName, lastName, id, gwa, email);

    }
}
