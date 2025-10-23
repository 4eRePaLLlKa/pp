package com.customer_management.main;

import com.customer_management.model.Customer; 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerGenerator {
    private static final AtomicLong idCounter = new AtomicLong(1);
    private static String generateCardNumber(Random rand) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    public static List<Customer> generateCustomers(int count) {
        List<Customer> customers = new ArrayList<>();
        Random rand = new Random();

        String[] lastNames = {"Shevchenko", "Kovalchuk", "Bondarenko", "Tkachenko", "Kovalenko", "Kravchenko", "Melnyk", "Guk"};
        String[] firstNames = {"Ivan", "Petro", "Oleksandr", "Mariia", "Olena", "Anna", "Viktor", "Nataliia"};
        String[] patronymics = {"Ivanovych", "Petrovych", "Oleksandrovych", "Mykolaivna", "Vasylivna", "Dmytrivna", "Andriyovych", "Ihorivna"};
        String[] cities = {"Kyiv", "Lviv", "Odesa", "Kharkiv", "Dnipro"};

        String[] streets = {"Khreshchatyk St", "Sadova St", "Svobody Ave", "Lesya Ukrainka St"};

        for (int i = 0; i < count; i++) {
            String lastName = lastNames[rand.nextInt(lastNames.length)];
            String firstName = firstNames[rand.nextInt(firstNames.length)];
            String patronymic = patronymics[rand.nextInt(patronymics.length)];

            String address = cities[rand.nextInt(cities.length)] + ", " + 
                             streets[rand.nextInt(streets.length)] + ", hom. " + (1 + rand.nextInt(150));

            String cardNumber = generateCardNumber(rand);


            double balanceValue = (rand.nextDouble() * 105000) - 5000;
            BigDecimal balance = new BigDecimal(balanceValue)
                                    .setScale(2, RoundingMode.HALF_UP);
            
            customers.add(new Customer(idCounter.getAndIncrement(), lastName, firstName, patronymic,
                                       address, cardNumber, balance));
        }
        return customers;
    }
}