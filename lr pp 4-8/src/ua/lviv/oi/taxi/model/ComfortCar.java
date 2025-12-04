package ua.lviv.oi.taxi.model;

public class ComfortCar extends Car {
    private boolean hasChildSafetySeat; // Дитяче крісло

    public ComfortCar(int id, String modelAndBrand, double price, double fuelConsumption, int maxSpeed, boolean hasChildSafetySeat) {
        super(id, modelAndBrand, price, fuelConsumption, maxSpeed);
        this.hasChildSafetySeat = hasChildSafetySeat;
    }

    public boolean hasChildSafetySeat() { return hasChildSafetySeat; }

    @Override
    public String toString() {
        return super.toString() + ", Type: Comfort, Child Seat: " + (hasChildSafetySeat ? "Yes" : "No");
    }

    @Override
    public String toCSV() {
        return "Comfort," + super.toCSV() + "," + hasChildSafetySeat;
    }
}