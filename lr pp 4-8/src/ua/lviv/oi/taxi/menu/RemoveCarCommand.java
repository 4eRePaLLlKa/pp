package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class RemoveCarCommand implements Command {
    private TaxiPark manager;
    public RemoveCarCommand(TaxiPark manager) { this.manager = manager; }
    @Override
    public void execute() { manager.removeCar(); }
}
