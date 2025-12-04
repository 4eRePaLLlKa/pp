package ua.lviv.oi.taxi.model;

public class PremiumCar extends Car {
    private boolean hasBar; // Міні-бар

    public PremiumCar(int id, String modelAndBrand, double price, double fuelConsumption, int maxSpeed, boolean hasBar) {
        super(id, modelAndBrand, price, fuelConsumption, maxSpeed);
        this.hasBar = hasBar;
    }

    public boolean hasBar() { return hasBar; }

    @Override
    public String toString() {
        return super.toString() + ", Type: Premium, Bar: " + (hasBar ? "Yes" : "No");
    }

    @Override
    public String toCSV() {
        return "Premium," + super.toCSV() + "," + hasBar;
    }
}