import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceId;
    private String serviceName;
    private double price;

    public AddOnService(String serviceId, String serviceName, double price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// Represents a Reservation (Core booking stays unchanged)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType;
    }
}

// Manager class for Add-On Services
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = serviceMap.get(reservationId);

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getPrice();
            }
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services:");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalCost(reservationId));
    }
}

// Main class to demonstrate functionality
public class BookMyStayApp {

    public static void main(String[] args) {

        // Create reservation (core system)
        Reservation reservation = new Reservation("R101", "Sanjay", "Deluxe");

        // Create add-on services
        AddOnService breakfast = new AddOnService("S1", "Breakfast", 500);
        AddOnService spa = new AddOnService("S2", "Spa", 1500);
        AddOnService pickup = new AddOnService("S3", "Airport Pickup", 800);

        // Add-On Service Manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Guest selects services
        manager.addService(reservation.getReservationId(), breakfast);
        manager.addService(reservation.getReservationId(), spa);
        manager.addService(reservation.getReservationId(), pickup);

        // Display booking + services
        System.out.println(reservation);
        manager.displayServices(reservation.getReservationId());
    }
}