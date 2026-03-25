import java.util.HashMap;
import java.util.Map;

// RoomInventory class - Centralized inventory manager
class RoomInventory {

    // HashMap to store room type -> available count
    private Map<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Add or initialize room type with count
    public void addRoomType(String roomType, int count) {
        if (count < 0) {
            System.out.println("Invalid room count.");
            return;
        }
        inventory.put(roomType, count);
        System.out.println(roomType + " rooms added with count: " + count);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Book a room (reduce count)
    public boolean bookRoom(String roomType) {
        int available = getAvailability(roomType);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            System.out.println("Booking successful for " + roomType);
            return true;
        } else {
            System.out.println("No rooms available for " + roomType);
            return false;
        }
    }

    // Cancel booking (increase count)
    public void cancelBooking(String roomType) {
        int available = getAvailability(roomType);
        inventory.put(roomType, available + 1);
        System.out.println("Booking cancelled for " + roomType);
    }

    // Display entire inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() +
                    " | Available: " + entry.getValue());
        }
        System.out.println("--------------------------------\n");
    }
}

// Main class to test the system
public class BookMyStayApp {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Add room types
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 3);
        inventory.addRoomType("Suite", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Perform bookings
        inventory.bookRoom("Single");
        inventory.bookRoom("Suite");
        inventory.bookRoom("Suite"); // should reduce to 0
        inventory.bookRoom("Suite"); // should fail

        // Display after bookings
        inventory.displayInventory();

        // Cancel booking
        inventory.cancelBooking("Suite");

        // Final inventory state
        inventory.displayInventory();

        // Check availability
        System.out.println("Available Double Rooms: " +
                inventory.getAvailability("Double"));
    }
}