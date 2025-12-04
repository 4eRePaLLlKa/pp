package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class AddCarCommand implements Command {
    private TaxiPark manager;
    public AddCarCommand(TaxiPark manager) { this.manager = manager; }
    @Override
    public void execute() { manager.addCar(); }
}
