package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;
import util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationService {
    private static final ReservationService INSTANCE = new ReservationService();
    private final List<IRoom> rooms = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();

    private ReservationService() {}

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    public IRoom addRoom(String roomNumber, double price, RoomType roomType) {
        if (getRoom(roomNumber) != null) {
            System.out.println("Room number already exists! Please enter a different number.");
            return null;
        }

        IRoom room = new Room(roomNumber, price, roomType);
        rooms.add(room);
        return room;
    }

    public IRoom getRoom(String roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber().equals(roomNumber))
                .findFirst()
                .orElse(null);
    }

    public List<IRoom> getAllRooms() {
        return rooms;
    }

    public List<IRoom> getAvailableRooms(Date checkIn, Date checkOut) {
        return rooms.stream()
                .filter(room -> isRoomAvailable(room, checkIn, checkOut))
                .collect(Collectors.toList());
    }

    private boolean isRoomAvailable(IRoom room, Date checkIn, Date checkOut) {
        return reservations.stream().noneMatch(reservation ->
                reservation.getRoom().equals(room) &&
                        !(checkOut.before(reservation.getCheckInDate()) || checkIn.after(reservation.getCheckOutDate()))
        );
    }

    public Reservation bookRoom(Customer customer, String roomNumber, Date checkIn, Date checkOut) {
        IRoom room = getRoom(roomNumber);
        if (room == null) {
            System.out.println("Room not found!");
            return null;
        }

        if (!isRoomAvailable(room, checkIn, checkOut)) {
            System.out.println("Room is not available for the selected dates.");
            return null;
        }

        Reservation reservation = new Reservation(customer, room, checkIn, checkOut);
        reservations.add(reservation);

        System.out.println("\n**Reservation Successful!**");
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Room: " + room.getRoomNumber() + " | Type: " + room.getRoomType() + " | Price: $" + room.getRoomPrice());
        System.out.println("Check-In Date: " + DateUtil.formatDate(checkIn));
        System.out.println("Check-Out Date: " + DateUtil.formatDate(checkOut));
        System.out.println("Room successfully booked!");

        return reservation;
    }

    public List<Reservation> getCustomerReservations(String email) {
        return reservations.stream()
                .filter(reservation -> reservation.getCustomer().getEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
    public List<Date> findNearestAvailableDates(Date checkIn, Date checkOut) {
        Date suggestedCheckIn = new Date(checkIn.getTime());
        Date suggestedCheckOut = new Date(checkOut.getTime());

        for (int i = 1; i <= 7; i++) {
            suggestedCheckIn = new Date(suggestedCheckIn.getTime() + (1 * 24 * 60 * 60 * 1000)); // +1 day
            suggestedCheckOut = new Date(suggestedCheckOut.getTime() + (1 * 24 * 60 * 60 * 1000));

            List<IRoom> availableRooms = getAvailableRooms(suggestedCheckIn, suggestedCheckOut);
            if (!availableRooms.isEmpty()) {
                return List.of(suggestedCheckIn, suggestedCheckOut);
            }
        }
        return List.of(); 
    }
}