package ua.lviv.oi.taxi.menu;

import ua.lviv.oi.taxi.park.TaxiPark;

public class SaveCommand implements Command {
    private TaxiPark manager;

    public SaveCommand(TaxiPark manager) {
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.saveToFile();
    }
}