/**
 * Book My Stay Application
 *
 * This class serves as the entry point for the Hotel Booking Management System.
 * It demonstrates the basic structure of a Java application, including
 * the main() method and console output.
 *
 * The application prints a welcome message along with the application
 * name and version, then terminates.
 *
 * @author Ss
 * @version 1.0
 */
class BookMyStayApp3 {

    /**
     * Main method - Entry point of the application.
     * JVM starts execution from this method.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {

        // Application Name and Version
        String appName = "Book My Stay - Hotel Booking System";
        String version = "v1.0";

        // Welcome Message
        System.out.println("====================================");
        System.out.println(" Welcome to " + appName);
        System.out.println(" Version: " + version);
        System.out.println("====================================");

        // Additional Information
        System.out.println("Your reliable partner for booking hotels seamlessly.");
        System.out.println("Application started successfully!");

        // End of application
        System.out.println("====================================");
    }
}