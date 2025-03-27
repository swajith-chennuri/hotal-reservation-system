package service;

import model.Customer;
import util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    private static final CustomerService INSTANCE = new CustomerService();
    private final List<Customer> customers = new ArrayList<>();

    private CustomerService() {}

    public static CustomerService getInstance() {
        return INSTANCE;
    }

    public void addCustomer(String email, String firstName, String lastName) {
        customers.add(new Customer(email, firstName, lastName));
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return customers.stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }


    public List<Customer> getAllCustomers() {
        return customers;
    }

    public void printAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customers.forEach(System.out::println);
        }
    }
}
