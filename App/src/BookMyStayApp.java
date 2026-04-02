import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Guest: " + guestName + ", Room: " + roomType;
    }
}

// Booking History (same as Use Case 8)
class BookingHistory {
    private List<Reservation> bookings = new ArrayList<>();

    public void addReservation(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getBookings() {
        return new ArrayList<>(bookings); // safe copy
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

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Check availability
    public void checkAvailability(String roomType) throws InvalidBookingException {
        if (rooms.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    // Book room safely
    public void bookRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        checkAvailability(roomType);

        rooms.put(roomType, rooms.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + rooms);
    }
}

// Validator Class (Fail-Fast)
class BookingValidator {

    public static void validate(String guestName, String roomType)
            throws InvalidBookingException {

        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        InventoryManager inventory = new InventoryManager();
        BookingHistory history = new BookingHistory();

        String[][] inputs = {
                {"Sanjay", "Deluxe"},
                {"", "Standard"},        // Invalid name
                {"Rahul", "Premium"},   // Invalid room
                {"Anjali", "Suite"},
                {"Kiran", "Suite"}      // No availability
        };

        int id = 101;

        for (String[] input : inputs) {
            try {
                String guest = input[0];
                String room = input[1];

                // Step 1: Input validation (Fail-Fast)
                BookingValidator.validate(guest, room);

                // Step 2: Inventory validation + booking
                inventory.bookRoom(room);

                // Step 3: Create reservation
                Reservation r = new Reservation("R" + id++, guest, room);

                // Step 4: Store in history
                history.addReservation(r);

                System.out.println("✅ Booking Successful: " + r);

            } catch (InvalidBookingException e) {
                // Graceful error handling
                System.out.println("❌ Booking Failed: " + e.getMessage());
            }
        }

        // System continues running
        System.out.println("\n📋 Booking History:");
        for (Reservation r : history.getBookings()) {
            System.out.println(r);
        }

        System.out.println();
        inventory.displayInventory();
    }
}