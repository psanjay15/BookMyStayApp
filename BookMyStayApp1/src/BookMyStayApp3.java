/**
 * Book My Stay Application - Use Case 2
 *
 * Demonstrates object-oriented design using abstraction, inheritance,
 * polymorphism, and encapsulation for room modeling.
 *
 * @author Ss
 * @version 1.1
 */

// Abstract class representing a generic Room
abstract class Room {
    private String type;
    private int beds;
    private double price;
    private int size; // in square feet

    // Constructor
    public Room(String type, int beds, double price, int size) {
        this.type = type;
        this.beds = beds;
        this.price = price;
        this.size = size;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq.ft");
        System.out.println("Price per night: ₹" + price);
    }
}

// Single Room class
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 1500.0, 120);
    }
}

// Double Room class
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 2500.0, 200);
    }
}

// Suite Room class
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 5000.0, 350);
    }
}

// Main Application (NO public keyword)
class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======= Book My Stay - Room Availability =======");

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability (simple variables)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display Single Room
        System.out.println("\n--- Single Room ---");
        single.displayDetails();
        System.out.println("Available Rooms: " + singleAvailable);

        // Display Double Room
        System.out.println("\n--- Double Room ---");
        doubleRoom.displayDetails();
        System.out.println("Available Rooms: " + doubleAvailable);

        // Display Suite Room
        System.out.println("\n--- Suite Room ---");
        suite.displayDetails();
        System.out.println("Available Rooms: " + suiteAvailable);

        System.out.println("\n===============================================");
        System.out.println("Thank you for using Book My Stay!");
    }
}