import java.io.IOException;
import java.util.Scanner;
public class StudentUI {
    private final Scanner scanner;
    private final StudentManager studentManager;
    private final int FIRST_MENU_CHOICE = 1;
    private final int LAST_MENU_CHOICE = 6;


    public StudentUI(Scanner scanner, StudentManager studentManager) {
        this.scanner = scanner;
        this.studentManager = studentManager;
    }

    public void orchestrateCRUDMenu() {
        while (true) {
            try {
                displayCRUDMenu();

                int choice = readCRUDChoice();

                switch (choice) {
                    case 1 -> createStudentUI();

                    case 2 -> studentManager.displayStudents();

                    case 3 -> {
                        deleteStudentUI();
                    }

                    case 4 -> {
                        updateStudentUI();
                    }

                    case 5 -> {
                        searchStudentUI();
                    }

                    case 6 -> {
                        System.out.println("Exiting System...");
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println(" File error: " + e.getMessage());
                System.out.println("Press Enter to continue...");
                scanner.nextLine();
                // Loop continues - user stays in menu
            }

        }

    }

    private void displayCRUDMenu() {
        System.out.println("\n┌─────────────────────────────┐");
        System.out.println("│  STUDENT MANAGEMENT SYSTEM  │");
        System.out.println("├─────────────────────────────┤");
        System.out.println("│ [1] Create Student          │");
        System.out.println("│ [2] View All Students       │");
        System.out.println("│ [3] Delete Student          │");
        System.out.println("│ [4] Update Student Info     │");
        System.out.println("│ [5] Search Student          │");
        System.out.println("│ [6] Exit Program            │");
        System.out.println("└─────────────────────────────┘");
    }

    private int readCRUDChoice() {
        while (true) {
            try {
                System.out.print("Select an option → ");
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                if (choice >= FIRST_MENU_CHOICE && choice <= LAST_MENU_CHOICE) return choice; // returns choice that you decided.

                System.out.println("Invalid input! Enter a number between 1-6");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number.");
            }
        }
    }

    private void createStudentUI() throws IOException {
     while(true){ // loops until student is created
         System.out.println("\n=== Create New Student ===");
         System.out.println("(Enter 'cancel' to return to menu)");

         System.out.print("Enter First Name: ");
         String firstName = scanner.nextLine().trim();

         if (firstName.equalsIgnoreCase("cancel")) return;

         System.out.print("Enter Last Name: ");
         String lastName = scanner.nextLine().trim();

         System.out.print("Enter Email: ");
         String email = scanner.nextLine().trim();

         System.out.print("Enter GWA: ");
         String gwa = scanner.nextLine().trim();

         try {
             studentManager.createStudent(email, gwa, lastName, firstName);
             System.out.println("Student created successfully.");
             break;
         } catch (Exception e){
             System.out.println("Could not create student: " + e.getMessage());
            }
        }
    }

    private void deleteStudentUI() {
        System.out.println("Enter Student's ID to delete: ");
        String id = scanner.nextLine();

        Validator.validateID(id); // Validate ID before deleting.
        studentManager.deleteStudent(id);
    }


    public void updateStudentUI() {
        System.out.print("Enter student's ID to update: ");
        String id = scanner.nextLine();

        Validator.validateID(id);


            System.out.println("\nOptions: first name, last name, email, gwa");
            System.out.println("Note: ID cannot be changed.");
            System.out.print("Enter attribute name → ");
            String attribute = scanner.nextLine().trim().toLowerCase();

            if (attribute.isEmpty()) {
                System.out.println("This field is required!");
                return;
            }

            switch (attribute.toLowerCase()) {
                case "first name", "last name", "email", "gwa":
                    promptForAttributeValue(attribute, id); // goes to prompt for the new value
                    break;
                case "id":
                    System.out.println("Error: ID cannot be changed.");
                    break;
                default:
                    System.out.println("Invalid attribute: " + attribute);
                    System.out.println("Valid options: first name, last name, email, gwa");
                    break;
            }

    }

    private void promptForAttributeValue(String attribute, String id) {
        System.out.print("Enter new " + attribute + " : ");
        String newValue = scanner.nextLine().trim();

        studentManager.updateStudentInfo(id,attribute,newValue);

    }

    private void searchStudentUI(){
        System.out.print("Enter Student's ID to search: ");
        String id = scanner.nextLine().trim();
        Validator.validateID(id);

        studentManager.searchStudent(id);

    }

}



