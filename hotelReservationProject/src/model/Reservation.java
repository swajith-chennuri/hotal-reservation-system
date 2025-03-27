package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); // ✅ Format date

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "==== Reservation Successful!====\n" +
               "Customer: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
               "Email: " + customer.getEmail() + "\n" +
               "Room Number: " + room.getRoomNumber() + " (" + room.getRoomType() + ")\n" +
               "Price: $" + room.getRoomPrice() + " per night\n" +
               "Check-in Date: " + DATE_FORMAT.format(checkInDate) + "\n" +
               "Check-out Date: " + DATE_FORMAT.format(checkOutDate);
    }
}
