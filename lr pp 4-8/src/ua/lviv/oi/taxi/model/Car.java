package ua.lviv.oi.taxi.model;

public abstract class Car {
    private int id;                 // Унікальний номер
    private String modelAndBrand;   // Модель та марка
    private double price;           // Вартість
    private double fuelConsumption; // Витрата пального
    private int maxSpeed;           // Максимальна швидкість

    public Car(int id, String modelAndBrand, double price, double fuelConsumption, int maxSpeed) {
        this.id = id;
        this.modelAndBrand = modelAndBrand;
        this.price = price;
        this.fuelConsumption = fuelConsumption;
        this.maxSpeed = maxSpeed;
    }

    // Геттери (щоб отримати значення полів)
    public int getId() { return id; }
    public String getModelAndBrand() { return modelAndBrand; }
    public double getPrice() { return price; }
    public double getFuelConsumption() { return fuelConsumption; }
    public int getMaxSpeed() { return maxSpeed; }

    @Override
    public String toString() {
        return "ID: " + id + ", Model: " + modelAndBrand +
                ", Price: " + price + "$, Fuel: " + fuelConsumption + "l/100km, Speed: " + maxSpeed + "km/h";
    }

    // Метод для запису у файл (CSV формат)
    public String toCSV() {
        return id + "," + modelAndBrand + "," + price + "," + fuelConsumption + "," + maxSpeed;
    }
}