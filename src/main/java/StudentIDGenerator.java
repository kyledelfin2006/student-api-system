import java.util.concurrent.atomic.AtomicInteger;

public class StudentIDGenerator {
    private static final AtomicInteger nextId = new AtomicInteger(0);

    public static String generateNextID() {
        int id = nextId.getAndIncrement();  // READ + WRITE as ONE operation
        return String.format("%04d", id);
    }


    // Setter for NextId
    public static void setNextId(int id) {
        StudentIDGenerator.nextId.set(id);
    }
}
