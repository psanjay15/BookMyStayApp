import java.util.*;

// Core Reservation class (unchanged)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double basePrice;

    public Reservation(String reservationId, String guestName, String roomType, double basePrice) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.basePrice = basePrice;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBasePrice() {
        return basePrice;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Price: ₹" + basePrice;
    }
}

// Booking History (stores confirmed reservations)
class BookingHistory {

    // List preserves insertion order
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() {
        return new ArrayList<>(history); // return copy (safe)
    }
}

// Reporting Service (separate from storage)
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> bookings) {
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("----- Booking History -----");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> bookings) {
        int totalBookings = bookings.size();
        double totalRevenue = 0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : bookings) {
            totalRevenue += r.getBasePrice();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\n----- Booking Summary -----");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: ₹" + totalRevenue);

        System.out.println("Room Type Distribution:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }
}

// Main App (Simulation)
public class BookMyStayApp {

    public static void main(String[] args) {

        // Booking History
        BookingHistory history = new BookingHistory();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("R101", "Sanjay", "Deluxe", 3000);
        Reservation r2 = new Reservation("R102", "Rahul", "Standard", 2000);
        Reservation r3 = new Reservation("R103", "Anjali", "Suite", 5000);

        // Add to history (AFTER confirmation)
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Reporting
        BookingReportService reportService = new BookingReportService();

        List<Reservation> bookings = history.getAllBookings();

        reportService.displayAllBookings(bookings);
        reportService.generateSummary(bookings);
    }
}