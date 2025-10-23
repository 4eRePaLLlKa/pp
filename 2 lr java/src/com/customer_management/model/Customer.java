package com.customer_management.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Customer {
    private long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String address;
    private String creditCardNumber; 
    private BigDecimal accountBalance; 

    // Конструктор для створення об'єкта
    public Customer(long id, String lastName, String firstName, String patronymic,
                    String address, String creditCardNumber, BigDecimal accountBalance) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.accountBalance = accountBalance;
    }

    // Методи getValue() (гетери)
    public long getId() { return id; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getPatronymic() { return patronymic; }
    public String getAddress() { return address; }
    public String getCreditCardNumber() { return creditCardNumber; }
    public BigDecimal getAccountBalance() { return accountBalance; }

    // Методи setValue() (сетери)
    public void setId(long id) { this.id = id; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setPatronymic(String patronymic) { this.patronymic = patronymic; }
    public void setAddress(String address) { this.address = address; }
    public void setCreditCardNumber(String creditCardNumber) { this.creditCardNumber = creditCardNumber; }
    public void setAccountBalance(BigDecimal accountBalance) { this.accountBalance = accountBalance; }


    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");
        return String.format(
            "Customer [id=%d, PIB: %s %s %s, Address: %s, Card: %s, Balance: %s grn]",
            id, lastName, firstName, patronymic, address, creditCardNumber, df.format(accountBalance)
        );
    }
}