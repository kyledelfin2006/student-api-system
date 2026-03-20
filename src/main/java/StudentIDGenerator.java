public class StudentIDGenerator {
    private static int nextId;

    public static String generateNextID(){
        String id = String.format("%04d", nextId);
        nextId++;
        return id; // "0001", "0002", etc.
    }


    public static void setNextId(int nextId) {
        StudentIDGenerator.nextId = nextId;
    }
}
