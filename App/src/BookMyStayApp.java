import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String guestName;
    private String roomType;

    public Reservation(String id, String guestName, String roomType) {
        this.id = id;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Guest: " + guestName + ", Room: " + roomType;
    }
}

// Inventory Manager (Serializable)
class InventoryManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> rooms;

    public InventoryManager() {
        rooms = new HashMap<>();
        rooms.put("Standard", 2);
        rooms.put("Deluxe", 1);
        rooms.put("Suite", 1);
    }

    public void bookRoom(String roomType) {
        if (rooms.containsKey(roomType) && rooms.get(roomType) > 0) {
            rooms.put(roomType, rooms.get(roomType) - 1);
        }
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void displayInventory() {
        System.out.println("Inventory: " + rooms);
    }
}

// Booking History (Serializable)
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookings = new ArrayList<>();

    public void addReservation(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getBookings() {
        return bookings;
    }

    public void display() {
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }
}

// Wrapper class (to store entire system state)
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    BookingHistory history;
    InventoryManager inventory;

    public SystemState(BookingHistory history, InventoryManager inventory) {
        this.history = history;
        this.inventory = inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("💾 State saved successfully.");

        } catch (IOException e) {
            System.out.println("❌ Error saving data: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("🔄 State loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No saved data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("❌ Error loading data. Starting safe state.");
        }

        return null; // safe fallback
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory history;
        InventoryManager inventory;

        // Step 1: Load previous state
        SystemState state = PersistenceService.load();

        if (state != null) {
            history = state.history;
            inventory = state.inventory;
        } else {
            history = new BookingHistory();
            inventory = new InventoryManager();
        }

        // Step 2: Simulate booking
        Reservation r1 = new Reservation("R101", "Sanjay", "Deluxe");
        history.addReservation(r1);
        inventory.bookRoom("Deluxe");

        System.out.println("\n📋 Current Bookings:");
        history.display();

        System.out.println();
        inventory.displayInventory();

        // Step 3: Save state before shutdown
        SystemState newState = new SystemState(history, inventory);
        PersistenceService.save(newState);

        System.out.println("\n✅ System ready for shutdown/restart.");
    }
}