package mainmenu;

import service.CustomerService;
import service.ReservationService;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.RoomType;

import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ReservationService reservationService = ReservationService.getInstance();
    private static final CustomerService customerService = CustomerService.getInstance();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static void startAdminMenu() {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add a Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. View All Customers");
            System.out.println("4. View All Reservations");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter option: ");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    addRoom();
                    break;
                case "2":
                    viewAllRooms();
                    break;
                case "3":
                    viewAllCustomers();
                    break;
                case "4":
                    viewAllReservations();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

private static void addRoom() {
    while (true) {  
        String roomNumber;
        
        while (true) {  
            System.out.print("Enter room number: ");
            roomNumber = scanner.nextLine().trim();

            if (roomNumber.isEmpty()) {
                System.out.println("Room number cannot be empty.");
                continue;
            }

            if (reservationService.getRoom(roomNumber) != null) {
                System.out.println("Room number already exists! Please enter a different number.");
            } else {
                break;
            }
        }

        System.out.print("Enter price per night: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Please enter a valid number.");
            continue;
        }

        System.out.print("Enter room type (1 for SINGLE, 2 for DOUBLE): ");
        String roomTypeChoice = scanner.nextLine().trim();

        RoomType roomType;
        if (roomTypeChoice.equals("1")) {
            roomType = RoomType.SINGLE;
        } else if (roomTypeChoice.equals("2")) {
            roomType = RoomType.DOUBLE;
        } else {
            System.out.println("Invalid choice. Please enter 1 for SINGLE or 2 for DOUBLE.");
            continue;
        }

        IRoom room = reservationService.addRoom(roomNumber, price, roomType);
        System.out.println("Room successfully added: " + room);

        while (true) {
            System.out.print("Do you want to add another room? (Y/N): ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("N")) {
                return;
            } else if (choice.equals("Y")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }
}


    private static void viewAllRooms() {
        List<IRoom> allRooms = reservationService.getAllRooms();
        if (allRooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            System.out.println("\n===== All Rooms =====");
            allRooms.forEach(room -> 
                System.out.println("Room No: " + room.getRoomNumber() + " | Type: " + room.getRoomType() + " | Price: $" + room.getRoomPrice()));
        }
    }

    private static void viewAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        if (allCustomers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("\n===== All Customers =====");
            allCustomers.forEach(customer -> 
                System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName() +
                                   " | Email: " + customer.getEmail()));
        }
    }

    private static void viewAllReservations() {
        List<Reservation> allReservations = reservationService.getAllReservations();
        if (allReservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("\n===== All Reservations =====");
            allReservations.forEach(reservation -> 
                System.out.println("Customer: " + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName() +
                                   " | Room: " + reservation.getRoom().getRoomNumber() +
                                   " | Type: " + reservation.getRoom().getRoomType() +
                                   " | Price: $" + reservation.getRoom().getRoomPrice() +
                                   " | Check-in: " + DATE_FORMAT.format(reservation.getCheckInDate()) +
                                    " | Check-out: " + DATE_FORMAT.format(reservation.getCheckOutDate())));
        }
    }
}
