package ua.lviv.oi.taxi.model;

public class VanCar extends Car {
    private int seatsCount; // Кількість місць

    public VanCar(int id, String modelAndBrand, double price, double fuelConsumption, int maxSpeed, int seatsCount) {
        super(id, modelAndBrand, price, fuelConsumption, maxSpeed);
        this.seatsCount = seatsCount;
    }

    public int getSeatsCount() { return seatsCount; }

    @Override
    public String toString() {
        return super.toString() + ", Type: Van, Seats: " + seatsCount;
    }

    @Override
    public String toCSV() {
        return "Van," + super.toCSV() + "," + seatsCount;
    }
}