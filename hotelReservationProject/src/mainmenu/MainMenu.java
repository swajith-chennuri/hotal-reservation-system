package mainmenu;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;
import util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ReservationService reservationService = ReservationService.getInstance();

    public static void start() {
        while (true) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Create an Account");
            System.out.println("2. Find and Reserve a Room");
            System.out.println("3. See My Reservations");
            System.out.println("4. Admin Menu");
            System.out.println("5. Exit");
            System.out.print("Enter option: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    createAccount();
                    break;
                case "2":
                    findAndReserveRoom();
                    break;
                case "3":
                    seeReservations();
                    break;
                case "4":
                    AdminMenu.startAdminMenu();
                    break;
                case "5":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

private static void createAccount() {
    String email;
    int attemptCount = 0;
    while (true) {
        System.out.print("Enter email: ");
        email = scanner.nextLine().trim();

        if (!DateUtil.isValidEmail(email)) {  
            System.out.println("Invalid email format! Please enter a valid email (example: user@example.com).");
        } else if (customerService.getCustomerByEmail(email).isPresent()) { 
            attemptCount++;
            System.out.println("Email is already registered! Please log in or use a different email.");

            if (attemptCount == 2) {
                System.out.println("Returning to Main Menu...");
                return;
            }
        } else {
            break;
        }
    }
    System.out.print("Enter first name: ");
    String firstName = scanner.nextLine().trim();

    System.out.print("Enter last name: ");
    String lastName = scanner.nextLine().trim();

    customerService.addCustomer(email, firstName, lastName);
    System.out.println("Account successfully created for " + firstName + " " + lastName + " (" + email + ")");
}



private static void findAndReserveRoom() {
    System.out.print("Enter check-in date (yyyy-MM-dd): ");
    Date checkIn = DateUtil.parseDate(scanner.nextLine());
    System.out.print("Enter check-out date (yyyy-MM-dd): ");
    Date checkOut = DateUtil.parseDate(scanner.nextLine());

    if (checkIn == null || checkOut == null) {
        System.out.println("Invalid date format. Please try again.");
        return;
    }

    List<IRoom> availableRooms = reservationService.getAvailableRooms(checkIn, checkOut);

    if (!availableRooms.isEmpty()) {
        System.out.println("\nAvailable Rooms:");
        availableRooms.forEach(room -> System.out.println(room));
        System.out.print("\nEnter room number to book: ");
        String roomNumber = scanner.nextLine();

        Optional<Customer> customerOpt = customerService.getCustomerByEmail(getUserEmail());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            reservationService.bookRoom(customer, roomNumber, checkIn, checkOut);
        } else {
            System.out.println("Customer not found! Please create an account first.");
        }
        return;
    }

    System.out.println("\nNo available rooms for selected dates.");
    System.out.println("Checking for the nearest available dates...\n");

    List<Date> suggestedDates = reservationService.findNearestAvailableDates(checkIn, checkOut);
    
    if (suggestedDates.isEmpty()) {
        System.out.println("No alternative dates available.");
        return;
    }

    Date suggestedCheckIn = suggestedDates.get(0);
    Date suggestedCheckOut = suggestedDates.get(1);

    List<IRoom> suggestedRooms = reservationService.getAvailableRooms(suggestedCheckIn, suggestedCheckOut);

    if (suggestedRooms.isEmpty()) {
        System.out.println("No recommended rooms available either.");
        return;
    }

    System.out.println("Nearest Available Dates: " + DateUtil.formatDate(suggestedCheckIn) +
            " to " + DateUtil.formatDate(suggestedCheckOut));

    suggestedRooms.forEach(room -> System.out.println(room));

    System.out.print("\nWould you like to book a room for the suggested dates? (yes/no): ");
    String response = scanner.nextLine().trim().toLowerCase();

    if (response.equals("yes") || response.equals("y")) {
        System.out.print("\nEnter room number to book: ");
        String roomNumber = scanner.nextLine();

        Optional<Customer> customerOpt = customerService.getCustomerByEmail(getUserEmail());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            reservationService.bookRoom(customer, roomNumber, suggestedCheckIn, suggestedCheckOut);
            System.out.println("✅ Room successfully booked!");
        } else {
            System.out.println("❌ Customer not found! Please create an account first.");
        }
    } else {
        System.out.println("Returning to the main menu...");
    }
}

    private static void seeReservations() {
        Optional<Customer> customerOpt = customerService.getCustomerByEmail(getUserEmail());

        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            List<Reservation> reservations = reservationService.getCustomerReservations(customer.getEmail());

            if (reservations.isEmpty()) {
                System.out.println("No reservations found.");
            } else {
                reservations.forEach(System.out::println);
            }
        } else {
            System.out.println("Customer not found! Please create an account first.");
        }
    }

    private static String getUserEmail() {
        System.out.print("Enter your email: ");
        return scanner.nextLine();
    }
}
