package ua.lviv.oi.taxi.model;

public class EconomyCar extends Car {
    private boolean allowsPets; // Чи можна з тваринами

    public EconomyCar(int id, String modelAndBrand, double price, double fuelConsumption, int maxSpeed, boolean allowsPets) {
        super(id, modelAndBrand, price, fuelConsumption, maxSpeed);
        this.allowsPets = allowsPets;
    }

    public boolean isAllowsPets() { return allowsPets; }

    @Override
    public String toString() {
        return super.toString() + ", Type: Economy, Pets: " + (allowsPets ? "Yes" : "No");
    }

    @Override
    public String toCSV() {
        return "Economy," + super.toCSV() + "," + allowsPets;
    }
}