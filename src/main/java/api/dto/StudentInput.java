package api.dto;

public class StudentInput {

    private String firstName;
    private String lastName;
    private String course;
    private Integer yearLevel;
    private Double gwa;
    private String email;

    public StudentInput() {}

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

    public Integer getYearLevel() {
        return yearLevel;
    }

    public void setYearLevel(Integer yearLevel) {
        this.yearLevel = yearLevel;
    }

    public Double getGwa() {
        return gwa;
    }

    public void setGwa(Double gwa) {
        this.gwa = gwa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
