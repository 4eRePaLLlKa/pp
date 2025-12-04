package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class SearchCommand implements Command {
    private TaxiPark manager;
    public SearchCommand(TaxiPark manager) { this.manager = manager; }
    @Override
    public void execute() { manager.searchBySpeedRange(); }
}
