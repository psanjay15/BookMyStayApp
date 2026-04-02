import java.util.*;

// Custom Exception
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String id;
    private String guestName;
    private String roomType;
    private boolean isCancelled;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isCancelled = false;
    }

    public String getId() { return id; }
    public String getRoomType() { return roomType; }

    public boolean isCancelled() { return isCancelled; }
    public void cancel() { this.isCancelled = true; }

    @Override
    public String toString() {
        return "ID: " + id + ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> rooms = new HashMap<>();

    public InventoryManager() {
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 1);
        rooms.put("Suite", 1);
    }

    public void bookRoom(String roomType) throws BookingException {
        if (!rooms.containsKey(roomType))
            throw new BookingException("Invalid room type");

        if (rooms.get(roomType) <= 0)
            throw new BookingException("No rooms available");

        rooms.put(roomType, rooms.get(roomType) - 1);
    }

    public void releaseRoom(String roomType) {
        rooms.put(roomType, rooms.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + rooms);
    }
}

// Booking History
class BookingHistory {
    private Map<String, Reservation> bookings = new HashMap<>();

    public void addReservation(Reservation r) {
        bookings.put(r.getId(), r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void displayAll() {
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}

// Cancellation Service (Core Logic)
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              InventoryManager inventory)
            throws BookingException {

        // Step 1: Validate existence
        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            throw new BookingException("Reservation does not exist");
        }

        // Step 2: Prevent duplicate cancellation
        if (r.isCancelled()) {
            throw new BookingException("Booking already cancelled");
        }

        // Step 3: Push to rollback stack (LIFO)
        rollbackStack.push(reservationId);

        // Step 4: Restore inventory
        inventory.releaseRoom(r.getRoomType());

        // Step 5: Update booking status
        r.cancel();

        System.out.println("🔄 Booking Cancelled: " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("Rollback Stack (LIFO): " + rollbackStack);
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        try {
            // Create bookings
            Reservation r1 = new Reservation("R101", "Sanjay", "Deluxe");
            Reservation r2 = new Reservation("R102", "Rahul", "Standard");

            inventory.bookRoom("Deluxe");
            inventory.bookRoom("Standard");

            history.addReservation(r1);
            history.addReservation(r2);

            System.out.println("✅ Bookings Created\n");

            // Cancel booking
            cancelService.cancelBooking("R101", history, inventory);

            // Try invalid cancellation
            cancelService.cancelBooking("R999", history, inventory);

        } catch (BookingException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        // Display system state
        System.out.println("\n📋 Booking History:");
        history.displayAll();

        System.out.println();
        inventory.displayInventory();

        System.out.println();
        cancelService.displayRollbackStack();
    }
}