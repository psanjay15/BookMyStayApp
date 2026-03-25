import java.util.*;

// -------------------- Reservation --------------------
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

// -------------------- Booking Queue (FIFO) --------------------
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
        System.out.println("Request added: " + r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// -------------------- Inventory Service --------------------
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Controlled update (decrement)
    public void decrementRoom(String type) {
        int available = getAvailability(type);
        if (available > 0) {
            inventory.put(type, available - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------------------- Booking Service (Core Logic) --------------------
class BookingService {

    private RoomInventory inventory;

    // Track ALL allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track roomType -> assigned room IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private int roomIdCounter = 101;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + roomIdCounter++;
        } while (allocatedRoomIds.contains(roomId)); // ensure uniqueness

        return roomId;
    }

    // Process booking request (ATOMIC LOGIC)
    public void processReservation(Reservation reservation) {

        String type = reservation.getRoomType();

        System.out.println("\nProcessing: " + reservation);

        // Step 1: Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("Booking FAILED - No rooms available for " + type);
            return;
        }

        // Step 2: Generate unique room ID
        String roomId = generateRoomId(type);

        // Step 3: Assign & store
        allocatedRoomIds.add(roomId);

        roomAllocations
                .computeIfAbsent(type, k -> new HashSet<>())
                .add(roomId);

        // Step 4: Update inventory immediately
        inventory.decrementRoom(type);

        // Step 5: Confirm booking
        System.out.println("Booking CONFIRMED!");
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("Room Type: " + type);
        System.out.println("Assigned Room ID: " + roomId);
    }

    // Display allocations
    public void displayAllocations() {
        System.out.println("\n--- Room Allocations ---");

        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " Rooms: " + roomAllocations.get(type));
        }
    }
}

// -------------------- Main App --------------------
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 2);
        inventory.addRoomType("Suite", 1);

        // Step 2: Initialize Queue
        BookingRequestQueue queue = new BookingRequestQueue();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("David", "Suite")); // should fail

        // Step 3: Booking Service
        BookingService bookingService = new BookingService(inventory);

        // Step 4: Process Queue (FIFO)
        while (!queue.isEmpty()) {
            Reservation r = queue.getNextRequest();
            bookingService.processReservation(r);
        }

        // Step 5: Final State
        inventory.displayInventory();
        bookingService.displayAllocations();
    }
}