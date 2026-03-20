import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String [] args) {

        Scanner scanner = new Scanner(System.in);
        StudentRepository repository = new StudentRepository(new ArrayList<>());
        StudentStorage storage = new StudentStorage("Students.json");
        StudentManager manager = new StudentManager(repository,storage);
        StudentUI studentUI = new StudentUI(scanner,manager);

        studentUI.orchestrateCRUDMenu();


    }
}
