package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class CalculateCostCommand implements Command {
    private TaxiPark manager;
    public CalculateCostCommand(TaxiPark manager) { this.manager = manager; }
    @Override
    public void execute() { manager.calculateTotalCost(); }
}
