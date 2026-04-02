import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Inventory Manager
class InventoryManager {
    private Map<String, Integer> rooms = new HashMap<>();

    public InventoryManager() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 1);
        rooms.put("Suite", 1);
    }

    // Critical Section (Thread Safe)
    public synchronized boolean bookRoom(String roomType, String guestName) {
        if (!rooms.containsKey(roomType)) {
            System.out.println("❌ Invalid room type for " + guestName);
            return false;
        }

        int available = rooms.get(roomType);

        if (available > 0) {
            // Simulate delay (to expose race conditions if not synchronized)
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            rooms.put(roomType, available - 1);
            System.out.println("✅ " + guestName + " booked " + roomType);
            return true;
        } else {
            System.out.println("❌ No " + roomType + " rooms left for " + guestName);
            return false;
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + rooms);
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
    }

    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private InventoryManager inventory;

    public BookingProcessor(BookingQueue queue, InventoryManager inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request;

            // Fetch request safely
            synchronized (queue) {
                request = queue.getRequest();
            }

            if (request == null) break;

            // Process booking (critical section inside)
            inventory.bookRoom(request.roomType, request.guestName);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {

        InventoryManager inventory = new InventoryManager();
        BookingQueue queue = new BookingQueue();

        // Simulating multiple guest requests
        queue.addRequest(new BookingRequest("Sanjay", "Deluxe"));
        queue.addRequest(new BookingRequest("Rahul", "Deluxe"));
        queue.addRequest(new BookingRequest("Anjali", "Standard"));
        queue.addRequest(new BookingRequest("Kiran", "Standard"));
        queue.addRequest(new BookingRequest("Ravi", "Suite"));
        queue.addRequest(new BookingRequest("Meena", "Suite"));

        // Multiple threads (concurrent users)
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        // Final system state
        inventory.displayInventory();
    }
}