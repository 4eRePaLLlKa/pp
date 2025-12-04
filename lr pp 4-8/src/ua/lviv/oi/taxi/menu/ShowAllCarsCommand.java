package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class ShowAllCarsCommand implements Command {
    private TaxiPark manager;
    public ShowAllCarsCommand(TaxiPark manager) { this.manager = manager; }
    @Override
    public void execute() { manager.showAllCars(); }
}
