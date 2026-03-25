import java.util.*;

// Room domain model (contains details)
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

// Centralized Inventory (same as Use Case 3)
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // READ-ONLY access
    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    // Read-only full map (safe copy)
    public Map<String, Integer> getAllAvailability() {
        return new HashMap<>(inventory); // defensive copy
    }
}

// Search Service (READ-ONLY)
class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    // Search available rooms (NO state change)
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");

        Map<String, Integer> availabilityMap = inventory.getAllAvailability();

        for (String type : availabilityMap.keySet()) {

            int availableCount = availabilityMap.get(type);

            // Defensive check: show only available rooms
            if (availableCount > 0 && roomCatalog.containsKey(type)) {

                Room room = roomCatalog.get(type);

                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: ₹" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available: " + availableCount);
                System.out.println("---------------------------");
            }
        }
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        // Step 1: Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 5);
        inventory.addRoomType("Double", 0); // unavailable
        inventory.addRoomType("Suite", 2);

        // Step 2: Create room catalog (domain data)
        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room("Single", 1500, "Bed, WiFi, AC"));
        roomCatalog.put("Double", new Room("Double", 2500, "2 Beds, WiFi, AC, TV"));
        roomCatalog.put("Suite", new Room("Suite", 5000, "Luxury Bed, WiFi, AC, TV, Mini Bar"));

        // Step 3: Search Service
        SearchService searchService = new SearchService(inventory, roomCatalog);

        // Step 4: Guest searches rooms
        searchService.searchAvailableRooms();
    }
}