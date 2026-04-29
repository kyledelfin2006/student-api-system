package Model;

import Utils.Validator;

public class Student {
    private String firstName;
    private String lastName;
    private String gwa;
    private String id;
    private String email;

    public Student(){}

    public Student(String email, String generatedID,  String gwa, String lastName, String firstName) {

        // Validation occurs here.
        Validator.validateName(firstName);
        Validator.validateName(lastName);
        Validator.validateGWA(gwa);
        Validator.validateEmail(email);


        this.email = email;
        this.id = generatedID;
        this.gwa = gwa;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        Validator.validateEmail(email);
        this.email = email;
    }

    public void setGwa(String gwa) {
        Validator.validateGWA(gwa);
        this.gwa = gwa;
    }

    public void setLastName(String lastName) {
        Validator.validateName(lastName);
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        Validator.validateName(firstName);
        this.firstName = firstName;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGwa() {
        return gwa;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("%s %s, ID: %s, GWA: %s, email: %s",
                firstName, lastName, id, gwa, email);

    }
}
