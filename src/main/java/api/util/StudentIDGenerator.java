package api.util;

import java.util.concurrent.atomic.AtomicInteger;

public class StudentIDGenerator {
    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static final int ID_WIDTH = 4;

    public static String generateNextID() {
        int id = nextId.getAndIncrement();
        return String.format("%0" + ID_WIDTH + "d", id);
    }

    public static void setNextId(int id) {
        nextId.set(id);
    }

    // Add this for testing/reset
    public static void reset() {
        nextId.set(0);
    }
}