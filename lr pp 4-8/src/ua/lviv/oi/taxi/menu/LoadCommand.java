package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class LoadCommand implements Command {
    private TaxiPark manager;

    public LoadCommand(TaxiPark manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.loadFromFile();
    }
}