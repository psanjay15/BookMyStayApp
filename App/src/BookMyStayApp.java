import java.util.*;

// Reservation class (represents booking request)
class Reservation {
    private static int idCounter = 1;

    private int reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.reservationId = idCounter++;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType;
    }
}

// Booking Request Queue (FIFO handling)
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View next request (peek, no removal)
    public Reservation viewNextRequest() {
        return queue.peek();
    }

    // Get and remove next request (dequeue)
    public Reservation processNextRequest() {
        return queue.poll();
    }

    // Display all queued requests
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue ---");

        if (queue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : queue) {
            System.out.println(r);
        }
        System.out.println("-----------------------------\n");
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize booking request queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Guests submit booking requests (arrival order matters)
        requestQueue.addRequest(new Reservation("Alice", "Single"));
        requestQueue.addRequest(new Reservation("Bob", "Suite"));
        requestQueue.addRequest(new Reservation("Charlie", "Single"));
        requestQueue.addRequest(new Reservation("David", "Double"));

        // Display all requests (FIFO order)
        requestQueue.displayQueue();

        // Peek next request (without removing)
        System.out.println("Next Request to Process: " + requestQueue.viewNextRequest());

        // Simulate processing (NO inventory update yet)
        System.out.println("\nProcessing Requests (no allocation yet):");

        while (!requestQueue.isEmpty()) {
            Reservation r = requestQueue.processNextRequest();
            System.out.println("Processing: " + r);
        }

        // Final queue state
        requestQueue.displayQueue();
    }
}