package com.customer_management.main;

import com.customer_management.model.Customer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Locale;

public class Main {
    private static List<Customer> customers = new ArrayList<>();
    private static final AtomicLong nextId = new AtomicLong(101); 

    public static void main(String[] args) {
        customers.addAll(CustomerGenerator.generateCustomers(100));
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        
        while (true) {
            printMenu();
            try {  
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid choice. Please enter a number.");
                    scanner.nextLine();
                    continue;
                }
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                
                switch (choice) {
                    case 1: addNewCustomer(scanner); break;
                    case 2: deleteCustomerById(scanner); break;
                    case 3: printAllCustomers(); break;
                    case 4: findCustomerById(scanner); break;
                    case 5: findCustomersByCardInterval(scanner); break;
                    case 6: findCustomersWithDebt(); break; 
                    case 0: System.out.println("Program terminated."); return;
                    default: System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number according to the menu.");
                scanner.nextLine();
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n========= CUSTOMER MANAGEMENT MENU =========");
        System.out.println("1. Add a new customer");
        System.out.println("2. Delete customer by ID");
        System.out.println("3. Display the entire customer list");
        System.out.println("--- Selection Criteria ---");
        System.out.println("4. Find customer by ID (Criterion 'a')"); 
        System.out.println("5. List of customers whose card number is within a given range (Criterion 'b')");
        System.out.println("6. Customers who have debt (negative balance) (Criterion 'c')");
        System.out.println("0. Exit");
        System.out.print("Your choice: ");
    }
    
    private static void addNewCustomer(Scanner scanner) {
        try {
            System.out.println("\n--- Adding a new customer ---");
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter Patronymic: ");
            String patronymic = scanner.nextLine();
            System.out.print("Enter Address: ");
            String address = scanner.nextLine();
            
            String cardNumber;
            do {
                System.out.print("Enter Credit Card Number (16 digits): ");
                cardNumber = scanner.nextLine();
                if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                    System.out.println("Card number must consist of 16 digits.");
                }
            } while (cardNumber.length() != 16 || !cardNumber.matches("\\d+"));

            System.out.print("Enter Account Balance (e.g., 123.45): ");
            BigDecimal balance = scanner.nextBigDecimal().setScale(2, RoundingMode.HALF_UP);
            scanner.nextLine(); 
            
            customers.add(new Customer(nextId.getAndIncrement(), lastName, firstName, patronymic,
                                     address, cardNumber, balance));
            System.out.println("Customer successfully added! ID: " + (nextId.get() - 1));
        } catch (InputMismatchException e) {
            System.out.println("Invalid balance format. Please use numbers (e.g., 123.45).");
            if (scanner.hasNextLine()) scanner.nextLine(); 
        } catch (Exception e) {
            System.out.println("An error occurred while adding the customer: " + e.getMessage());
        }
    }

    private static void deleteCustomerById(Scanner scanner) {
        System.out.print("Enter Customer ID to delete: ");
        try {
            long idToDelete = scanner.nextLong();
            scanner.nextLine();

            boolean removed = customers.removeIf(customer -> customer.getId() == idToDelete);

            if (removed) {
                System.out.println("Customer with ID=" + idToDelete + " successfully deleted!");
            } else {
                System.out.println("Customer with ID=" + idToDelete + " was not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID type. Please enter an integer.");
            scanner.nextLine();
        }
    }

    private static void printAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("Customer list is empty!");
            return;
        }
        System.out.println("\n========= Entire Customer List (" + customers.size() + ") =========");
        for (Customer customer : customers) {
            System.out.println(customer.toString());
        }
    }
    
    private static void findCustomerById(Scanner scanner) {
        System.out.print("Enter Customer ID to search: ");
        try {
            long searchId = scanner.nextLong();
            scanner.nextLine(); 

            System.out.println("\n========= Customer with ID '" + searchId + "' =========");

            Customer foundCustomer = null; 
            for (Customer customer : customers) {
                if (customer.getId() == searchId) {
                    foundCustomer = customer;
                    break; 
                }
            }

            if (foundCustomer != null) {
                System.out.println(foundCustomer.toString());
            } else {
                System.out.println("Customer with ID=" + searchId + " was not found.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid ID type. Please enter an integer.");
            scanner.nextLine();
        }
    }

    private static void findCustomersByCardInterval(Scanner scanner) {
        try {
            System.out.println("\n--- Search customers by card number range ---");
            System.out.print("Enter starting card number (16 digits minimum): ");
            String minCard = scanner.nextLine().trim();
            System.out.print("Enter ending card number (16 digits maximum): ");
            String maxCard = scanner.nextLine().trim();

            if (minCard.length() != 16 || maxCard.length() != 16 || !minCard.matches("\\d+") || !maxCard.matches("\\d+")) {
                System.out.println("Card numbers must be 16-digit numbers.");
                return;
            }

            System.out.println("\n========= Customers with card in range [" + minCard + " - " + maxCard + "] =========");

            int count = 0;
            for (Customer customer : customers) {
                String currentCard = customer.getCreditCardNumber();

                if (currentCard.compareTo(minCard) >= 0 && currentCard.compareTo(maxCard) <= 0) {
                    System.out.println(customer.toString());
                    count++;
                }
            }
            
            if (count == 0) {
                System.out.println("Customers in the specified range were not found.");
            } else {
                System.out.println("--- Found customers: " + count + " ---");
            }
        } catch (Exception e) {
            System.out.println("Error processing card range: " + e.getMessage());
        }
    }

    private static void findCustomersWithDebt() {
        System.out.println("\n========= Customers who have debt (Balance < 0) =========");

        int count = 0;
        List<Customer> debtors = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getAccountBalance().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(customer);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No customers with debt found.");
        } else {
            System.out.println("NUMBER of customers with debt: " + count + "\n");
            for (Customer debtor : debtors) {
                System.out.println(debtor.toString());
            }
        }
    }
}