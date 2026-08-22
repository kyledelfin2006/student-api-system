package Utils;

public class StudentIDGenerator {
    private static int nextId = 0;

    public static String generateNextID() {
        return String.format("%04d", nextId++);
    }

    public static void setNextId(int id) {
        StudentIDGenerator.nextId = id;
    }
}
